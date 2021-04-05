package sample;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    public int nrOfServers;
    public int nrOfTasks;
    public int simulationTime;
    public int minArrivalTime,maxArrivalTime;
    public int minProcessingTime,maxProcessingTime;
    public SelectionPolicy selectionPolicy;
    private List<Task> generatedTasks=new ArrayList<>();
    private Scheduler scheduler;
    private UserInterface userInterface;
    private FileWriter myWriter=null;
    private int taskNumber;
    private double averageServiceTime;
    private int tasksInServersPerTime[];
    public SimulationManager(int nrOfServers, int nrOfTasks, int simulationTime, int minArrivalTime,int maxArrivalTime,int minProcessingTime, int maxProcessingTime, SelectionPolicy selectionPolicy){
        this.nrOfServers=nrOfServers;
        this.nrOfTasks=nrOfTasks;
        this.simulationTime=simulationTime;
        this.minArrivalTime=minArrivalTime;
        this.maxArrivalTime=maxArrivalTime;
        this.minProcessingTime=minProcessingTime;
        this.maxProcessingTime=maxProcessingTime;
        this.selectionPolicy=selectionPolicy;
        userInterface=new UserInterface();
        userInterface.displayQueues(nrOfServers);
        scheduler=new Scheduler(this.nrOfServers,this.nrOfTasks);
        generateRandomTasks();
        try {
            myWriter=new FileWriter("log.txt");
            myWriter.write("Log of events\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        taskNumber=-1;
        double sumServiceTimes=0;
        for(Task task: generatedTasks){
            sumServiceTimes+=task.getProcessingTime();
        }
        averageServiceTime=sumServiceTimes/nrOfTasks;
        tasksInServersPerTime=new int[simulationTime];
        for(int i=0;i<simulationTime;i++){
            tasksInServersPerTime[i]=0;
        }
    }
    public void generateRandomTasks(){
        for(int i=0;i<nrOfTasks;i++) {
            Random randomArrivalTime = new Random();
            int arrivalTime=randomArrivalTime.nextInt(((maxArrivalTime)-minArrivalTime)+1)+minArrivalTime;
            Random randomProcessingTime=new Random();
            int processingTime=randomProcessingTime.nextInt(((maxProcessingTime)-minProcessingTime)+1)+minProcessingTime;
            Task task=new Task(i+1,arrivalTime,processingTime);
            generatedTasks.add(task);
        }
        Collections.sort(generatedTasks);
        for(int i=0;i<nrOfTasks;i++){
            System.out.println(generatedTasks.get(i).getID()+" "+generatedTasks.get(i).getArrivalTime()+" "+generatedTasks.get(i).getProcessingTime());
        }
    }
    public void setServersCurrentTime(int currentTime){
        for(Server server:scheduler.getServers()){
            server.setCurrentTime(currentTime);
        }
    }
    public void computeStatistic(){
        try {
            myWriter = new FileWriter("log.txt",true);
            myWriter.write("Average service time: "+averageServiceTime+"\n");
            int peakClients=0;
            for(int i=0;i<simulationTime;i++){
                if(tasksInServersPerTime[i]>peakClients){
                    peakClients=tasksInServersPerTime[i];
                }
            }
            myWriter.write("Peak hour(s): ");
            for(int i=0;i<simulationTime;i++){
                if(peakClients==tasksInServersPerTime[i]){
                    myWriter.write(i+", ");
                }
            }
            double sumWaitingTimes=0;
            for(Task task: generatedTasks){
                task.setWaitingTime(task.getWaitingTime()-task.getArrivalTime());
                sumWaitingTimes+=task.getWaitingTime();
                System.out.println(task.getID()+" "+task.getWaitingTime());
            }
            double averageWaitingTime=sumWaitingTimes/nrOfTasks;
            myWriter.write("\nAverage waiting time: "+averageWaitingTime);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeWaitingTasks(){
        try {
            myWriter = new FileWriter("log.txt",true);
            myWriter.write("Waiting tasks: ");
            for(int j=taskNumber+1;j<generatedTasks.size();j++) {
                if (j == generatedTasks.size() - 1) {
                    myWriter.write("(" + generatedTasks.get(j).getID() + ", " + generatedTasks.get(j).getArrivalTime() + ", " + generatedTasks.get(j).getProcessingTime() + ")");
                } else {
                    myWriter.write("(" + generatedTasks.get(j).getID() + ", " + generatedTasks.get(j).getArrivalTime() + ", " + generatedTasks.get(j).getProcessingTime() + "),");
                }
            }
            myWriter.write("\n");
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void terminateAllThreads(){
        for(Server server: scheduler.getServers()){
            server.setStopThread(true);
        }
    }
    public void writeTime(int currentTime){
        try {
            myWriter = new FileWriter("log.txt",true);
            myWriter.write("Time "+currentTime+"\n");
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void writeQueues(int i){
        try {
            myWriter = new FileWriter("log.txt",true);
            myWriter.write("Q"+i+":");
            for(Task task: scheduler.getServers().get(i).getTasks()){
                myWriter.write("("+task.getID()+","+task.getArrivalTime()+","+task.getProcessingTime()+"), ");
            }
            if(scheduler.getServers().get(i).getTasks().size()==0){
                myWriter.write("closed");
            }
            myWriter.write("\n");
            myWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        int currentTime=0;
        while(currentTime<simulationTime){
            //System.out.println("Current time:"+currentTime);
            setServersCurrentTime(currentTime);
            writeTime(currentTime);
            userInterface.changeTime(String.valueOf(currentTime));
            int k=0;
            for(Task task: generatedTasks){
                if(task.getArrivalTime()==currentTime){
                    scheduler.dispatchTask(task,selectionPolicy);
                    taskNumber=k;
                }
                k++;
            }
            for(int i=0;i<scheduler.getServers().size();i++){
                writeQueues(i);
                tasksInServersPerTime[currentTime]+=scheduler.getServers().get(i).getTasks().size();
            }
            writeWaitingTasks();
            userInterface.updateQ(scheduler.getServers(),nrOfTasks);
            userInterface.updateListOfTasks(generatedTasks,taskNumber,scheduler.getServers(),nrOfTasks);
            currentTime++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        terminateAllThreads();
        computeStatistic();
    }
}

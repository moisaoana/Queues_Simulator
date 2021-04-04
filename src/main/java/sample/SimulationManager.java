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
    public SelectionPolicy selectionPolicy=SelectionPolicy.SHORTEST_TIME;
    private List<Task> generatedTasks=new ArrayList<>();
    private Scheduler scheduler;
    private UserInterface userInterface;
    private FileWriter myWriter=null;
    private int taskNumber;
    public SimulationManager(int nrOfServers, int nrOfTasks, int simulationTime, int minArrivalTime,int maxArrivalTime,int minProcessingTime, int maxProcessingTime){
        this.nrOfServers=nrOfServers;
        this.nrOfTasks=nrOfTasks;
        this.simulationTime=simulationTime;
        this.minArrivalTime=minArrivalTime;
        this.maxArrivalTime=maxArrivalTime;
        this.minProcessingTime=minProcessingTime;
        this.maxProcessingTime=maxProcessingTime;
        System.out.println(this.nrOfServers+" "+this.nrOfTasks+" "+this.simulationTime+" "+this.minArrivalTime+" "+this.maxArrivalTime+" "+this.minProcessingTime+" "+maxProcessingTime);
         userInterface=new UserInterface();
        userInterface.displayQueues(nrOfServers);
        scheduler=new Scheduler(this.nrOfServers,this.nrOfTasks);
        generateRandomTasks();
        //generatedTasks.add(new Task(1,2,2));
       //generatedTasks.add(new Task(2,3,3));
       //generatedTasks.add(new Task(3,4,3));
       //generatedTasks.add(new Task(4,10,2));
        try {
            myWriter=new FileWriter("log.txt");
            myWriter.write("Log of events\n");
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        taskNumber=-1;
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
    @Override
    public void run() {
        int currentTime=0;
        while(currentTime<=simulationTime){
            System.out.println("Current time:"+currentTime);
            try {
                myWriter = new FileWriter("log.txt",true);
                myWriter.write("Time "+currentTime+"\n");
                myWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
          userInterface.changeTime(String.valueOf(currentTime));
            int k=0;
            for(Task task: generatedTasks){
                if(task.getArrivalTime()==currentTime){
                    scheduler.dispatchTask(task);
                    taskNumber=k;
                }
                k++;
            }
            for(int i=0;i<scheduler.getServers().size();i++){
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

            userInterface.updateQ(scheduler.getServers(),nrOfTasks);
             userInterface.updateListOfTasks(generatedTasks,taskNumber,scheduler.getServers(),nrOfTasks);
            currentTime++;
            try {

                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Server server: scheduler.getServers()){
            server.setStopThread(true);
        }
    }
}

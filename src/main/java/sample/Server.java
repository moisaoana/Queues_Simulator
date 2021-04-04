package sample;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task>tasks;
    private AtomicInteger waitingPeriod;
    private boolean stopThread;
    public Server(int maxSize){
        this.waitingPeriod=new AtomicInteger(0);
        this.tasks=new ArrayBlockingQueue<Task>(maxSize);
        this.stopThread=false;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }
    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public boolean isStopThread() {
        return stopThread;
    }

    public void setStopThread(boolean stopThread) {
        this.stopThread = stopThread;
    }

    public void addTaskToQueue(Task newTask)  {
        try {
            this.tasks.put(newTask);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.waitingPeriod.getAndIncrement();
    }
    @Override
    public void run() {
        while(!stopThread){
                try {
                    Thread.sleep(2000);
                    Task current = tasks.peek();
                    if(current!=null) {
                        //Thread.sleep((current.getProcessingTime())*2000);
                            current.setProcessingTime(current.getProcessingTime()-1);
                        if(current.getProcessingTime()==0) {
                            this.tasks.take();
                            waitingPeriod.getAndDecrement();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }
}

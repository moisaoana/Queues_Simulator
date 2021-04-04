package sample;

import java.io.Serializable;
import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server shortestServer=servers.get(0);
        for(Server server: servers){
            if(getTotalWaitingTime(server)<getTotalWaitingTime(shortestServer)){
                shortestServer=server;
            }
        }
        shortestServer.addTaskToQueue(task);
    }
    public int getTotalWaitingTime(Server server){
        int time=0;
        for(Task task: server.getTasks()){
            time+=task.getProcessingTime();
        }
        return time;
    }
}

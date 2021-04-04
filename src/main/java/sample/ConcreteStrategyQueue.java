package sample;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyQueue implements Strategy {
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server shortestServer=servers.get(0);
        for(Server server: servers){
            if(server.getWaitingPeriod().get()<shortestServer.getWaitingPeriod().get()){
                shortestServer=server;
            }
        }
        shortestServer.addTaskToQueue(task);
    }
}

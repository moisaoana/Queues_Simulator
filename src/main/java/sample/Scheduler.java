package sample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Server> servers=new ArrayList<>();
    private int maxNrServers;
    private int maxTasksPerServer;
    private Strategy strategy;
    public Scheduler(int maxNrServers, int maxTasksPerServer){
        for(int i=0;i<maxNrServers;i++){
            Server server=new Server(maxTasksPerServer);
            servers.add(server);
            Thread thread=new Thread(server);
            thread.start();
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public void changeStrategy(SelectionPolicy selectionPolicy){
        if(selectionPolicy==SelectionPolicy.SHORTEST_QUEUE){
            strategy=new ConcreteStrategyQueue();
        }
        if(selectionPolicy==SelectionPolicy.SHORTEST_TIME){
            strategy=new ConcreteStrategyTime();
        }
    }
    public void dispatchTask(Task task,SelectionPolicy selectionPolicy){
        changeStrategy(selectionPolicy);
        strategy.addTask(servers,task);
    }
}

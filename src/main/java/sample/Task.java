package sample;

public class Task implements Comparable< Task>{
    private int ID;
    private int arrivalTime;
    private int processingTime;
    private int waitingTime;
    public Task(int ID, int arrivalTime, int processingTime){
        this.ID=ID;
        this.arrivalTime=arrivalTime;
        this.processingTime=processingTime;
        this.waitingTime=-1;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public int getID() {
        return ID;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public int compareTo(Task o) {
        return Integer.compare(arrivalTime,o.getArrivalTime());
    }
}

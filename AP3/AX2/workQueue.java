/*
 * Name:Seah Hao Jian
 * GUID:2228156S
 * Title: APH Exercise 2
 * This is my own work as defined in the Academic Ethics agreement I have signed..
 */


import java.util.concurrent.ConcurrentLinkedQueue;

public class workQueue<workItem> extends ConcurrentLinkedQueue<workItem> {

    int waiting = 0;
    int totalThreads;
    private static final long serialVersionUID = 0;

    public workQueue() {
    }

    public void setTotalThreads(int t) {
        totalThreads = t;
    }

    //synchronised method for adding work to queue
    public synchronized void addWork(workItem p) {
        this.offer(p);
        this.notifyAll();
    }

    //synchronised method for getting work from queue
    public synchronized workItem getWork() throws InterruptedException {
        //notifyAll();
        if (this.isEmpty()) {
            waiting++;
            if (waiting == totalThreads) {
                //if all waiting i.e time to stop the program
                //exit gracefully by interrupting the thread as it waits
                waiting--;
                totalThreads--;
                Thread.currentThread().interrupt();
                notifyAll();
            }
            wait();
            waiting--;
        }
        workItem p = this.poll();
        notifyAll();
        return p;
    }

}

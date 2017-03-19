package Server;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Tiffany on 3/19/2017.
 */
public class Queue {
    private volatile int head, tail;
    private int[] requests;
    private ReentrantLock lock;
    public Queue (int capacity){
        head = 0;
        tail = 0;
        lock = new ReentrantLock();

    }
    public void enq(int request) throws Exception{
        lock.lock();
        try{
            if(tail - head == requests.length){
                throw new Exception("Queue is full");
            }
            requests[tail % requests.length] = request;
            tail ++;

        }
        finally{
            lock.unlock();
        }
    }
    public int deq() throws Exception{
        if (tail == head) {
            throw new Exception("Queue is empty");
        }
        int x = requests[head % requests.length];
        head++;
        return x;
    }
}


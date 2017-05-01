package Server;

import java.util.concurrent.locks.ReentrantLock;

public class Queue {

    // Main method created to test the queue

    public static void main(String args[]){
        String test;
        /*
        Queue queue = new Queue(10);
        queue.enq("Hello my friend!");
        if(!(test = queue.deq()).equals("")) {
            System.out.println(test);
        }
        if(!(test = queue.deq()).equals("")) {
            System.out.println(test);
        }
        */
    }

    private volatile int head, tail;
    private String[] requests;
    private ReentrantLock lock;
    public Queue (int capacity){
        head = 0;
        tail = 0;
        lock = new ReentrantLock();
        requests = new String[capacity];
    }
    public boolean enq(String request){
        lock.lock();
        try{
            if(tail - head == requests.length){
                return false;
            }
            requests[tail % requests.length] = request;
            tail ++;

        }
        finally{
            lock.unlock();
        }
        return true;
    }
    public String deq(){
        if (tail == head) {
            return "";
        }
        String x = requests[head % requests.length];
        head++;
        return x;
    }
}


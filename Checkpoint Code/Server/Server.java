// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Server;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

class Server {
    private static Mediator allUsers[] = new Mediator[10];
    private static int numUsers = 0;
    private static Queue messageQueue = new Queue(20);

    public static void main(String args[]) throws Exception {
        String sendAll;
        new Thread(Server::listener).start();



        while(true) {
            //System.out.println(numUsers);
            TimeUnit.SECONDS.sleep(1);
            // Q.lock()
            // message = Q.dequeue()
            // Q.unlock()
            // user.writeMessage(message)
            /*
            Old code

            if (numUsers == 2) {
                for (Mediator mediator : allUsers) {
                    mediator.writeMessage("Hello worfhsjklafskjdhld\n");
                    TimeUnit.SECONDS.sleep(4);
                    mediator.writeMessage("Message 2 comin' at you!\n");
                    TimeUnit.SECONDS.sleep(4);
                    mediator.writeMessage("Close\n");
                }
                break;
            */
            if(!(sendAll = messageQueue.deq()).equals("")) {
                for (Mediator mediator : allUsers) {
                    try {
                        mediator.writeMessage(sendAll + "\n");
                    }catch (Exception e){}
                }
            }
        }

    }

    static private void listener(){
        ServerSocket welcomeSocket;
        try {
            welcomeSocket = new ServerSocket(9090);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        while(true) {
            Socket connectionSocket;
            Mediator newMediator;

            try {
                connectionSocket = welcomeSocket.accept();
                newMediator = new Mediator(connectionSocket);
                new Thread(newMediator).start();
                newMediator.writeMessage("Welcome to the chat room!\n");
            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            allUsers[numUsers] = newMediator;
            numUsers++;
        }
    }

    // Pushes the messages onto the queue
    static void writeQueue(String message){
        messageQueue.enq(message);
    }


}

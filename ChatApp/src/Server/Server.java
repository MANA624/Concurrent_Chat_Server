// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

class Server implements Runnable{
    private static Mediator allUsers[] = new Mediator[10];
    private static int numUsers = 0;
    private static Queue messageQueue = new Queue(20);

    public static void main(String args[]) throws Exception {
        String sendAll;
        new Thread(new Server()).start();

        while(true) {
            TimeUnit.SECONDS.sleep(1);
            if(!(sendAll = messageQueue.deq()).equals("")) {
                for (Mediator mediator : allUsers) {
                    try {
                        mediator.writeMessage(sendAll + "\n");
                    }catch (Exception e){}
                }
            }
        }

    }

     public void run(){
        ServerSocket welcomeSocket;
        String userName;
        try {
            welcomeSocket = new ServerSocket(9090);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        outside: while(true) {
            Socket connection;
            Mediator newMediator;

            try {
                connection = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
                boolean userNameTaken;
                while(true) {
                    userNameTaken = false;
                    try {
                        userName = inFromClient.readLine();
                    }catch (Exception e){
                        continue outside;
                    }
                    for(Mediator mediator : allUsers){
                        try {
                            if (userName.equals(mediator.getUserName())) {
                                userNameTaken = true;
                            }
                        }catch (Exception e) {}
                    }
                    if(userNameTaken){
                        outToClient.writeBytes("Username taken\n");
                    }
                    else{
                        outToClient.writeBytes("\n");
                        break;
                    }
                }

                newMediator = new Mediator(connection, numUsers, userName);
                new Thread(newMediator).start();
                newMediator.writeMessage("Welcome to the chat room, " + userName + "!\n");
                newMediator.writeMessage("Type a message to start chatting or type 'exit' to leave\n");
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

    public static void killMediator(int userIndex){
        allUsers[userIndex] = null;
        return;
    }

}

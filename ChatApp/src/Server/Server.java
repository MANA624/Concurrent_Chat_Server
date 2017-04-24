// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

public class Server implements Runnable{
    private final static int maxUsers = 10;
    private static Mediator allUsers[] = new Mediator[maxUsers];
    // private static int numUsers = 0;
    private static Queue messageQueue = new Queue(20);

    public static void main(String args[]) throws Exception {
        String sendAll;
        new Thread(new Server()).start();

        while(true) {
            TimeUnit.SECONDS.sleep(1);
            if(!(sendAll = messageQueue.deq()).equals("")) {
//                for (Mediator mediator : allUsers) {
//                    if(mediator != null){
//                        mediator.writeMessage(sendAll + "\n");
//                    }
//                }
                for(int i=0; i<maxUsers; i++){
                    if(allUsers[i] != null){
                        // System.out.println(i);
                        allUsers[i].writeMessage(sendAll + "\n");
                    }
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
                            if (mediator != null && userName.equals(mediator.getUserName())) {
                                userNameTaken = true;
                            }
                        }catch (Exception e) {
                            System.out.println("THIS ERROR");
                        }
                    }
                    if(userNameTaken){
                        outToClient.writeBytes("Username taken\n");
                    }
                    else{
                        outToClient.writeBytes("\n");
                        break;
                    }
                }

            }catch(Exception e){
                e.printStackTrace();
                return;
            }
            int i = 0;
            while(allUsers[i] != null && i < maxUsers){
                i++;
            }
            try {
                newMediator = new Mediator(connection, i, userName);
            }
            catch (Exception e){
                e.printStackTrace();
                return;
            }
            if(i < maxUsers) {
                try {
                    allUsers[i] = newMediator;
                    new Thread(newMediator).start();
                    newMediator.writeMessage("Welcome to the chat room, " + userName + "!\n");
                    newMediator.writeMessage("Type a message to start chatting or type 'exit' to leave\n");
                }
                catch (Exception e){
                    System.out.println("Hey!");
                }
            }
            else{
                try {
                    newMediator.writeMessage("There is currently no room in the chat room. Please try again later\n");
                }catch (Exception e) {
                    // e.printStackTrace();
                }

            }
            // numUsers++;
        }
    }

    // Pushes the messages onto the queue
    static void writeQueue(String message){
        messageQueue.enq(message);
    }

    public static void killMediator(int userIndex){
        // System.out.println("Mediator " + allUsers[userIndex].getUserName() + " killed");
        allUsers[userIndex] = null;
        // numUsers--;

    }

}

// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

public class Server implements Runnable{
    private final static int maxUsers = 20;
    private static Mediator allUsers[] = new Mediator[maxUsers];
    private static Queue messageQueue = new Queue(20);

    public static void main(String args[]) throws Exception {
        String sendAll;
        new Thread(new Server()).start();

        //While the server is running
        while(true) {
            TimeUnit.SECONDS.sleep(1);
            //Index through an array of each user's messages
            if(!(sendAll = messageQueue.deq()).equals("")) {
                for(int i=0; i<maxUsers; i++){
                    //if a client has submitted a message,
                    if(allUsers[i] != null){
                        //send that message to all clients
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
                    }
                    //if the server is waiting to read a client username, but they disconnect before they do
                    catch (Exception e){
                        continue outside;
                    }

                    //Ensure server takes in unique usernames from clients
                    for(Mediator mediator : allUsers){
                        try {
                            //if there is a user and the requested userName matches a pre-existing username
                            if (mediator != null && userName.equals(mediator.getUserName())) {
                                userNameTaken = true;
                            }
                        }catch (Exception e) {
                            System.out.println("Username uniqueness check failure");
                        }
                    }
                    //send Username taken to client class who will then inform the client
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
            //While there is room in the chat room add new connections
            int i = 0;
            while(i < maxUsers && allUsers[i] != null) {
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
                    //Welcome each client to the chat-room and get them started
                    allUsers[i] = newMediator;
                    new Thread(newMediator).start();
                    newMediator.writeMessage("Welcome to the chat room, " + userName + "!\n");
                    newMediator.writeMessage("Type a message to start chatting or type 'exit' to leave\n");
                }
                catch (Exception e){
                    System.out.println("Hey!");
                }
            }
            //There is no room in the chat room
            else {
                try {
                    newMediator.writeMessage("There is currently no room in the chat room. Please try again later\n");
                } catch (Exception e) {
                    System.out.println("Failed to write 'no room' to the user");
                }
            }
        }
    }

    // Pushes the messages onto the queue
    static void writeQueue(String message){
        messageQueue.enq(message);
    }

    //if client exits the chat room, remove mediator
    static void killMediator(int userIndex){
        allUsers[userIndex] = null;
    }
}

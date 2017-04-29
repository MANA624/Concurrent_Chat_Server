package Server;

import java.io.*;
import java.net.*;

import static Server.Server.killMediator;
import static Server.Server.writeQueue;

// Mediator is the intermediate line of communication between the Client and the Server classes
public class Mediator implements Runnable{
    private Socket connection;
    private int userIndex;
    private String userName;
    private DataOutputStream outToClient;
    private volatile boolean finished = false;

    Mediator(Socket _connection, int _userIndex, String _userName) throws Exception{
        connection = _connection;
        userIndex = _userIndex;
        userName = _userName;
    }

    void writeMessage(String message){ //to the client
        try {
            outToClient = new DataOutputStream(connection.getOutputStream());
            outToClient.writeBytes(message);
        }catch (java.io.IOException E){
            System.out.println("Failed to Write Message");
        }
    }

    public void run(){
        System.out.println("Connection Successfully Made Between Client and Server");
        String clientSentence;
        BufferedReader inFromClient;
        Server.writeQueue(userName + " has entered the chat room!");
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e){
            System.out.println("Unable to create buffer");
            return;
        }
        //While there is still a connection
        while(!finished) {
            //Enq() the client's messages
            try {
                clientSentence = inFromClient.readLine();
                if (clientSentence == null) {
                    throw new ConnectException();
                }
                clientSentence = userName + ": " + clientSentence;
                System.out.println(clientSentence);
                writeQueue(clientSentence);
            }
            catch (Exception e){
                killMediator(userIndex);
                writeQueue(userName + " has left the chat room :(");
                System.out.println("Client-Server Connection Lost");
                break;
            }
        }
    }

    String getUserName(){
        return userName;
    }
}

package Server;

import java.io.*;
import java.net.*;

import static Server.Server.killMediator;
import static Server.Server.writeQueue;

public class Mediator implements Runnable{
    private Socket connection;
    private int userIndex;
    private String userName;
    public Mediator(Socket _connection, int _userIndex, String _userName) throws Exception{
        this.connection = _connection;
        userIndex = _userIndex;
        userName = _userName;
    }

    public void writeMessage(String message) throws Exception{
        DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
        outToClient.writeBytes(message);
    }

    public void run(){
        System.out.println("I am running!");
        String clientSentence;
        BufferedReader inFromClient;
        writeQueue(userName + " has entered the chat room!");
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e){
            System.out.println("Unable to create buffer");
            return;
        }
        while(true) {
            try {
                clientSentence = inFromClient.readLine();
                if(clientSentence == null){
                    throw new ConnectException();
                }
                clientSentence = userName + ": " + clientSentence;
                System.out.println(clientSentence);
                Server.writeQueue(clientSentence);

            }catch (Exception e){
                killMediator(userIndex);
                writeQueue(userName + " has left the chat room :(");
                System.out.println("Connection Lost");
                break;
            }
        }
    }

    public String getUserName(){
        return userName;
    }
}

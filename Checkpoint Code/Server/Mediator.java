package Server;

import java.io.*;
import java.net.*;

public class Mediator implements Runnable{
    private Socket connection;
    public Mediator(Socket connection) throws Exception {
        this.connection = connection;
    }

    public void writeMessage(String message) throws Exception{

        DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
        outToClient.writeBytes(message);
    }

    public void run(){
        System.out.println("I am running!");
        String clientSentence;
        BufferedReader inFromClient;
        try {
            inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }catch (Exception e){
            System.out.println("Unable to create buffer");
            return;
        }
        while(true) {
            try {

                clientSentence = inFromClient.readLine();
                // Q.lock()
                // Q.enqueue(clientSentence);
                // Q.unlock()
                // System.out.println(clientSentence);
                System.out.println("Message received!");
                Server.writeQueue(clientSentence);
            }catch (Exception e){
                System.out.println("Connection Lost");
                break;
            }
        }
    }
}

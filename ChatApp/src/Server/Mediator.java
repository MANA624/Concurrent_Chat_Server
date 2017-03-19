package Server;

import java.io.*;
import java.net.*;

public class Mediator implements Runnable{
    private Socket connection;
    public Mediator(Socket connection) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        this.connection = connection;


    }

    public void writeMessage(String message) throws Exception{

        DataOutputStream outToClient = new DataOutputStream(connection.getOutputStream());
        outToClient.writeBytes(message);
    }

    public void run(){
        String clientSentence;
        while(true) {
            try {
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                clientSentence = inFromClient.readLine();
                // Q.lock()
                // Q.enqueue(clientSentence);
                // Q.unlock()
                System.out.println(clientSentence);
            }catch (Exception e){
                System.out.println("Connection Lost");
                break;
            }
        }
    }
}

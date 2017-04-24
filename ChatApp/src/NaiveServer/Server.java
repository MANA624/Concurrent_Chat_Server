package NaiveServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private static final int CAPACITY = 10;
    private static Socket[] connections = new Socket[CAPACITY];
    private static volatile int numConnections = 0;
    private static String[] usernames = new String[CAPACITY];

    public static void main(String[] args) throws Exception{
        String clientSentence;
        String capitalizedSentence;
        for(int i=0; i<numConnections; i++){
            connections[i] = null;
        }

        Thread listener = new Thread(new Server());
        listener.start();

        while(true) {
            for(int i=0; i<numConnections; i++) {
                if(connections[i] == null){
                    continue;
                }

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connections[i].getInputStream()));
                if(!inFromClient.ready()) {
                    continue;
                }

                System.out.println("Message received!");
                clientSentence = inFromClient.readLine();
                for(int j=0; j<numConnections; j++) {
                    DataOutputStream outToClient;
                    try {
                        outToClient = new DataOutputStream(connections[j].getOutputStream());
                        outToClient.writeBytes(usernames[i] + ": " + clientSentence + "\n");
                    }catch (Exception e){
                        connections[j] = null;
                        usernames[j] = null;
                        continue;
                    }
                }
            }
        }
    }

    public void run(){
        ServerSocket welcomeSocket;
        DataOutputStream outToClient;
        Socket connection;
        String userName;
        boolean userNameTaken;
        try {
            welcomeSocket = new ServerSocket(9090);
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
        outside: while(true) {
            while (true) {
                try {
                    connection = welcomeSocket.accept();
                    BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    outToClient = new DataOutputStream(connection.getOutputStream());
                    while (true) {
                        userNameTaken = false;
                        userName = inFromClient.readLine();

                        for (int i=0; i<numConnections; i++) {
                            if (userName.equals(usernames[i])) {
                                userNameTaken = true;
                            }

                        }
                        if (userNameTaken) {
                            outToClient.writeBytes("Username taken\n");
                        } else {
                            outToClient.writeBytes("\n");
                            break;
                        }
                    }
                    if(!inFromClient.readLine().equals("okay")){
                        System.out.println("What happened?");
                        break outside;
                    }
                    outToClient.writeBytes("Welcome to the chat room, " + userName + "!\n");
                    outToClient.writeBytes("Type a message to start chatting or type 'exit' to leave\n");
                } catch (Exception e) {
                    System.out.println("User bailed!");
                    continue outside;
                }
                int i = 0;
                while (connections[i] != null && i < CAPACITY) {
                    i++;
                }
                if (i < CAPACITY) {
                    connections[i] = connection;
                    usernames[i] = userName;
                } else {
                    try {
                        outToClient.writeBytes("There is currntly no room in the chat room. Please try agian later\n");
                    } catch (Exception e) {
                    }

                }
                numConnections++;
            }
        }
    }
}

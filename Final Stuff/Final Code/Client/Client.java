// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Client;

import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.Scanner;

class Client implements Runnable
{
    //Color code messaging, distinguishing typing from sent messages
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_CYAN = "\u001B[36m";

    //create a new socket(end point for communication) for the client with a username
    private static Socket clientSocket;
    private static String userName;

    public static void main(String args[]) throws Exception
    {
        //Set-up Client Connection and Interface
        BufferedReader inFromServer; //reads text from a character-input stream
        String sentence;
        Scanner reader = new Scanner(System.in);

        //Ask user for chat identification
        System.out.print("Type your name: ");
        userName = reader.nextLine();
        try {
            clientSocket = new Socket("", 9090);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(userName + "\n");
            sentence = inFromServer.readLine();

            //Ensure a unique username
            while(sentence.equals("Username taken")){
                System.out.println("Username taken!\nType your name: ");
                userName = reader.nextLine();
                outToServer.writeBytes(userName + "\n");
                sentence = inFromServer.readLine();
            }
        }catch (Exception e){
            System.out.println("Could not connect to server!");
            return;
        }

        new Thread(new Client()).start();

        //While the server is running, print out chat messages to clients
        while(true) {
            try {
                sentence = inFromServer.readLine();
            }catch(Exception e){
                break;
            }
            if(sentence == null) {
                break;
            }
            System.out.println(ANSI_CYAN + sentence + ANSI_RESET);
        }
        clientSocket.close();

    }

    public void run(){
        // Get user input and send it to the server
        String sentence;
        DataOutputStream outToServer;

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
        try {
            //Connecting to the server
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception exe) {
            System.out.println("Connection to server failed");
            return;
        }
        while(true) {
            try {
                // Taking in client input
                sentence = inFromClient.readLine();

                //Allow client to exit chat room
                if(sentence.equals("exit")){
                    clientSocket.close();
                    break;
                }


            } catch (Exception exe) {
                System.out.println("Failure to take in user input");
                //If user input into sentence fails, override the failed message with ""
                sentence = "";
            }
            try{
                if(sentence.trim().length() > 0 ) {
                    //Type client's sentence out to server
                    outToServer.writeBytes(sentence + '\n');
                }
            }catch (Exception e){
                System.out.println("Connection to server lost!!!");
                try {
                    clientSocket.close();
                }catch (Exception exe){
                    exe.printStackTrace();
                    return;
                }
                break;
            }
        }
    }
}

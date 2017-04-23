// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Client;

import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.Scanner;

class Client implements Runnable
{
    public static final String ANSI_RESET = "\u001B[0m";
    // public static final String ANSI_BLACK = "\u001B[30m";
    // public static final String ANSI_RED = "\u001B[31m";
    // public static final String ANSI_GREEN = "\u001B[32m";
    // public static final String ANSI_YELLOW = "\u001B[33m";
    // public static final String ANSI_BLUE = "\u001B[34m";
    // public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    // public static final String ANSI_WHITE = "\u001B[37m";

    private static Socket clientSocket;
    private static String userName;
    public static void main(String args[]) throws Exception
    {
        BufferedReader inFromServer;
        String sentence;
        Scanner reader = new Scanner(System.in);
        System.out.print("Type your name: ");
        userName = reader.nextLine();
        try {
            clientSocket = new Socket("", 9090);
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeBytes(userName + "\n");
            sentence = inFromServer.readLine();
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

        while(!sentence.equals("Close")) {
            try {
                sentence = inFromServer.readLine();
            }catch(Exception e){
                break;
            }
            if(sentence == null){
                break;
            }
            //System.out.println(sentence);
            System.out.println(ANSI_CYAN + sentence + ANSI_RESET);
        }
        clientSocket.close();

    }

    public void run(){
        // In this method we need to get user input somehow and then send it to the server using the following lines
        // of code
        String sentence;
        DataOutputStream outToServer;

        BufferedReader inFromClient = new BufferedReader(new InputStreamReader(System.in));
        try {
            //Connecting to the server
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
        }
        catch(Exception exe) {
            System.out.println("Connection to server failed");
            //make sure application doesn't close on return
            return;
        }
        while(true) {
            try {
                // Taking in client input
                sentence = inFromClient.readLine();
                if(sentence.equals("exit")){
                    clientSocket.close();
                    break;
                }

            } catch (Exception exe) {
                System.out.println("Failure to take in user input");
                sentence = "";
            }
            try{
                outToServer.writeBytes(sentence + '\n');
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

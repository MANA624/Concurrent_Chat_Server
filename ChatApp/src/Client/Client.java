// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Client;

import java.io.*;
import java.net.*;
import java.lang.Thread;

class Client
{
    private static Socket clientSocket;
    public static void main(String args[]) throws Exception
    {
        String sentence = "";

        clientSocket = new Socket("localhost", 9090);

        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        new Thread(Client::userInput).start();

        while(!sentence.equals("Close")) {
            sentence = inFromServer.readLine();
            System.out.println("FROM SERVER: " + sentence);
        }
        clientSocket.close();

    }

    static private void userInput(){
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
        try {
            //taking in Tiffany's input
            sentence = inFromClient.readLine();
            outToServer.writeBytes(sentence + '\n');
        }
        catch(Exception exe){
            System.out.println("Failure to take in user input");
        }

    }
}

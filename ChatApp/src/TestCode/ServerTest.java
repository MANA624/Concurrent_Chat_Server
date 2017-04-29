package TestCode;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// The following is a threaded application to test the performance of the respective servers, since that is what we are
// trying to test, not the clients.
class ServerTest implements Runnable{
    private static final int NUM_TESTERS = 8;
    private String name;
    private BufferedReader inFromServer;
    private Socket connection;
    private DataOutputStream outToServer;
    private static String episode1;
    private static AtomicInteger numReady = new AtomicInteger(0);

    public ServerTest(String name, boolean isNaive) throws Exception{
        String sentence;
        this.name = name;
        try {
            connection = new Socket("", 9090);
            inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outToServer = new DataOutputStream(connection.getOutputStream());
            writeMessage(name);
            sentence = inFromServer.readLine();
            while(sentence.equals("Username taken")){
                name += "a";
                writeMessage(name);
                sentence = inFromServer.readLine();
            }
        }catch (Exception e){
            System.out.println("Could not connect to server!");
            return;
        }

        if(isNaive){
            try {
                writeMessage("okay");
            }catch (Exception e){
                System.out.println("Failed to make handshake");
            }
        }
        System.out.println(listen());
        System.out.println(listen());
        if(!isNaive) {
            listen();
        }

    }

    public void run(){
        writeMessage(episode1);
        for(int i=0; i<NUM_TESTERS; i++) {
            listen();
        }
        System.out.println("Finished!");
    }

    private String listen(){
        try {
            return inFromServer.readLine();
        }catch(Exception e){
            System.out.println("Ehh");
            return null;
        }
    }

    private void writeMessage(String message){
        try {
            outToServer.writeBytes(message + "\n");
        }
        catch (Exception e){
            System.out.println("Failed to send message!");
        }
    }

    public static void main(String[] args){
        // System.out.println(System.getProperty("user.dir"));
        String path = System.getProperty("user.dir") + "/src/TestCode/AllEpisodes.txt";
        episode1 = readFile(path);
        boolean isNaive;
        String userInput;
        Thread testThreads[] = new Thread[NUM_TESTERS];

        // System.out.println(episode1);
        Scanner reader = new Scanner(System.in);
        System.out.print("Are you running the full server? ('yes'/'no'):  ");
        userInput = reader.nextLine();
        if(userInput.equalsIgnoreCase("yes")){
            isNaive = false;
        }
        else if(userInput.equalsIgnoreCase("no")){
            isNaive = true;
        }
        else{
            System.out.println("Not a valid input");
            return;
        }
        for(int i=0; i<NUM_TESTERS; i++) {
            try {
                testThreads[i] = new Thread(new ServerTest("bob", isNaive));
            } catch (Exception e) {
                System.out.println("Failed to create thread. For whatever reason.");
            }
        }
        long startTimes = System.currentTimeMillis();
        for(int i=0; i<NUM_TESTERS; i++) {
            try {
                testThreads[i].start();
            } catch (Exception e) {
                System.out.println("Failed to start thread. For whatever reason.");
            }
        }

        for(int i=0; i<NUM_TESTERS; i++) {
            try {
                testThreads[i].join();
            } catch (Exception e) {
                System.out.println("Failed to join thread. For whatever reason.");
            }
        }
        long endTimes = System.currentTimeMillis(); // The endTimes
        System.out.println("Time of execution: " + (endTimes-startTimes));
    }

    private static String readFile(String fileName){
        String ret = "";
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                ret += sCurrentLine;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
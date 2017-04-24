package TestCode;

//import NaiveServer.Server;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

// The following is a threaded application to test the performance of the respective servers, since that is what we are
// trying to test, not the clients.
class ServerTest {
    private static final int NUM_TESTERS = 1;
    private String name;
    private static BufferedReader inFromServer;
    private Socket connection;
    private DataOutputStream outToServer;

    public ServerTest(String name, boolean isNaive) throws Exception{
        String sentence;
        this.name = name;
        try {
            connection = new Socket("", 9090);
            inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outToServer = new DataOutputStream(connection.getOutputStream());
            outToServer.writeBytes(name + "\n");
            sentence = inFromServer.readLine();
            while(sentence.equals("Username taken")){
                name += "a";
                outToServer.writeBytes(name + "\n");
                sentence = inFromServer.readLine();
            }
        }catch (Exception e){
            System.out.println("Could not connect to server!");
            return;
        }

        if(isNaive){
            try {
                outToServer.writeBytes("okay\n");
            }catch (Exception e){
                System.out.println("Failed to make handshake");
            }
        }
        System.out.println(listen());
        System.out.println(listen());
        System.out.println(listen());
        System.out.println(listen());
    }

    private String listen(){
        try {
            return inFromServer.readLine();
        }catch(Exception e){
            System.out.println("Ehh");
            return null;
        }
    }

    public static void main(String[] args){
        System.out.println("Hello wrold!");
        try {
            ServerTest test1 = new ServerTest("bob", false);
        }catch (Exception e){}
    }
}

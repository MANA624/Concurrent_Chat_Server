package NaiveClient;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.ConnectIOException;

public class Client implements Runnable{
    private static Socket connection;
    private static boolean finished = false;
    private static BufferedReader inFromServer;
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";

    public static void main(String[] args) throws Exception{
        String sentence;
        String userName;
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DataOutputStream outToServer;

        System.out.print("Type your name: ");
        userName = inFromUser.readLine();
        // System.out.println(userName);

        try {
            connection = new Socket("", 9090);
            inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            outToServer = new DataOutputStream(connection.getOutputStream());
            outToServer.writeBytes(userName + "\n");
            sentence = inFromServer.readLine();
            while(sentence.equals("Username taken")){
                System.out.println("Username taken!\nType your name: ");
                userName = inFromUser.readLine();
                outToServer.writeBytes(userName + "\n");
                sentence = inFromServer.readLine();
            }
        }catch (Exception e){
            System.out.println("Could not connect to server!");
            return;
        }

        Thread listener = new Thread(new Client());
        listener.start();

        // DataOutputStream outToServer = new DataOutputStream(connection.getOutputStream());
        while (!finished) {
            sentence = inFromUser.readLine();
            if(sentence.equals("exit")){
                finished = true;
                break;
            }
            outToServer.writeBytes(sentence + '\n');
        }


        connection.close();
    }

    public void run(){
        String incomingSentence;
        try {
            inFromServer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            DataOutputStream outToServer = new DataOutputStream(connection.getOutputStream());
            outToServer.writeBytes("okay\n");
        }
        catch (Exception e){
            System.out.println("Failed to listen to server. Which is ludicrous.");
            return;
        }


        while(!finished){
            try {
                incomingSentence = inFromServer.readLine();
                if(incomingSentence == null){
                    throw new ConnectIOException("");
                }
            }
            catch (Exception e){
                if(!finished) {
                    System.out.println("Lost connection to server!");
                }
                else{
                    System.out.println("Good bye!");
                }
                finished = true;
                return;
            }
            System.out.println(ANSI_CYAN + incomingSentence + ANSI_RESET);
        }
    }
}

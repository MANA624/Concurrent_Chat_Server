// Obtained base code from http://stackoverflow.com/questions/2165006/simple-java-client-server-program

package Server;

import java.net.*;
import java.util.concurrent.TimeUnit;
import java.lang.Thread;

class Server {
    private static Mediator allUsers[] = new Mediator[2];
    private static int numUsers = 0;

    public static void main(String args[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;
        new Thread(Server::listener).start();



        while(true) {
            System.out.println(numUsers);
            TimeUnit.SECONDS.sleep(1);
            // Q.lock()
            // message = Q.dequeue()
            // Q.unlock()
            // user.writeMessage(message)
            if (numUsers == 2) {
                for (Mediator mediator : allUsers) {
                    mediator.writeMessage("Hello worfhsjklafskjdhld\n");
                    TimeUnit.SECONDS.sleep(4);
                    mediator.writeMessage("Message 2 comin' at you!\n");
                    TimeUnit.SECONDS.sleep(4);
                    mediator.writeMessage("Close\n");
                }
                break;
            }
        }

    }

    static private void listener(){
        ServerSocket welcomeSocket;
        try {
            welcomeSocket = new ServerSocket(9090);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }

        while(true) {
            Socket connectionSocket;
            Mediator newMediator;

            try {
                connectionSocket = welcomeSocket.accept();
                newMediator = new Mediator(connectionSocket);
                new Thread(newMediator);
                newMediator.writeMessage("Welcome to the chat room!\n");
            }catch(Exception e){
                e.printStackTrace();
                return;
            }

            allUsers[numUsers] = newMediator;
            numUsers++;
        }
    }


}

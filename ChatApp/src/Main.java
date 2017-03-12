import java.util.Scanner;

class Main
{
    public static void main(String argv[]) throws Exception
    {
        Scanner input = new Scanner(System.in);
        System.out.println("Are you the server? 'y' or 'n': ");
        char response = input.next().charAt(0);
        if(response == 'y'){
            Server server = new Server();
        }
        else if (response == 'n'){
            Client client = new Client();
        }
        else {
            System.out.println("Not a valid response");
        }

    }
}
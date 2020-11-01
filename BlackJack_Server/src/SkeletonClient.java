import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SkeletonClient
{
    public static void main(String [] args)
    {
        String serverName = args[0];
        int port = Integer.parseInt(args[1]);
        Scanner keyboard = new Scanner(System.in);
        String typing = "firstmessage";
        try
        {
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Hello from " + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            String line = in.readUTF();
            //if user input is not needed yet just wait for server messages
            //when server sends a 0 the  server is ready to receive user input
            while(line.equals("0") == false){
                line = in.readUTF();
                System.out.println("Server says " + line);
            }
            System.out.println("Type hit or stay");
            //handle user input and send it to the server
            while(!typing.equals("exit")){
                typing = keyboard.nextLine();
                out.writeUTF(typing);
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException ie){
                    System.err.print(ie);
                }
                if(in.available() > 0){
                    System.out.println("Server replies " + in.readUTF());
                }
                else{
                    System.out.println("Turn Finished");
                    typing = "exit";
                }
            }
            System.out.println("outside input loop");
            //when input is done get the results from the server
            while(line.contains("Results:") == false){
                line = in.readUTF();
                System.out.println("Server says " + line);
            }
            out.writeUTF("exit");
            out.close();
            in.close();
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}

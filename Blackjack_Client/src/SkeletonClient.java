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
            System.out.println("Server says " + in.readUTF());
            while(!typing.equals("exit")){
                typing = keyboard.nextLine();
                out.writeUTF("pooopy doopy");
                try{
                    Thread.sleep(1000);
                }catch(InterruptedException ie){
                    System.err.print(ie);
                }
                if(in.available() > 0){
                    System.out.println("Server replies " + in.readUTF());
                }
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


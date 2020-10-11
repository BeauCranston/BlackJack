import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class SkeletonServer extends Thread {

    private Socket server;
    private static ServerSocket serverSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private static int currentClientCount = 0;
    private static BlackJackGame bj = new BlackJackGame();
    public SkeletonServer(Socket theSocket) throws IOException {
        server = theSocket;
    }


    public void run() {
        String line = "start";

        boolean gameStarted = false;
        try {
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            currentClientCount ++;
            if(gameStarted == false){
                System.out.println("there are now " + currentClientCount + " connected");
                if(currentClientCount == 2){
                    gameStarted = true;
                }
            }

            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
            /* Echo back whatever the client writes until the client exits. */
            while (!line.equals("exit")) {
                if (in.available() > 0) {
                    System.out.println(in.readUTF()  );
                    out.writeUTF("you are such a gay");
                }
            }
            out.close();
            in.close();
            server.close();

        } catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                //out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!");

        }

    }

    public static void main(String[] args) throws IOException {
        Socket clientConnection;
        int port = 5005;
        serverSocket = new ServerSocket(port);
        // Need a way to close the server without just killing it.
        while (true) {
            System.out.println("Waiting for client on port "
                    + serverSocket.getLocalPort() + "...");
            System.out.println("Minimum of " + (Integer.parseInt(args[0]) - currentClientCount) + " clients needed to play");

            Thread clientAcceptanceThread = new AcceptClients();
              //blocking
            try {
                Thread t = new SkeletonServer(clientConnection);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    static class AcceptClients extends Thread{
        public Socket clientSocket;
        public AcceptClients(Socket clientSocket){
            this.clientSocket = clientSocket;
        }


        @Override
        public void run() {
            try {
                this.clientSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}


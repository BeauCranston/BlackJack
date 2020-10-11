import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

public class SkeletonServer extends Thread {

    private Socket server;
    private DataInputStream in;
    private DataOutputStream out;
    private Socket[] players = new Socket[10];
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
            bj.addPlayer(server.getPort());
            currentClientCount ++;
            System.out.println("there are now " + currentClientCount + " connected");
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());

            /* Echo back whatever the client writes until the client exits. */
            while (!line.equals("exit")) {
                if (in.available() > 0) {
                    if(in.readUTF() == "hi"){
                        bj.advanceTurn();
                    }
                    out.writeUTF(in.readUTF());
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
        startServer(Integer.parseInt(args[0]));
    }

    /**
     *  A method that listens for new connections on the listen thread but calling the listen runnable variable. When a new user connects the method will place that new connection on a separate thread
     *  and open up the line of communication
     * @param minUserCount
     */
    public static void startServer(int minUserCount){
        Runnable listen = new Runnable() {
            @Override
            public void run() {
                try{
                    int port = 5005;
                    ServerSocket serverSocket = new ServerSocket(port);
                    // Need a way to close the server without just killing it.
                    while (true) {
                        System.out.println("Waiting for client on port" + serverSocket.getLocalPort() + "...");
                        Socket clientConnection = serverSocket.accept();
                        Thread t = new SkeletonServer(clientConnection);
                        t.start();
                    }
                } catch (IOException e) {
                        e.printStackTrace();
                }

            }
        };
        Thread listenThread = new Thread(listen);
        listenThread.start();


    }



}


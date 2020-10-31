import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SkeletonServer extends Thread {

    private Socket server;
    private static int id = 0;
    private DataInputStream in;
    private DataOutputStream out;
    private static int currentConnections;
    private static BlackJackGame2 bj;
    private static final Object lock1 = new Object();
    private static int currentTurn = 0;
    private Player thePlayer;

    public SkeletonServer(Socket theSocket) throws IOException {
        server = theSocket;

    }

    public static void updateConnectionCount(boolean isIncrement){
        if(isIncrement == true){
            currentConnections++;
        }
        else {
            currentConnections--;
        }

    }
    public static void updateTurn(){
        if(currentTurn < bj.getPlayersInGame().size()){
            currentTurn++;
        }
        else{
            currentTurn = 0;
        }

    }

    public void run() {
        String line = "start";
        synchronized (lock1){
            if(currentTurn != id){
                try {
                    lock1.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else{
                bj.addPlayer(id);
                thePlayer = bj.getPlayerById(id);
                bj.initializeHand(id);
                id++;
                System.out.println(bj.getPlayersString());
                lock1.notify();
                if(thePlayer.getId() == bj.getPlayersInGame().size() -1){
                    bj.setState(GameState.DealingCards);
                    bj.initializeDealer();
                }
                updateTurn();
            }

        }
        try {
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
            System.out.println(in.readUTF());
            /* Echo back whatever the client writes until the client exits. */
            while (!line.equals("exit")) {
                if(thePlayer.getId() == currentTurn){
                    out.writeUTF(thePlayer.showHand());
                }
            }
            updateConnectionCount(false);
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
        currentConnections = 0;
        int port = 7777;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting for client on port "
                + serverSocket.getLocalPort() + "...");
        bj = new BlackJackGame2("cards.txt");
        // Need a way to close the server without just killing it.
        while (currentConnections < 2) {
            bj.setState(GameState.Initializing);
            Socket connectionToClient = serverSocket.accept(); //blocking
            try {
                Thread t = new SkeletonServer(connectionToClient);
                t.start();

                updateConnectionCount(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(bj.getState());




        System.out.println("Game Has Started");
    }

}



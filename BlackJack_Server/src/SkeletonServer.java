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
    private static volatile BlackJackGame2 bj;
    private static final Object lock1 = new Object();
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

    public void run() {
        String line = "start";
        thePlayer = bj.getPlayerById(id);
        id++;
        System.out.println(bj.getCurrentTurn());
        while(bj.getState() != GameState.GameStarted){

        }
        try {
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
            BlackJackProtocol bjProtocol = new BlackJackProtocol(thePlayer, bj,in, out);
            System.out.println("about to hit input loop?");
            /* Echo back whatever the client writes until the client exits. */
            while (!line.equals("exit")) {
                synchronized (lock1){
                    System.out.println("hit synchronized block");
                    if(thePlayer.getId() == bj.getCurrentTurn()){
                        System.out.println("...im in");
                        System.out.println("line: " + line);
                        bjProtocol.acceptInput(line, bj.getState());
                        bj.updateTurn();
                        System.out.println("after " + thePlayer.getName() + "'s turn: " + bj.getCurrentTurn());
                        lock1.notify();
                    }
                    else{
                        System.out.println("in else");
                        try {
                            lock1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
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
        int maxConnections = Integer.parseInt(args[0]);
        int port = 7777;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting for client on port "
                + serverSocket.getLocalPort() + "...");
        bj = new BlackJackGame2("cards.txt",maxConnections);
        bj.setState(GameState.Initializing);
        // Need a way to close the server without just killing it.
        while (currentConnections < maxConnections) {

            Socket connectionToClient = serverSocket.accept(); //blocking
            try {
                Thread t = new SkeletonServer(connectionToClient);
                t.start();
                updateConnectionCount(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        bj.setState(GameState.GameStarted);
        while(bj.getState() != GameState.GameEnd){
            if(bj.getCurrentTurn() == bj.getDealer().getId()){
                if(bj.getState() == GameState.GameStarted){
                    bj.setState(GameState.DealingCards);
                    bj.updateTurn();
                }
                else if(bj.getState() == GameState.DealingCards){
                    synchronized (lock1){
                        System.out.println("dealing card to dealer..");
                        bj.getDealer().hit(bj.dealCard());
                        System.out.println("dealers hand: " + bj.getDealer().showHand());
                        if(bj.getDealer().numOfCards() == 2){
                            bj.setState(GameState.PlayerTurn);
                            System.out.println(bj.getState());
                            bj.updateTurn();

                        }
                        else{
                            bj.updateTurn();
                            System.out.println("after dealers turn: " + bj.getCurrentTurn());
                        }

                        lock1.notifyAll();
                    }
                }


            }
        }


        System.out.println(bj.getState());
        System.out.println("Game Has Started");
    }

}



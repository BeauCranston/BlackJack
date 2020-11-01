import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 *  a class that steps through the game of black jack with multiple clients concurrently
 */
public class SkeletonServer extends Thread {

    private Socket server;
    private static int id = 0;
    private DataInputStream in;
    private DataOutputStream out;
    private static int currentConnections;
    private static volatile BlackJackGame2 bj;
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();
    private Player thePlayer;

    public SkeletonServer(Socket theSocket) throws IOException {
        server = theSocket;

    }

    /**
     * a method to update the connection count up or down
     * @param isIncrement
     */
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
        // assign the player from the static black jack game object which holds the players that were instantiated.
        thePlayer = bj.getPlayerById(id);
        //increment id so that every time that the run() method runs it is someone with a new id
        id++;
        //System.out.println(bj.getCurrentTurn());
        //keep the thread in a loop until the game is started, this code is probably not needed but i am scared to remove it
        synchronized(lock2){
            if(bj.getState() != GameState.GameStarted){
                try {
                    lock2.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            in = new DataInputStream(server.getInputStream());
            out = new DataOutputStream(server.getOutputStream());
            //instantiate a BlackJackProtocol class so that the client an server can make an agreement on the input output model
            BlackJackProtocol bjProtocol = new BlackJackProtocol(thePlayer, bj,in, out);
            //System.out.println("about to hit input loop?");
            while (!line.equals("exit")) {
                // synchronized block to control the flow of execution
                synchronized (lock1){
                    //System.out.println("hit synchronized block");
                    //if it is the player turn accept input with a specific state
                    if(thePlayer.getId() == bj.getCurrentTurn()){
                        //talk to client depending on state
                        bjProtocol.acceptInput(bj.getState());
                        //it is now the next players turn
                        bj.updateTurn();
                        //System.out.println("after " + thePlayer.getName() + "'s turn: " + bj.getCurrentTurn());
                        //notify the other threads that the lock object is avaiable. The next player should now be able to take control of the lock
                        lock1.notify();
                    }
                    //if it is not the players turn the wait for a notfiy() call
                    else{
                        try {
                            lock1.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
            //decrement connection count when a player leaves
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
        //a variable to check how many connnections are currently running
        currentConnections = 0;
        //takes the max players from the args
        int maxConnections = Integer.parseInt(args[0]);
        int port = 7777;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Waiting for client on port "
                + serverSocket.getLocalPort() + "...");
        //instantiate a static instance of the blackjack game so that all threads have visibility of the games state, and players.
        bj = new BlackJackGame2("cards.txt",maxConnections);
        bj.setState(GameState.Initializing);
        // Need a way to close the server without just killing it.
        //while the room is not full allow connections

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

        //all players have connected, start the game
        synchronized (lock2){
            bj.setState(GameState.GameStarted);
            lock2.notifyAll();
        }

        // shuffle the deck
        bj.cardDeck.shuffle();
        //while the game has not ended handle the dealer's actions
        while(bj.getState() != GameState.GameEnd){
            //if it is the dealers turn execute a dealer action
            if(bj.getCurrentTurn() == bj.getDealer().getId()){
                //if the game has started then that means the players have been initiallized and are ready to be dealt cards
                if(bj.getState() == GameState.GameStarted){
                    //set the state to dealing cards
                    bj.setState(GameState.DealingCards);
                    //change turn to the players turn (dealer always goes last)
                    bj.updateTurn();
                }
                //if the game state is dealing cards then that means that cards have already been dealt to the players
                else if(bj.getState() == GameState.DealingCards){
                    //take control of the lock so that the players are waiting for the dealer to get his card
                    synchronized (lock1){
                        System.out.println("dealing card to dealer..");
                        bj.getDealer().hit(bj.dealCard());
                        System.out.println("dealers hand: " + bj.getDealer().showHand());
                        //if the dealer has his starting 2 cards then change the game state to player turn since the dealer will receive the cards last
                        if(bj.getDealer().numOfCards() == 2){
                            //set the state to player turn
                            bj.setState(GameState.PlayerTurn);
                            System.out.println(bj.getState());
                        }
                        //change to the players turn
                        bj.updateTurn();
                        //release the lock so that the players can execute their turns
                        lock1.notifyAll();
                    }
                }
                else if(bj.getState() == GameState.PlayerTurn){
                    //take control of the lock to exeute the dealers turn
                    synchronized (lock1){
                        bj.executeDealerTurn();
                        //after the dealer has gone display the results to the server and set the game state to end so that the clients can see the results as well
                        System.out.println(bj.displayResults());
                        bj.setState(GameState.GameEnd);
                        //let playeers see the results
                        bj.updateTurn();
                        //notify threads to get the lock if it is their turn
                        lock1.notifyAll();
                    }

                }


            }
        }
        if(bj.getState() == GameState.GameEnd){
            //close the server
            serverSocket.close();
        }
    }

}



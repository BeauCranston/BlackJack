import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class BlackJackGame {
    private Player dealer;
    private ArrayList<Integer> playersInGame;
    private int currentPlayer;
    private int count = 0;
//    private Player[] playersWaiting;
    private boolean isInProgress;
    public Deck cardDeck;
    public BlackJackGame(){
        cardDeck = new Deck();
        playersInGame = new ArrayList<>();
    }

    public String dealerResponse(String clientResponse){
        return "";
    }
    public void initializeGame(){

        cardDeck.readFromFile("src/cards.txt");

    }

    public void dealCards(){
        Random rand = new Random();
        int n = rand.nextInt(52);

    }

    public int getCurrentPlayersTurn(){
        return this.currentPlayer;
    }

    public void advanceTurn(){

        if(count == this.playersInGame.size()){
            this.count = 0;
        }
        this.currentPlayer = this.playersInGame.get(count);
        this.count++;
    }
    public void setPlayerTurn(int playerIndex){
        this.currentPlayer = this.playersInGame.get(playerIndex);
    }

    public void addPlayer(int playerPort){
        this.playersInGame.add(playerPort);
    }


}

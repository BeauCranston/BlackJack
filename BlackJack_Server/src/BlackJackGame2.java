import java.util.ArrayList;

enum GameState {
    Initializing,
    GameStarted,
    DealingCards,
    PlayerTurn,
    GameEnd
}

/**
 * class that contains various helper methods to play the game of black jack on a server
 * state of the game visible to all threads as long as the black jack game is instantiated as static
 */
public class BlackJackGame2 {
    private Player dealer;
    private Player[] playersInGame;
    private GameState state;
    public Deck cardDeck;
    private int currentTurn;

    public BlackJackGame2(String fileName, int maxPlayers){
        this.cardDeck = new Deck(fileName);
        this.playersInGame = new Player[maxPlayers + 1];
        this.dealer = new Player(maxPlayers,"dealer", true);
        //initialize player objects and leave the last slot open for the dealer
        for(int i = 0; i < playersInGame.length - 1; i++){
            playersInGame[i] = new Player(i);
        }
        //last playuer is dealer
        this.playersInGame[playersInGame.length - 1] = this.dealer;
        currentTurn = 0;
    }
    //updates whos turn it is, if the number is the final index then go back to the first index
    public void updateTurn(){
        if(currentTurn < playersInGame.length - 1){
            currentTurn++;
        }
        else{
            currentTurn = 0;
        }
    }
    //get the currrent turn
    public int getCurrentTurn(){
        return currentTurn;
    }

    //return dealer as a player object
    public Player getDealer(){
        return this.dealer;
    }

    //sets the global state of the game visible to all threads as long as the black jack game is instantiated as static
    public void setState(GameState state){
        this.state = state;
    }
    //get the state ofg the game
    public GameState getState(){
        return this.state;
    }

    //remove the top card of the deck and return it
    public Card dealCard(){
        return cardDeck.removeFromDeck();
    }

    //get the player by "id"
    public Player getPlayerById(int id){

        return playersInGame[id];
    }

    /**
     * a method that executes the dealers turn and plays with casino rules
     */
    public void executeDealerTurn(){
        //as long as the dealer has not bust keep going
        while(dealer.calculateHandValue()  != -1){
            // if the dealers hand is below 17 the dealer must hit
            if(dealer.calculateHandValue() < 17) {
                dealer.hit(cardDeck.removeFromDeck());
            }
            //otherwise break
            else {
                System.out.println("Dealer stays");
                break;
            }

        }
        System.out.println("Dealers turn ends");

    }

    //retur nthe player array
    public Player[] getPlayersInGame(){
        return this.playersInGame;
    }

    /**
     *  display the results as a string
     *  this includes all of the players hands along with their hand values
     */

    public String displayResults(){
        StringBuilder results = new StringBuilder();
        results.append("\nResults: \n==================== \n");
        for(int i = 0; i < playersInGame.length - 1; i++){
            results.append(playersInGame[i].toString() + "\n");
        }
        Player dealer = playersInGame[playersInGame.length - 1];
        results.append("Dealers Hand: " + dealer.showHand() + " Dealers Value: " + dealer.calculateHandValue());
        results.append("\n" + determineWinner());

        return results.toString();

    }

    /**
     * go through all of the players and determine who has the highest score. If a player busts the score will be -1
     *
     * @return the winner message as a string
     */
    public String determineWinner(){
        String playerName = "";
        int highestScore = 0;
        for(int i = 0; i < playersInGame.length; i++){
            int playerScore = playersInGame[i].calculateHandValue();
            if(highestScore < playerScore){
                highestScore = playerScore;
                playerName = playersInGame[i].getName();
            }

        }
        return playerName + " Wins!!";
    }

}

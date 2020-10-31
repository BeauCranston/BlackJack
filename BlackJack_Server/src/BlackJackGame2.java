import java.util.ArrayList;

enum GameState {
    Initializing,
    GameStarted,
    DealingCards,
    PlayerTurn,
    DealerTurn,
    Results,
    GameEnd
}

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
        for(int i = 0; i < playersInGame.length - 1; i++){
            playersInGame[i] = new Player(i);
        }
        this.playersInGame[playersInGame.length - 1] = this.dealer;
        currentTurn = 0;
    }
    public void updateTurn(){
        if(currentTurn < playersInGame.length - 1){
            currentTurn++;
        }
        else{
            currentTurn = 0;
        }
    }
    public int getCurrentTurn(){
        return currentTurn;
    }

    public Player getDealer(){
        return this.dealer;
    }

    public void initializeDealer(){
        this.dealer.hit(cardDeck.removeFromDeck());
        this.dealer.hit(cardDeck.removeFromDeck());
    }
    public void setState(GameState state){
        this.state = state;
    }

    public GameState getState(){
        return this.state;
    }

//    public void addPlayer(Player player){
//        playersInGame.add(player);
//    }
//
//    public void addPlayer(int id){
//        playersInGame.add(new Player(id,false));
//    }

    public Card dealCard(){
        return cardDeck.removeFromDeck();
    }

    public void initializeHand(int id){
        for(Player player : playersInGame){
            if(player.getId() == id){
                player.hit(cardDeck.removeFromDeck());
                player.hit(cardDeck.removeFromDeck());
            }
        }
    }


    public String getPlayersString(){
        StringBuilder sb = new StringBuilder();
        int count = 1;
        for(Player player : playersInGame){
            sb.append(player.getName() + ", ");
            count++;
        }
        return sb.toString();
    }

    public Player getPlayerByName(String playerName){
        for(Player player : playersInGame){
            if(player.getName() == playerName) {
                return player;
            }
        }
        return null;
    }
    public Player getPlayerById(int id){

        return playersInGame[id];
    }

    public Player[] getPlayersInGame(){
        return this.playersInGame;
    }

}

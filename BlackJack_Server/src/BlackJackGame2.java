import java.util.ArrayList;

enum GameState {
    Initializing,
    DealingCards,
    PlayerTurn,
    DealerTurn,
    Results,
    GameEnd
}

public class BlackJackGame2 {
    private Player dealer;
    private ArrayList<Player> playersInGame;
    private GameState state;
    public Deck cardDeck;

    public BlackJackGame2(String fileName){
        this.cardDeck = new Deck(fileName);
        this.dealer = new Player("Dealer", true);
        this.playersInGame = new ArrayList<>();


    }

    public Player getDealer(){
        return this.dealer;
    }
    public void initializeDealer(){
        this.dealer.hit(cardDeck.removeFromDeck());
        this.dealer.hit(cardDeck.removeFromDeck());
        playersInGame.add(dealer);
    }
    public void setState(GameState state){
        this.state = state;
    }

    public GameState getState(){
        return this.state;
    }

    public void addPlayer(Player player){
        playersInGame.add(player);
    }

    public void addPlayer(int id){
        playersInGame.add(new Player(id,false));
    }

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
        for(Player player : playersInGame){
            if(player.getId() == id) {
                System.out.println("getting player id: " + id);
                return player;
            }
        }
        return null;
    }

    public ArrayList<Player> getPlayersInGame(){
        return this.playersInGame;
    }

}

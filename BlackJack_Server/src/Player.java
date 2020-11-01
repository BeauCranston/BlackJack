import java.util.ArrayList;

/**
 * a class that represents a player in a blackjack game
 */
public class Player {
    private String playerName;
    private int id;
    private ArrayList<Card> hand;
    private boolean isDealer;

    //for initializing dealer
    public Player(int id, String playerName, boolean isDealer){
        this.id = id;
        this.playerName = playerName;
        this.isDealer = isDealer;
        this.hand = new ArrayList<>();
    }
    //if id is passed a name is made up fro the player
    public Player(int id){
        this.id = id;
        this.playerName = "player" + id;
        this.isDealer = false;
        this.hand = new ArrayList<>();
    }

    //get the players id
    public int getId(){return this.id;}

    //get the player name
    public String getName(){
        return this.playerName;
    };

    // add a card to the players hand
    public void hit(Card cardDealt){
        hand.add(cardDealt);
    }

    /**
     * displays the hand as a string
     * @return
     */
    public String showHand(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        //append each card t othe string
        for(Card card : this.hand){
            sb.append(card.getName());

            if(count < this.hand.size() - 1){
                sb.append(", ");
            }
            count ++;
        }
        return sb.toString();
    }

    /**
     * shows only the first card only. This is good for outputting the dealers hand at the begininning of the game
     * @return
     */
    public String showFirstOnly(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        sb.append(this.hand.get(0).getName());
        /**
         * append first card to string and the rest are X's
         */
        for(int i = 1; i < this.hand.size(); i++){
            if(count < this.hand.size() - 1){
                sb.append(", ");
            }
            sb.append('X');
        }
        return sb.toString();
    }

    /**
     * calcualtes the value of a players hand by calling the get value on the cards in the players hand
     * @return return the total value of the hand
     */
    public int calculateHandValue(){
        int handValue = 0;
        for(Card card : this.hand){
            handValue += card.getValue(card.getName());

        }
        /**
         * if the players hand is greater than 21 then it is a bust and also a losing hand so change the value to -1
         */
        if(handValue > 21){
            handValue = -1;
        }
        return handValue;
    }

    //returns the number of cards a player currently has
    public int numOfCards(){
        return hand.size();
    }
    // a method i did not have tiem to implement but this was supposed to return the cards back to the deck if the replay functionality was enabled
    public ArrayList<Card> returnCardsToDeck(){
        ArrayList<Card> userHand = this.hand;
        this.hand.clear();
        return userHand;
    }

    @Override
    public String toString(){
        if(isDealer == false){
            return playerName + ": " + " Hand: " + showHand() + " Hand Value: " + calculateHandValue();
        }
        else{
            return playerName + ": " + " Hand: " + showFirstOnly();
        }

    }

}


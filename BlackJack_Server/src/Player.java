import java.util.ArrayList;

public class Player {
    private String playerName;
    private ArrayList<Card> hand;


    public Player(String playerName){
        this.playerName = playerName;
    }
    public Player(String playerName, ArrayList<Card> hand){
        this.playerName = playerName;
        this.hand = hand;
    }

    public void hit(Card cardDealt){
        hand.add(cardDealt);
    }

    public String showHand(){
        String playerHandString = "";
        int count = 0;
        for(Card card : this.hand){
            playerHandString.concat(card.getName());

            if(count < this.hand.size() - 1){
                playerHandString.concat(", ");
            }
            count ++;
        }
        return playerHandString;
    }
    public int calculateHandValue(){
        int handValue = 0;
        for(Card card : this.hand){
            handValue += card.getValue(card.getName());

        }
        return handValue;
    }


    @Override
    public String toString(){
        return playerName + ": " + " hand: " + showHand() + " Hand Value: " + calculateHandValue();
    }


}

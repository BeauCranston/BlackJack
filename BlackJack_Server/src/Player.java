import java.util.ArrayList;

public class Player {
    private String playerName;
    private ArrayList<Card> hand;
    private boolean isDealer;

    public Player(String playerName, boolean isDealer){
        this.playerName = playerName;
        this.isDealer = isDealer;
    }
    public Player(String playerName, ArrayList<Card> hand, boolean isDealer){
        this.playerName = playerName;
        this.hand = hand;
        this.isDealer = isDealer;
    }

    public void hit(Card cardDealt){
        hand.add(cardDealt);
    }

    public String showHand(){
        StringBuilder sb = new StringBuilder();
        sb.append(playerName + "'s Hand: ");
        int count = 0;
        if(this.isDealer != true){
            for(Card card : this.hand){
                sb.append(card.getName());

                if(count < this.hand.size() - 1){
                    sb.append(", ");
                }
                count ++;
            }
        }
        else{
            sb.append(this.hand.get(0));
            for(int i = 1; i < this.hand.size(); i++){
                sb.append('x');

                if(count < this.hand.size() - 1){
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
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

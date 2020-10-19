import java.util.ArrayList;

public class Player {
    private String playerName;
    private ArrayList<Card> hand;
    private boolean isDealer;

    public Player(String playerName, boolean isDealer){
        this.playerName = playerName;
        this.isDealer = isDealer;
        this.hand = new ArrayList<>();
    }
    public Player(String playerName, ArrayList<Card> hand, boolean isDealer){
        this.playerName = playerName;
        this.hand = hand;
        this.isDealer = isDealer;
    }

    public String getName(){
        return playerName;
    };


    public void hit(Card cardDealt){
        hand.add(cardDealt);
    }

    public String showHand(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(Card card : this.hand){
            sb.append(card.getName());

            if(count < this.hand.size() - 1){
                sb.append(", ");
            }
            count ++;
        }
        return sb.toString();
    }
    public String showFirstOnly(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        sb.append(this.hand.get(0).getName());
        for(int i = 1; i < this.hand.size(); i++){
            if(count < this.hand.size() - 1){
                sb.append(", ");
            }
            sb.append('X');
        }
        return sb.toString();
    }
    public int calculateHandValue(){
        int handValue = 0;
        for(Card card : this.hand){
            handValue += card.getValue(card.getName());

        }
        if(handValue > 21){
            handValue = -1;
        }
        return handValue;
    }

    public void setHand(Card[] cards){
        for(Card card : cards){
            this.hand.add(card);
        }

    }

    public boolean isDealer(){
        return this.isDealer;
    }

    public ArrayList<Card> returnCards(){
        ArrayList<Card> userHand = this.hand;
        this.hand.clear();
        return userHand;
    }

    @Override
    public String toString(){
        return playerName + ": " + " Hand: " + showHand() + " Hand Value: " + calculateHandValue();
    }


}


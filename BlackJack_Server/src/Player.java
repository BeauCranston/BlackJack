import java.util.ArrayList;

public class Player {
    private String playerName;
    private int id;
    private ArrayList<Card> hand;
    private boolean isDealer;

    public Player(int id, String playerName, boolean isDealer){
        this.id = id;
        this.playerName = playerName;
        this.isDealer = isDealer;
        this.hand = new ArrayList<>();
    }
    public Player(int id){
        this.id = id;
        this.playerName = "player" + id;
        this.isDealer = false;
        this.hand = new ArrayList<>();
    }

    public int getId(){return this.id;}

    public String getName(){
        return this.playerName;
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

    public int numOfCards(){
        return hand.size();
    }

    public boolean isDealer(){
        return this.isDealer;
    }

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


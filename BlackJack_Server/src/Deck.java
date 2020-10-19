import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

public class Deck {
    private Stack<Card> deck;

    public Deck(String fileName){
        this.deck = readFromFile(fileName);

    }

    public Stack<Card> readFromFile(String fileName){
        Stack<Card> cards = new Stack<>();
        String row = "";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                for (int i =0; i < data.length; i ++) {
                    cards.push(new Card(data[i]));

                }
            }
            csvReader.close();
            System.out.println(cards.toString());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return cards;
    }

    public void shuffle(){
        Collections.shuffle(this.deck);
    }

    public Card removeFromDeck(){
        return deck.pop();
    }
    public void returnHandToDeck(ArrayList<Card> cards){
        for(Card card : cards){
            deck.push(card);
        }

    }
    public void returnHandToDeck(Card card){
        deck.add(0,card);
    }

}


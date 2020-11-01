import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * a class that represents a deck of cards
 */
public class Deck {
    //deck is a stack of cards
    private Stack<Card> deck;

    //initialize the deck by reading from a csv file
    public Deck(String fileName){
        this.deck = readFromFile(fileName);

    }

    /**
     * a method that reads a csv file and creates a deck of cards in a very specific format
     * @param fileName
     * @return the deck of cards
     */
    public Stack<Card> readFromFile(String fileName){
        Stack<Card> cards = new Stack<>();
        String row = "";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                for (int i =0; i < data.length; i ++) {
                    //for each row create a new card and push it to the deck
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

    /**
     *calls collections.shuffle on the deck of cards to randomize the deck
     */

    public void shuffle(){
        Collections.shuffle(this.deck);
    }

    //pops the top card off of the stack. "deals" the card
    public Card removeFromDeck(){
        return deck.pop();
    }



}


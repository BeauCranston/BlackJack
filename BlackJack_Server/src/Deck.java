import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deck;

    public Deck(){
        this.deck = new ArrayList<>();


    }

    public void readFromFile(){

        String row = "";
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("cards.txt"));

            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                for (int i =0; i < data.length; i ++) {
                    deck.add(new Card(data[i]));
                }
            }
            csvReader.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

    }


}

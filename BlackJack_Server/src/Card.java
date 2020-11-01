import java.lang.reflect.Type;

/**
 * a class that represents a playing card for blackjack. it has a name and a value associated with it
 */
public class Card {
    private int value;
    private String name;
    public Card(String name){
        this.name = name;
        this.value = getValue(name);
    }

    /**
     * get the value of a card but passign the name as a param
     * @param name
     * @return the value of the card
     */
    public int getValue(String name){
        if(isFace(name)){
            //if the card starts with a letter than it is a face card worth 10, unless if its A then it is only worth 1
            if(name.charAt(0) == 'A'){
                return 1;
            }
            else {
                return 10;
            }
        }
        else{
            //if the name contains a 10 it is worth 10, otherwise extract the first character which will be a number between 2-9
            if(name.contains("10")){
                return 10;
            }
            else{
                return Integer.parseInt(String.valueOf(name.charAt(0)));
            }
        }
    }

    /**
     * check if the card is a face card by checking if a number format exception is thrown when trying to parse the first charater to an int
     */
    public boolean isFace(String name){
        try{
            Integer.parseInt(String.valueOf(name.charAt(0)));
            return false;
        }
        catch(NumberFormatException e){
            return true;
        }
    }

    /**
     * gets the name of the card
     * @return
     */
    public String getName(){
        return this.name;
    }
    @Override
    public String toString(){
        return getName() + " value: " + getValue(getName());
    }


}


import java.lang.reflect.Type;

public class Card {
    private int value;
    private String name;
    public Card(String name){
        this.name = name;
        this.value = getValue(name);
    }

    public int getValue(String name){
        if(isFace(name)){
            if(name.charAt(0) == 'A'){
                return 1;
            }
            else {
                return 10;
            }
        }
        else{

            if(name.contains("10")){
                return 10;
            }
            else{
                return Integer.parseInt(String.valueOf(name.charAt(0)));
            }


        }
    }

    public boolean isFace(String name){
        try{
            Integer.parseInt(String.valueOf(name.charAt(0)));
            return false;
        }
        catch(NumberFormatException e){
            return true;
        }
    }
    public String getName(){
        return this.name;
    }
    @Override
    public String toString(){
        return getName() + " value: " + getValue(getName());
    }


}


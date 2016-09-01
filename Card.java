public class Card{
    private Suit suit;
    private Value value;
    //constructor method
    public Card(Suit theSuit, Value theValue){
        suit = theSuit;
        value = theValue;
    }
    //simple return methods; not much to say
    public Suit getSuit(){
        return suit;
    }
    
    public Value getValue(){
        return value;
    }
    //simple string output using private variables
    public String toString(){
        String returnString = getValue() + " of " + getSuit();
        return returnString;
    }
}
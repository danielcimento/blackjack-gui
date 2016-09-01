import java.util.ArrayList;
import java.util.*;
public class CardPile{
    private ArrayList<Card> cards;
    public CardPile(){
        cards = new ArrayList<Card>();
    }
    public void addToBottom(Card c){
        cards.add(c);
    }
    public boolean isEmpty(){
        return cards.isEmpty();
    }
    public Card get(int i){
        return cards.get(i);
    }
    public Card remove(int i){
        return cards.remove(i);
    }
    public int find(Suit s, Value v){
        //I would use indexOf, but I didn't want to create another method in card using instanceof, as we hadn't learned that as I write this
        int counter = 0;
        for(Card c : cards){
            if(c.getSuit() == s && c.getValue() == v){
                return counter;
            }
            counter++;
        }
        return -1;
    }
    public String toString(){
        String returnString = "";
        int counter = 1;
        for(Card c : cards){
            returnString += (counter + ". " + c.toString() + " ");
            counter++;
        }
        return returnString;
    }
    public static CardPile makeFullDeck(){
        CardPile returnCardPile = new CardPile();
        for(Suit s: Suit.values()){
            for(Value v: Value.values()){
                returnCardPile.addToBottom(new Card(s, v));
            }
        }
        Collections.shuffle(returnCardPile.cards);
        return returnCardPile;
    }
    public int getNumCards(){
        return cards.size();
    }
    public static CardPile makeFullDeck(int n){
        CardPile returnCardPile = new CardPile();
        for(int i = 0; i < n; i++){
              returnCardPile.cards.addAll((CardPile.makeFullDeck()).cards);
        }
        Collections.shuffle(returnCardPile.cards);
        return returnCardPile;
    }
}

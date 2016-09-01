
public class CardGame{
    public static void main(String[] args){
        int numberOfPlayers = Integer.parseInt(args[0]);
        CardPile[] cardPiles = new CardPile[numberOfPlayers];
        //creates n card piles for n players
        for(int i = 0; i < numberOfPlayers; i++){
            cardPiles[i] = new CardPile();
        }
        //creates the first "dealers" deck
        CardPile dealersDeck = CardPile.makeFullDeck();
        //for each card in the dealers deck
        int j = 0;
        while(!dealersDeck.isEmpty()){
            //deals the top card of the dealers deck to each player
            cardPiles[(j%numberOfPlayers)].addToBottom(dealersDeck.remove(0));
            j++;
        }
        for(int i = 0; i < numberOfPlayers; i++){
           // System.out.println(cardPiles[i].toString());
            //System.out.println(cardPiles[i].find(Suit.SPADES, Value.ACE));
            //looks in each players card pile for the ace of spades and sees if it does exist in a positive array index
            if(cardPiles[i].find(Suit.SPADES, Value.ACE) >= 0){
                System.out.println("Player " + (i + 1) + " Wins!");
            }
        }
    }
}
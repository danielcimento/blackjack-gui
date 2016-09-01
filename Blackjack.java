import java.util.*;
public class Blackjack{
   public enum Results{
       DEALER_WINS, PLAYER_WINS, TIE, BLACKJACK
   }
   public static void main(String[] args){
       Scanner input = new Scanner(System.in);
       int chips = Integer.parseInt(args[0]);
       int bet = 0;
       boolean betYet = false;
       System.out.println("Welcome to Java Blackjack!");
       CardPile houseDeck = CardPile.makeFullDeck(4);
       while(houseDeck.getNumCards() > 10 && chips != 0){
           System.out.println("New Round! You have " + chips + " chips!");
           System.out.print("How many chips would you like to bet? ");  
           //loops until the player inputs a valid input
           while(!betYet){
               bet = input.nextInt();
               if(bet > chips){
                   System.out.println("Error: Cannot bet more than your current chip value!");
                }else if(bet < 0){
                    System.out.println("Thanks for Playing!");
                    //using return as break from method
                    return;
                }else{
                    betYet = true;
                }
           }
           Results outcome = playRound(houseDeck);
           if(outcome == Results.DEALER_WINS){
               chips -= bet;
            }else if(outcome == Results.PLAYER_WINS){
                chips += bet;
            }else if(outcome == Results.BLACKJACK){
                chips += (int)(bet * 1.5);
            }
            betYet = false;
       }
       System.out.println("Thanks for playing!");
   }
   public static int getScore(Card c){
       if(c.getValue() == Value.ACE){
           return 11;
        }else if(c.getValue().ordinal() > Value.TEN.ordinal()){
            /*even though ACE technically falls in this range, Aces are 
            already handled in the previous if statement and won't be checked*/
            return 10;
        }else{
            return (c.getValue().ordinal() + 2);
        }
    }
   public static int countValues(CardPile hand){
        int returnValue = 0;
        int numAces = 0;
        for(int i = 0; i < hand.getNumCards(); i++){
            returnValue += getScore(hand.get(i));
            /* 
             * because the hand could have multiple Aces of a single suit
             * I couldn't use for each loops to count the number of aces
             * so I had to add a small if statement to track the aces
             */
            if(hand.get(i).getValue() == Value.ACE){
                numAces++;
            }
        }
        //until the aces run out or the hand is less than or equal to 21, it'll turn 11s into 1s
        while(numAces > 0 && returnValue > 21){
            numAces--;
            returnValue -= 10;
        }
        return returnValue;
   }
   public static Results playRound(CardPile houseDeck){
       Scanner input = new Scanner(System.in);
       String userResponse = "";
       CardPile dealersHand = new CardPile();
       CardPile playersHand = new CardPile();
       //deals both the player and the dealer their hands
       while(playersHand.getNumCards() < 2){
           playersHand.addToBottom(houseDeck.remove(0));
       }
       while(dealersHand.getNumCards() < 2){
           dealersHand.addToBottom(houseDeck.remove(0));
       }
       if(countValues(playersHand) == 21 && countValues(dealersHand) != 21){
           System.out.println("Blackjack!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());
           return Results.BLACKJACK;
       }else if(countValues(playersHand) == 21 && countValues(dealersHand) == 21){
           System.out.println("It's a tie!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());
           return Results.TIE;
       }else{
            //player can keep hitting until busts or types stay
            while(!userResponse.equals("stay") && countValues(playersHand) < 21){
                //note, I could add <= to the above line instead of <, but I assume players will never voluntarily hit if they have 21
                System.out.println("Your hand: " + playersHand.toString());
                System.out.println("Dealer showing: " + dealersHand.get(0).toString());
                System.out.print("\'hit\' or \'stay\'? ");
                userResponse = input.next();
                if(userResponse.equals("hit")){
                    playersHand.addToBottom(houseDeck.remove(0));
                }
                if(countValues(playersHand) > 21){
                    System.out.println("Bust!");
                    System.out.println("Your hand: " + playersHand.toString());
                    System.out.println("Dealers hand: " + dealersHand.toString());
                    return Results.DEALER_WINS;
                }
            }
       }
       //dealer must continue to hit until 18 or higher
       while(countValues(dealersHand) < 18){
           dealersHand.addToBottom(houseDeck.remove(0));
       }
       //at this point, the player can't have busted, so only the dealer busting matters
       if(countValues(dealersHand) > 21){
           System.out.println("You win! Dealer busted!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());
           return Results.PLAYER_WINS;
        }else if(countValues(dealersHand) > countValues(playersHand)){
           System.out.println("You lose!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());     
           return Results.DEALER_WINS;
        }else if(countValues(dealersHand) == countValues(playersHand)){
           System.out.println("It's a tie!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());
           return Results.TIE; 
        }else{
           //if player hasn't busted and dealer hasnt won, player winning is only remaining option
           System.out.println("You win!");
           System.out.println("Your hand: " + playersHand.toString());
           System.out.println("Dealers hand: " + dealersHand.toString());
           return Results.PLAYER_WINS;
        }
    }
}


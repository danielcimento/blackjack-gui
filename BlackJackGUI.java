import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.concurrent.*;
/**
 * Created by Daniel on 2015-12-01.
 */
public class BlackJackGUI extends Application{
    public enum Result{
        PLAYER_WINS, DEALER_WINS, TIE, BLACKJACK
    }
    int chipCount = 0;
    Stage window;
    Scene startScene, betScene, playScene;
    Button hit, stay, bet;
    TextField initialChips;
    TextField betAmountField;
    int betAmount;
    TextArea roundDisplay;
    Label chipsRemaining;
    CardPile dealersDeck = CardPile.makeFullDeck(4);
    CardPile dealersHand = new CardPile();
    CardPile playersHand = new CardPile();
    boolean value = false;
    Result thisRound;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
	window.setMinWidth(500);

        Label label1 = new Label("Welcome to Java Blackjack! Enter initial chips:");
        Button begin = new Button("Start");
        begin.setOnAction(e -> setChipCount());
        hit = new Button();
        hit.setText("Hit");
        stay = new Button();
        stay.setText("Stay");
        bet = new Button();
        bet.setText("Bet");
        bet.setOnAction(e -> setBetAmount());
        initialChips = new TextField();
        betAmountField = new TextField();
        initialChips.setMaxWidth(50);
        betAmountField.setMaxWidth(50);
        roundDisplay = new TextArea("");
        roundDisplay.setEditable(false);
        chipsRemaining = new Label();


        //chip scene
        VBox layout1 = new VBox(20);
        HBox buttonChipLayout = new HBox(10);
        buttonChipLayout.getChildren().addAll(initialChips, begin);
        layout1.getChildren().addAll(label1, buttonChipLayout);
        layout1.setAlignment(Pos.CENTER);
        buttonChipLayout.setAlignment(Pos.CENTER);
        startScene = new Scene(layout1, 300, 100);

        //bet scene
        VBox layout2 = new VBox(20);
        HBox buttonBetLayout = new HBox(10);
        buttonBetLayout.getChildren().addAll(betAmountField, bet);
        layout2.getChildren().addAll(chipsRemaining, buttonBetLayout);
        buttonBetLayout.setAlignment(Pos.CENTER);
        layout2.setAlignment(Pos.CENTER);
        betScene = new Scene(layout2, 300, 100);

        //play window
        HBox buttonLayout = new HBox(20);
        VBox playLayout = new VBox(50);
        buttonLayout.setAlignment(Pos.CENTER);
        roundDisplay.setWrapText(true);
        buttonLayout.getChildren().addAll(hit, stay);
        playLayout.getChildren().addAll(roundDisplay, buttonLayout);
        playScene = new Scene(playLayout, 300, 300);
        hit.setOnAction(e -> hit(playersHand, dealersDeck));
        stay.setOnAction(e -> finishRound());

        window.setScene(startScene);
        window.setTitle("BlackJack");
        window.show();

    }

    public void setChipCount(){
        boolean legitimateValue = false;
        while (!legitimateValue) {
            try {
                
		if(Integer.parseInt(initialChips.getText()) > 0){
			chipCount = Integer.parseInt(initialChips.getText());
                	legitimateValue = true;
		}else{
			AlertBox.display("Error", "Please input a positive number of chips!");
			return;
		}
            }catch(NumberFormatException e){
                AlertBox.display("Error", "Please insert a number");
                return;
            }
        }
        chipsRemaining.setText("You have " + chipCount + " chips remaining. \n How many would you like to bet?");
        window.setScene(betScene);
    }

    public void setChipCount(int i){
        chipCount = i;
        chipsRemaining.setText("You have " + chipCount + " chips remaining. \n How many would you like to bet?");
        window.setScene(betScene);
    }

    public void setBetAmount(){
        try {
            if(Integer.parseInt(betAmountField.getText()) > chipCount){
                AlertBox.display("Error", "Cannot bet more chips than you have");
                return;
            }else if(Integer.parseInt(betAmountField.getText()) < 0){
                AlertBox.display("Error", "Cannot bet a negative amount of chips!");
                return;
            }else{
                betAmount = Integer.parseInt(betAmountField.getText());
                window.setScene(playScene);
                playRound();
            }
        } catch (NumberFormatException e) {
            AlertBox.display("Error", "Please insert a number");
            return;
        }
    }

    public void playRound(){
        dealInitialHand();
        checkBlackJack();
        if(thisRound == null) {
            roundDisplay.appendText("Your hand: " + playersHand.toString() + "\n");
            roundDisplay.appendText("Dealer showing: " + dealersHand.get(0).toString() + "\n");
        }
        }




    public void hit(CardPile hand, CardPile deck){
        hand.addToBottom(deck.remove(0));
        roundDisplay.appendText("Your hand: " + playersHand.toString() + "\n");
        roundDisplay.appendText("Dealer showing: " + dealersHand.get(0).toString() + "\n");
        if(Blackjack.countValues(playersHand) > 21){
            AlertBox.display("Result", "You busted!\nYour hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString());
            chipCount -= (Integer.parseInt(betAmountField.getText()));
            thisRound = null;
            dealersHand = new CardPile();
            playersHand = new CardPile();
            betAmountField.clear();
            chipsRemaining.setText("You have " + chipCount + " chips remaining. \n How many would you like to bet?");
            window.setScene(betScene);
            roundDisplay.clear();
        }
    }

    public void dealInitialHand(){
    while(dealersHand.getNumCards() < 2){
        dealersHand.addToBottom(dealersDeck.remove(0));
    }
    while(playersHand.getNumCards() < 2){
        playersHand.addToBottom(dealersDeck.remove(0));
    }

    }
    public void checkBlackJack(){
        if(Blackjack.countValues(playersHand) == 21 && !(Blackjack.countValues(dealersHand) == 21)){
            AlertBox.display("Result", ("Blackjack!\n" + "Your hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString()));
            thisRound = Result.BLACKJACK;
            return;
        }
        if(Blackjack.countValues(playersHand) == 21 && Blackjack.countValues(dealersHand) == 21) {
            AlertBox.display("Result", ("It's a tie!\n" + "Your hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString()));
            thisRound = Result.TIE;
            return;
        }
    }
    public void finishRound(){
            while (Blackjack.countValues(dealersHand) < 18) {
                dealersHand.addToBottom(dealersDeck.remove(0));
            }
            if (Blackjack.countValues(dealersHand) > 21) {
                AlertBox.display("Result", "Dealer busted!\nYour hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString());
                thisRound = Result.PLAYER_WINS;
            } else if (Blackjack.countValues(dealersHand) > Blackjack.countValues(playersHand)) {
                AlertBox.display("Result", "You lost!\nYour hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString());
                thisRound = Result.DEALER_WINS;
            } else if (Blackjack.countValues(dealersHand) == Blackjack.countValues(playersHand)) {
                AlertBox.display("Result", ("It's a tie!\n" + "Your hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString()));
                thisRound = Result.TIE;
            } else {
                AlertBox.display("Result", ("You won!\n" + "Your hand: " + playersHand.toString() + "\n" + "Dealers Hand: " + dealersHand.toString()));
                thisRound = Result.PLAYER_WINS;
            }


        if(thisRound == Result.BLACKJACK){
            chipCount += (int)(Integer.parseInt(betAmountField.getText()) * 1.5);
        }else if(thisRound == Result.DEALER_WINS){
            chipCount -= (Integer.parseInt(betAmountField.getText()));
        }else if(thisRound == Result.PLAYER_WINS) {
            chipCount += (Integer.parseInt(betAmountField.getText()));
        }
        thisRound = null;
        value = false;
        dealersHand = new CardPile();
        playersHand = new CardPile();
        betAmountField.clear();
        chipsRemaining.setText("You have " + chipCount + " chips remaining. \n How many would you like to bet?");
        window.setScene(betScene);
        roundDisplay.clear();
    }
}

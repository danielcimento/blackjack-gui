import javafx.scene.control.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import javafx.scene.control.Alert.*;

public class AlertBox{
	public static void display(String title, String message){
		Stage window = new Stage();

		String[] messages = message.split("\n");

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(250);

		
		Button closeButton = new Button("OK");
		closeButton.setOnAction(e -> window.close());

		VBox layout = new VBox(10);
		layout.setPadding(new Insets(50, 20, 50, 20));

		for(String s : messages){
			Label label = new Label();
			label.setText(s);
			layout.getChildren().add(label);
		}

		layout.getChildren().add(closeButton);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}

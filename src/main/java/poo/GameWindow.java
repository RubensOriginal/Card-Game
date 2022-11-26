package poo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GameWindow extends Application implements GameListener {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		Game.getInstance().addGameListener(this);

		primaryStage.setTitle("Batalha de Cartas");

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));

		DeckView deckJ1 = new DeckView(1);
		ScrollPane sd1 = new ScrollPane();
		// sd1.setPrefSize(1024, 256);
		sd1.setContent(deckJ1);
		grid.add(sd1, 0, 0);

		FieldView placar = new FieldView(Game.getInstance().getFieldJ1(), 1); // Field Player Up
		grid.add(placar, 0, 1);

		FieldView placar2 = new FieldView(Game.getInstance().getFieldJ2(), 2); // Field Player Down
		grid.add(placar2, 0, 2);

//		Button butClean = new Button("Clean");
//		grid.add(butClean, 1, 1);
//		butClean.setOnAction(e -> Game.getInstance().removeSelected());

		DeckView deckJ2 = new DeckView(2);
		ScrollPane sd2 = new ScrollPane();
		// sd2.setPrefSize(1024, 256);
		sd2.setContent(deckJ2);
		grid.add(sd2, 0, 3);

		Scene scene = new Scene(grid);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	@Override
	public void notify(GameEvent eg) {
		Alert alert;
		if (eg == null) return;
		if (eg.getTarget() == GameEvent.Target.GWIN) {
			switch (eg.getAction()) {
			case INVPLAY:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText("Jogada inválida!!");
				alert.setContentText("Era a vez do jogador " + eg.getArg());
				alert.showAndWait();
				break;
			case MUSTCLEAN:
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Atenção !!");
				alert.setHeaderText(null);
				alert.setContentText("Utilize o botao \"Clean\"");
				alert.showAndWait();
				break;
			case ENDGAME:
				String text = "Fim de Jogo !!\n";
				if (Game.getInstance().getPtsJ1() > Game.getInstance().getPtsJ2()) {
					text += "O jogador 1 ganhou !! :-)";
				} else {
					text += "O jogador 2 ganhou !! :-)";
				}
				alert = new Alert(AlertType.WARNING);
				alert.setTitle("Parabens !!");
				alert.setHeaderText(null);
				alert.setContentText(text);
				alert.showAndWait();
				break;
			case REMOVESEL:
				// Esse evento não vem para cá
			}
		}
	}

}

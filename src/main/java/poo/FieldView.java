package poo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class FieldView extends GridPane implements GameListener {
	// private TextField ptsJ1, ptsJ2;

	private Field field;

	private Card monsterNullCard;
	private Card specialNullCard;
	private Card graveyardNullCard;

	private List<CardView> monsterCardView;
	private List<CardView> specialCardView;

	private Card topGraveyard;
	private CardView topGraveyardView;

	public FieldView(Field field) {
		this.field = field;

		monsterNullCard = new MonsterCard("null_card", "/cards/back.jpg", 0, 0, 0, 0);
		specialNullCard = new MagicCard("special_null_card", "/cards/back.jpg", 0);
		graveyardNullCard = new MonsterCard("graveyard_null_card", "/cards/back.jpg", 0, 0, 0, 0);

//		for (int i = 0; i < 5; i++) {
//			monsterCards.add(monsterNullCard);
//			specialCards.add(specialNullCard);
//		}

		topGraveyard = graveyardNullCard;

		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(25, 25, 25, 25));

		for (int i = 0; i < 5; i++) {
			CardView card = new CardView(monsterNullCard);
			// monsterCardView.add(card);
			this.add(card, i, 0);

			card = new CardView(specialNullCard);
			// specialCardView.add(card);
			this.add(card, i, 1);
		}

		topGraveyard = new MonsterCard("null_card", "/cards/back.jpg", 0, 0, 0, 0);
		topGraveyardView = new CardView(topGraveyard);
		this.add(topGraveyardView, 5,0);


		CardView cardStackView = new CardView(new SpecialCard("stack_card", "/cards/back.jpg", 0));
		this.add(cardStackView, 5,1);




		Game.getInstance().addGameListener(this);
//
//		ptsJ1 = new TextField();
//		ptsJ2 = new TextField();
//
//		ptsJ1.setText("" + Game.getInstance().getPtsJ1());
//		ptsJ2.setText("" + Game.getInstance().getPtsJ2());
//
//		this.add(new Label("Jogador 1:"), 0, 0);
//		this.add(ptsJ1, 1, 0);
//		this.add(new Label("Jogador 2:"), 0, 1);
//		this.add(ptsJ2, 1, 1);
	}

	@Override
	public void notify(GameEvent event) {

		for (int i = 0; i < 5; i++) {
			CardView card = new CardView(field.getMonsterCards().get(i));
			// monsterCardView.add(card);

			getChildren().remove(i,0);

			this.add(card, i, 0);

			card = new CardView(field.getMonsterCards().get(i));
			// specialCardView.add(card);

			getChildren().remove(i,1);

			this.add(card, i, 1);
		}
//		ptsJ1.setText("" + Game.getInstance().getPtsJ1());
//		ptsJ2.setText("" + Game.getInstance().getPtsJ2());
	}
}

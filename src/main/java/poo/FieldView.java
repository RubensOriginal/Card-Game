package poo;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import poo.CardView.CardType;
import poo.exceptions.DeckSizeException;

import java.util.ArrayList;
import java.util.List;

public class FieldView extends GridPane implements CardViewListener, GameListener {

	private Field field;
	private int player;

	private Card monsterNullCard;
	private Card specialNullCard;
	private Card graveyardNullCard;

	private List<CardView> monsterCardsView;
	private List<CardView> specialCardsView;

	private Card topGraveyard;
	private CardView topGraveyardView;

	public FieldView(Field field, int player) {
		this.field = field;
		this.player = player;

		monsterNullCard = new MonsterCard("null_card", "/cards/monster_card_zone.jpg", 0, 0,0);
		specialNullCard = new MagicCard("special_null_card", "/cards/speel_and_trap_card_zone.jpg", null);
		graveyardNullCard = new MonsterCard("graveyard_null_card", "/cards/graveyard.jpg", 0, 0, 0);

		monsterCardsView = new ArrayList<>(5);
		specialCardsView = new ArrayList<>(5);

		topGraveyard = graveyardNullCard;

		this.setAlignment(Pos.CENTER);
		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(15, 15, 15, 15));

		for (int i = 0; i < 5; i++) {
			CardView card = new CardView(monsterNullCard, CardType.NULLCARD);
			monsterCardsView.add(card);
			this.add(card, i, 0);

			card = new CardView(specialNullCard, CardType.NULLCARD);
			specialCardsView.add(card);
			this.add(card, i, 1);
		}

		topGraveyardView = new CardView(topGraveyard, CardType.NULLCARD);
		this.add(topGraveyardView, 5,0);
		topGraveyardView.setCardViewObserver(this);


		CardView cardStackView = new CardView(new SpecialCard("stack_card", "/cards/back.jpg"), CardType.STACKCARD);
		this.add(cardStackView, 5,1);
		cardStackView.setCardViewObserver(this);

		Button button = new Button("Próxima Fase");

		this.add(button, 6,1);

		button.setOnAction(e -> {
			if (player == Game.getInstance().getPlayer())
				Game.getInstance().nextStage();
		});

		Game.getInstance().addGameListener(this);
		field.addGameListener(this);

	}

	@Override
	public void notify(GameEvent event) {

		Field field;

		List<CardView> newMonsterCardsView = new ArrayList<>(5);
		List<CardView> newSpecialCardsView = new ArrayList<>(5);

		if (player == 1) {
			field = Game.getInstance().getFieldJ1();
		} else {
			field = Game.getInstance().getFieldJ2();
		}

		for (int i = 0; i < 5; i++) {

			CardView card;

			if (field.getMonsterCards().size() > i) {
				card = new CardView(field.getMonsterCards().get(i), CardType.FIELDCARD);
			} else {
				card = new CardView(monsterNullCard, CardType.NULLCARD);
			}

			getChildren().remove(monsterCardsView.get(i));

			this.add(card, i, 0);
			newMonsterCardsView.add(card);
			card.setCardViewObserver(this);

			if (field.getSpecialCards().size() > i) {
				card = new CardView(field.getSpecialCards().get(i), CardType.FIELDCARD);
			} else {
				card = new CardView(specialNullCard, CardType.NULLCARD);
			}

			getChildren().remove(specialCardsView.get(i));
 
			this.add(card, i, 1);
			newSpecialCardsView.add(card);
			card.setCardViewObserver(this);
		}

		monsterCardsView = newMonsterCardsView;
		specialCardsView = newSpecialCardsView;

	}

	@Override
	public void handle(CardViewEvent event) {
		CardView cv = event.getCardView();
		Card selectedCard = cv.getCard();

		Game.getInstance().playField(field, cv, player);

	}
}

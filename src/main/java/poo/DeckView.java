package poo;

import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import poo.CardView.CardType;

import java.util.ArrayList;
import java.util.List;

public class DeckView extends HBox implements CardViewListener, GameListener {
	private int jogador;
	private CardDeck cDeck;
	private Card selectedCard;

	private List<CardView> cardViewList;

	public DeckView(int nroJog) {
		super(4);
		this.setAlignment(Pos.CENTER);

		cardViewList = new ArrayList<>(6);

		jogador = nroJog;
		selectedCard = null;

		cDeck = null;
		if (jogador == 1) {
			cDeck = Game.getInstance().getDeckJ1();
		} else {
			cDeck = Game.getInstance().getDeckJ2();
		}
		cDeck.addGameListener(this);

		for (Card card : cDeck.getCards()) {
			CardView cv = new CardView(card, CardType.DECKCARD);
			cv.setCardViewObserver(this);
			this.getChildren().add(cv);
			cardViewList.add(cv);
		}
	}

	private void removeSel() {
		ObservableList<Node> cards = getChildren();
		for (int i = 0; i < cards.size(); i++) {
			CardView cv = (CardView) cards.get(i);
			if (cv.getCard() == selectedCard) {
				getChildren().remove(cv);
				selectedCard = null;
				cardViewList.remove(cv);
			}
		}
	}

	@Override
	public void notify(GameEvent event) {
		if (event.getTarget() != GameEvent.Target.DECK) {
			return;
		}

		if (event.getAction() == GameEvent.Action.UPDATEDECK) {
			ObservableList<Node> cards = getChildren();
			for (int i = 0; i < cardViewList.size(); i++) {
				getChildren().remove(cardViewList.get(i));
			}

			cardViewList.clear();

			if (jogador == 1) {
				cDeck = Game.getInstance().getDeckJ1();
			} else {
				cDeck = Game.getInstance().getDeckJ2();
			}

			for (Card card : cDeck.getCards()) {
				CardView cv = new CardView(card, CardType.DECKCARD);
				cv.setCardViewObserver(this);
				this.getChildren().add(cv);
				cardViewList.add(cv);
			}
		}

		if (event.getAction() == GameEvent.Action.REMOVESEL) {
			removeSel();
		}
	}

	@Override
	public void handle(CardViewEvent event) {
		CardView cv = event.getCardView();
		selectedCard = cv.getCard();
		cDeck.setSelectedCard(selectedCard);
		Game.getInstance().play(cDeck);
	}
}

package poo;

import poo.exceptions.DeckSizeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CardDeck {
	private List<Card> cartas;

	private CardStack stack;
	private int player;

	private Card selected;
	private List<GameListener> observers;

	public CardDeck(int player) {
		cartas = new ArrayList<>(6);

		this.player = player;

		stack = new CardStack();
		stack.buildStackCard();

		selected = null;

		for (int i = 0; i < 5; i++)
			cartas.add(stack.pop());

		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public void removeSel() {
		if (selected == null) {
			return;
		}
		cartas.remove(selected);
		selected = null;
		GameEvent gameEvent = new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.REMOVESEL, "");
		for (var observer : observers) {
			observer.notify(gameEvent);
		}
	}

	public void setSelectedCard(Card card) {
		selected = card;
	}

	public Card getSelectedCard() {
		return selected;
	}

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public void addCardToDeck() throws DeckSizeException {
		if (cartas.size() == 6)
			throw new DeckSizeException();
		cartas.add(stack.pop());

		for (var observer : observers) {
			observer.notify(new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.UPDATEDECK, ""));
		}
	}

}

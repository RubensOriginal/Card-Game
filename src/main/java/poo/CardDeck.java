package poo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CardDeck {
	public static final int NCARDS = 6;
	private List<Card> cartas;
	private Card selected;
	private List<GameListener> observers;

	public CardDeck() {
		cartas = new ArrayList<>(NCARDS);
		selected = null;
		Random r = new Random();
		for (int i = 0; i < NCARDS; i++) {
			int n = r.nextInt(10) + 1;
			Card c = new Card("C" + n, "img" + n, n);
			c.flip();
			cartas.add(c);
		}
		observers = new LinkedList<>();
	}

	public List<Card> getCards() {
		return Collections.unmodifiableList(cartas);
	}

	public int getNumberOfCards() {
		return cartas.size();
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

}

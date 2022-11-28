package poo;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Field {

	private List<Card> monsterCards;
	private List<Card> specialCards;

	private CardStack graveyard;
	private CardStack stack;

	private List<GameListener> observers;

	public Field(CardStack stack) {
		this.stack = stack;

		monsterCards = new ArrayList<>(5);
		specialCards = new ArrayList<>(5);

		graveyard = new CardStack();

		observers = new LinkedList<>();
	}

	public List<Card> getMonsterCards() {
		return new ArrayList<>(monsterCards);
	}

	public List<Card> getSpecialCards() {
		return new ArrayList<>(specialCards);
	}

	public Card getGraveyardCard() {
		return graveyard.getPeek();
	}

	public CardStack getGraveyard() {
		return graveyard;
	}

	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}

	public void addCard(Card card) throws SizeLimitExceededException {

		if (card instanceof MonsterCard) {
			if (monsterCards.size() >= 5)
				throw new SizeLimitExceededException();

			monsterCards.add(card);
		} else {
			if (specialCards.size() >= 5)
				throw new SizeLimitExceededException();

			specialCards.add(card);
		}
	}

	public void removeCard(Card card) {
		if (card instanceof MonsterCard) {
			monsterCards.remove(card);
		} else {
			specialCards.remove(card);
		}

		for (var observer : observers) {
			observer.notify(new GameEvent(this, GameEvent.Target.DECK, GameEvent.Action.UPDATEDECK, ""));
		}
	}

	public void setCardsAsNonUsed() {
		for (Card card: monsterCards) {
			if (card instanceof MonsterCard) {
				MonsterCard monsterCard = (MonsterCard) card;
				monsterCard.setUsed(false);
			}
		}
	}

}

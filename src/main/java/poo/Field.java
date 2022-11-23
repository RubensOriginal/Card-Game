package poo;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

public class Field {

	private List<Card> monsterCards;
	private List<Card> specialCards;

	private CardStack graveyard;
	private CardStack stack;

	public Field(CardStack stack) {
		this.stack = stack;

		monsterCards = new ArrayList<>(5);
		specialCards = new ArrayList<>(5);

		graveyard = new CardStack();
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
}

package poo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class CardStack {

	private Stack<Card> stack;

	public CardStack() {
		stack = new Stack<>();
	}

	public void push(Card card) {
		stack.push(card);
	}

	public Card pop() {
		return stack.pop();
	}

	public boolean isEmpty() {
		return stack.isEmpty();
	}

	public Card getPeek() {
		return stack.peek();
	}

	public void buildStackCard() {
		if (!stack.empty())
			throw new RuntimeException("INVALID_STACK_BUILD_PROCESS");

		List<Card> cards = buildCardDeck();

		Random rand = new Random();

		while (cards.size() != 0)
			stack.push(cards.remove(rand.nextInt(cards.size())));

	}

	private List<Card> buildCardDeck() {
		List<Card> cards = new ArrayList<>(50);

		// Monster Cards
		cards.add(new MonsterCard("giant_soldier_of_stone", "/cards/13039848.jpg", 0, 3,1300,2000));
		cards.add(new MonsterCard("great_white", "/cards/13429800.jpg", 0, 4,1600,800));
		cards.add(new MonsterCard("man-eating_treasure_chest", "/cards/13723605.jpg", 0, 4,1600,1000));
		cards.add(new MonsterCard("gaia_the_fierce_knight", "/cards/6368038.jpg", 0, 7, 2300, 2100));

		// Magic Cards
		cards.add(new MagicCard("change_of_heart", "/cards/4031928.jpg", 0));

		// Trap Cards
		cards.add(new TrapCard("trap_hole", "/cards/4206964.jpg", 0));
		cards.add(new TrapCard("waboku", "/cards/12607053.jpg", 0));

		return cards;
	}


}

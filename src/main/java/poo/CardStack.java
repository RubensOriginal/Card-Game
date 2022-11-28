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
		cards.add(new MonsterCard("giant_soldier_of_stone", "/cards/13039848.jpg", 3,1300,2000));
		cards.add(new MonsterCard("great_white", "/cards/13429800.jpg", 4,1600,800));
		cards.add(new MonsterCard("man-eating_treasure_chest", "/cards/13723605.jpg", 4,1600,1000));
		cards.add(new MonsterCard("gaia_the_fierce_knight", "/cards/6368038.jpg", 7, 2300, 2100));
		cards.add(new MonsterCard("wall_of_ilussion", "/cards/13945283.jpg", 4, 1000, 1850));
		cards.add(new MonsterCard("mystical_elf", "/cards/15025844.jpg", 4, 800, 2000));
		cards.add(new MonsterCard("doma_the_angel_of_silence", "/cards/16972957.jpg", 5, 1600, 1400));
		cards.add(new MonsterCard("curse_of_dragon", "/cards/28279543.jpg", 5, 2000, 1500));
		cards.add(new MonsterCard("beaver_warrior", "/cards/32452818.jpg", 4, 1200, 1500));
		cards.add(new MonsterCard("witty_phantom", "/cards/36304921.jpg", 4, 1400, 1300));
		cards.add(new MonsterCard("mammoth_graveyard", "/cards/40374923.jpg", 3, 1200, 800));
		cards.add(new MonsterCard("claw_reacher", "/cards/41218256.jpg", 3, 1000, 800));
		cards.add(new MonsterCard("feral_imp", "/cards/41392891.jpg", 4, 1300, 1400));
		cards.add(new MonsterCard("trap_master", "/cards/46461247.jpg", 3, 500, 1100));
		cards.add(new MonsterCard("magical_ghost", "/cards/46474915.jpg", 4, 1300, 1400));
		cards.add(new MonsterCard("dark_magician", "/cards/46986414.jpg", 7, 2500, 2100));
		cards.add(new MonsterCard("mystic_clown", "/cards/47060154.jpg", 4, 1500, 1000));
		cards.add(new MonsterCard("ansatsu", "/cards/48365709.jpg", 5, 1700, 1200));
		cards.add(new MonsterCard("sorcerer_of_the_doomed", "/cards/49218300.jpg", 4, 1450, 1200));
		cards.add(new MonsterCard("neo_the_magician_swordsman", "/cards/50930991.jpg", 4, 1700, 1000));
		cards.add(new MonsterCard("man_eater_bug", "/cards/54652250.jpg", 2, 450, 650));
		cards.add(new MonsterCard("dragon_zombie", "/cards/66672569.jpg", 3, 1600, 0));
		cards.add(new MonsterCard("summoned_skull", "/cards/70781052.jpg", 6, 2500, 1200));
		cards.add(new MonsterCard("baron_of_the_fiend_sword", "/cards/86325596.jpg", 4, 1550, 800));
		cards.add(new MonsterCard("the_stern_mystic", "/cards/87557188.jpg", 4, 1500, 1200));
		cards.add(new MonsterCard("winged_dragon,_guardian_of_the_fortress1", "/cards/87796900.jpg", 4, 1400, 1200));
		cards.add(new MonsterCard("silver_fang", "/cards/90357090.jpg", 3, 1200, 800));
		cards.add(new MonsterCard("celtic_guardin", "/cards/91152256.jpg", 4, 1400, 1200));
		cards.add(new MonsterCard("ancient_elf", "/cards/93221206.jpg", 4, 1450, 1200));





		// Magic Cards
//		cards.add(new MagicCard("change_of_heart", "/cards/4031928.jpg"));
//		cards.add(new MagicCard("de-spell", "/cards/19159413.jpg"));
		cards.add(new MagicCard("sword_of_dark_destruction", "/cards/37120512.jpg", new SpecialEffect() {
			@Override
			public void applyMagic(int player, Card card) {
				if (card instanceof MonsterCard) {
					MonsterCard monsterCard = (MonsterCard) card;
					monsterCard.addDamage(400);
				}
			}

			@Override
			public MagicEnviroments getEnviroment() {
				return MagicEnviroments.CARD;
			}
		}));
//		cards.add(new MagicCard("remove_trap", "/cards/51482758.jpg"));
		cards.add(new MagicCard("dark_hole", "/cards/53129443.jpg", new SpecialEffect() {
			@Override
			public void applyMagic(int player, Card card) {
				List<Card> cards = Game.getInstance().getFieldJ1().getMonsterCards();
				cards.forEach(f -> {
					Game.getInstance().getFieldJ1().getGraveyard().push(f);
					Game.getInstance().getFieldJ1().removeCard(f);
				});

				cards = Game.getInstance().getFieldJ2().getMonsterCards();
				cards.forEach(f -> {
					Game.getInstance().getFieldJ2().getGraveyard().push(f);
					Game.getInstance().getFieldJ2().removeCard(f);
				});
			}

			@Override
			public MagicEnviroments getEnviroment() {
				return MagicEnviroments.PLAYER;
			}
		}));
//		cards.add(new MagicCard("yami", "/cards/59197169.jpg"));
//		cards.add(new MagicCard("fissure", "/cards/66788016.jpg"));
//		cards.add(new MagicCard("soul_exchange", "/cards/68005187.jpg"));
//		cards.add(new MagicCard("card_destruction", "/cards/72892473.jpg"));
//		cards.add(new MagicCard("monster_reborn", "/cards/83764719.jpg"));
//		cards.add(new MagicCard("dian_keto_the_cure_master", "/cards/84257640.jpg"));
//		cards.add(new MagicCard("last_will", "/cards/85602018.jpg"));
//		cards.add(new MagicCard("book_of_secret_arts", "/cards/91595718.jpg"));




		// Trap Cards
//		cards.add(new TrapCard("trap_hole", "/cards/4206964.jpg"));
//		cards.add(new TrapCard("waboku", "/cards/12607053.jpg"));
//		cards.add(new TrapCard("reverse_trap", "/cards/77622396.jpg"));
//		cards.add(new TrapCard("reinforcements", "/cards/17814387.jpg"));
//		cards.add(new TrapCard("castle_walls", "/cards/44209392.jpg"));
//		cards.add(new TrapCard("dragon_capture_jar", "/cards/50045299.jpg"));
//		cards.add(new TrapCard("ultimate_offering", "/cards/80604092.jpg"));
//		cards.add(new TrapCard("two_pronged_attack", "/cards/83887306.jpg"));


		return cards;
	}


}

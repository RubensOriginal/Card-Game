package poo;

import poo.exceptions.DeckSizeException;

import javax.naming.SizeLimitExceededException;
import java.util.LinkedList;
import java.util.List;

public class Game {
	private static Game game;
	private CardDeck deckJ1, deckJ2;
	private Field fieldJ1, fieldJ2;
	private StatusPlayer statusPlayerJ1, statusPlayerJ2;
	private List<GameListener> observers;
	private int round;
	private int player;
	private GameStages stage;
	private Card selectedCard;

	private int numMonstersAdded;

	public enum GameStages { BUYCARDSSTAGE, PREPAREATTACKSTAGE, PREPARECOUNTERATACKSTAGE, ATTACKSTAGE, ATTACKSTAGETWO, PREPAREDEFENCESTAGE, APPLYMAGIC};

	public static Game getInstance() {
		if (game == null)
			game = new Game();
		return game;
	}

	private Game() {
		round = 1;
		player = 1;
		numMonstersAdded = 0;
		stage = GameStages.BUYCARDSSTAGE;
		statusPlayerJ1 = new StatusPlayer();
		statusPlayerJ2 = new StatusPlayer();
		deckJ1 = new CardDeck(1);
		deckJ2 = new CardDeck(2);
		fieldJ1 = new Field();
		fieldJ2 = new Field();
		observers = new LinkedList<>();
	}

	public CardDeck getDeckJ1() {
		return deckJ1;
	}

	public CardDeck getDeckJ2() {
		return deckJ2;
	}

	public Field getFieldJ1() { return fieldJ1; }

	public Field getFieldJ2() { return fieldJ2; }

	public StatusPlayer getStatusPlayerJ1() {
		return statusPlayerJ1;
	}

	public StatusPlayer getStatusPlayerJ2() {
		return statusPlayerJ2;
	}

	public int getPlayer() {
		return player;
	}

	public GameStages getStage() {
		return stage;
	}

	public List<GameListener> getObservers() {
		return new LinkedList<>(observers);
	}

	public void play (CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		if (player == 1) {
			if (stage == GameStages.PREPAREATTACKSTAGE) {
				Card J1Card = deckJ1.getSelectedCard();

				try {
					if (J1Card instanceof MonsterCard && numMonstersAdded == 1) {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.NMONSTERROUND, ""));
						}
						return;
					}

					fieldJ1.addCard(J1Card);
					deckJ1.removeSel();
					if (J1Card instanceof MonsterCard)
						numMonstersAdded++;
					stage = GameStages.PREPARECOUNTERATACKSTAGE;
					player = 2;

				} catch (SizeLimitExceededException e) {
					for (var observer : observers) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.FIELDSIZE, ""));
					}
				}

				for (var observer : observers) {
					observer.notify(null);
				}
			}
		} else {
			if (stage == GameStages.PREPAREATTACKSTAGE) {
				Card J2Card = deckJ2.getSelectedCard();

				try {
					if (J2Card instanceof MonsterCard && numMonstersAdded == 1) {
						for (var observer : observers) {
							observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.NMONSTERROUND, ""));
						}
						return;
					}

					fieldJ2.addCard(J2Card);
					deckJ2.removeSel();
					if (J2Card instanceof MonsterCard)
						numMonstersAdded++;
					stage = GameStages.PREPARECOUNTERATACKSTAGE;
					player = 1;

				} catch (SizeLimitExceededException e) {
					for (var observer : observers) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.FIELDSIZE, ""));
					}
				}

				for (var observer : observers) {
					observer.notify(null);
				}
			}
		}
	}

	public void playField(Field field, CardView cv, int player) {

		if (cv.getCardType() == CardView.CardType.STACKCARD && getStage() == GameStages.BUYCARDSSTAGE) {
			try {
				if (getPlayer() == 1 && player == 1) {
					getDeckJ1().addCardToDeck();
				} else if (getPlayer() == 2 && player == 2) {
					getDeckJ2().addCardToDeck();
				}
			} catch (DeckSizeException e) {
				for (var observer: getObservers()) {
					observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.DECKSIZE, ""));
				}
			}
		} else if (cv.getCardType() == CardView.CardType.FIELDCARD && (getStage() == GameStages.PREPAREATTACKSTAGE || getStage() == GameStages.PREPAREDEFENCESTAGE)) {
			if (player == getPlayer()) {
				if (cv.getCard() instanceof MagicCard) {
					MagicCard magicCard = (MagicCard) cv.getCard();
					if (!magicCard.isUsed()) {
						if (magicCard.getSpecialEffect().getEnviroment() == MagicEnviroments.CARD) {
							selectedCard = magicCard;
							stage = GameStages.APPLYMAGIC;
						} else {
							magicCard.getSpecialEffect().applyMagic(getPlayer(), null);
							if (!magicCard.getSpecialEffect().stayInField())
								field.removeCard(cv.getCard());
						}
					}
				}
			}
		} else if (cv.getCardType() == CardView.CardType.FIELDCARD && getStage() == GameStages.APPLYMAGIC) {
			if (cv.getCard() instanceof MonsterCard) {
				MagicCard magicCard = (MagicCard) selectedCard;
				magicCard.getSpecialEffect().applyMagic(getPlayer(), cv.getCard());
				magicCard.setUsed(true);
				if (!magicCard.getSpecialEffect().stayInField())
					field.removeCard(cv.getCard());
			}
		}else if (cv.getCardType() == CardView.CardType.FIELDCARD && getStage() == GameStages.ATTACKSTAGE) {
			if (player == getPlayer() && cv.getCard() instanceof MonsterCard) {

				MonsterCard monsterCard = (MonsterCard) cv.getCard();
				if (!monsterCard.isUsed()) {
					selectedCard = cv.getCard();
					stage = GameStages.ATTACKSTAGETWO;
					for (var observer : observers) {
						observer.notify(null);
					}
				} else {
					for (var observer : observers) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MONSTERUSEDPREVIOUSLY, "" + player));
					}
				}

			} else {
				for (var observer : observers) {
					observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "" + player));
				}
			}
		} else if (cv.getCardType() == CardView.CardType.FIELDCARD && getStage() == GameStages.ATTACKSTAGETWO) {
			if (player != getPlayer()) {

				MonsterCard playerCard = (MonsterCard) selectedCard;
				MonsterCard otherCard = (MonsterCard) cv.getCard();
				if (playerCard.getAttack() > otherCard.getAttack()) {
					int damage = playerCard.getAttack() - otherCard.getAttack();
					if (getPlayer() == 1) {
						statusPlayerJ2.reduceLife(damage);
						fieldJ2.getGraveyard().push(otherCard);
						fieldJ2.removeCard(otherCard);
					} else {
						statusPlayerJ1.reduceLife(damage);
						fieldJ1.getGraveyard().push(otherCard);
						fieldJ1.removeCard(otherCard);
					}

					playerCard.setUsed(true);

				} else if (playerCard.getAttack() == otherCard.getAttack()) {
					if (getPlayer() == 1) {
						fieldJ1.getGraveyard().push(playerCard);
						fieldJ1.removeCard(playerCard);
						fieldJ2.getGraveyard().push(otherCard);
						fieldJ2.removeCard(otherCard);
					} else {
						fieldJ1.getGraveyard().push(otherCard);
						fieldJ1.removeCard(otherCard);
						fieldJ2.getGraveyard().push(playerCard);
						fieldJ2.removeCard(playerCard);
					}
				} else {
					int damage =  otherCard.getAttack() - playerCard.getAttack();
					if (getPlayer() == 1) {
						statusPlayerJ1.reduceLife(damage);
						fieldJ1.getGraveyard().push(playerCard);
						fieldJ1.removeCard(playerCard);
					} else {
						statusPlayerJ2.reduceLife(damage);
						fieldJ2.getGraveyard().push(playerCard);
						fieldJ2.removeCard(playerCard);
					}
				}

				selectedCard = null;
				stage = GameStages.ATTACKSTAGE;

				for (var observer : observers) {
					observer.notify(null);
				}

				if (statusPlayerJ1.getLife() == 0) {
					for (var observer: Game.getInstance().getObservers()) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "2"));
					}
				} else if (statusPlayerJ2.getLife() == 0) {
					for (var observer: Game.getInstance().getObservers()) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "1"));
					}
				}

			} else {
				if (getPlayer() == 1) {
					for (var observer : observers) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "2"));
					}
				} else {
					for (var observer : observers) {
						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "1"));
					}
				}
			}
		}
	}

	public void nextStage() {
		switch (stage) {
			case BUYCARDSSTAGE:
				numMonstersAdded = 0;
				stage = GameStages.PREPAREATTACKSTAGE;
				break;
			case PREPAREATTACKSTAGE:
				stage = GameStages.ATTACKSTAGE;
				selectedCard = null;
				if (round == 1)
					stage = GameStages.PREPAREDEFENCESTAGE;
				break;
			case PREPARECOUNTERATACKSTAGE:
				stage = GameStages.PREPAREATTACKSTAGE;
				if (player == 1)
					player = 2;
				else
					player = 1;
				break;
			case ATTACKSTAGE:
				stage = GameStages.PREPAREDEFENCESTAGE;
				break;
			case PREPAREDEFENCESTAGE:
				stage = GameStages.BUYCARDSSTAGE;
				if (player == 1) {
					player = 2;
					fieldJ1.setCardsAsNonUsed();
				} else {
					player = 1;
					fieldJ2.setCardsAsNonUsed();
					round++;
				}
				break;
		}

		for (var observer : observers) {
			observer.notify(null);
		}
	}
	
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}

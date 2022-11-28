package poo;

import poo.exceptions.DeckSizeException;

import javax.naming.SizeLimitExceededException;
import java.util.LinkedList;
import java.util.List;

public class Game {
	private static Game game;
	private int ptsJ1, ptsJ2;
	private CardDeck deckJ1, deckJ2;
	private Field fieldJ1, fieldJ2;
	private StatusPlayer statusPlayerJ1, statusPlayerJ2;
	private int playerStage;
	private int jogadas;
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
		ptsJ1 = 0;
		ptsJ2 = 0;
		round = 1;
		player = 1;
		numMonstersAdded = 0;
		stage = GameStages.BUYCARDSSTAGE;
		statusPlayerJ1 = new StatusPlayer(1);
		statusPlayerJ2 = new StatusPlayer(2);
		deckJ1 = new CardDeck(1);
		deckJ2 = new CardDeck(2);
		fieldJ1 = new Field(null);
		fieldJ2 = new Field(null);
		playerStage = 1;
		jogadas = CardDeck.NCARDS;
		observers = new LinkedList<>();
	}

	private void nextPlayer() {
		playerStage++;
		if (playerStage == 4) {
			playerStage = 1;
		}
	}

	public int getPtsJ1() {
		return ptsJ1;
	}

	public int getPtsJ2() {
		return ptsJ2;
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

	public int getPlayerStage() {
		return playerStage;
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
					// Add an alert here
					throw new RuntimeException(e);
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
					// Add an alert here
					throw new RuntimeException(e);
				}

				for (var observer : observers) {
					observer.notify(null);
				}
			}
		}
	}

	public void playField(Field field, CardView cv, int player) {
//		for (var observer: getObservers()) {
//			observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.PRINTDATA, (cv.getCardType() == CardView.CardType.FIELDCARD && getStage() == GameStages.ATTACKSTAGE) + "\n" + (player == getPlayer()) + ""));
//		}

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
					if (magicCard.getSpecialEffect().getEnviroment() == MagicEnviroments.CARD) {
						selectedCard = magicCard;
						stage = GameStages.APPLYMAGIC;
					} else {
						magicCard.getSpecialEffect().applyMagic(getPlayer(), null);
						field.removeCard(cv.getCard());
					}
				}
			}
//			if (player == 1) {
//				if (getPlayerStage() == 1) {
//					getFieldJ1().getGraveyard().push(selectedCard);
//					getFieldJ1().removeCard(selectedCard);
//				}
//			} else {
//				if (getPlayerStage() == 2) {
//					getFieldJ2().getGraveyard().push(selectedCard);
//					getFieldJ2().removeCard(selectedCard);
//				}
//			}
		} else if (cv.getCardType() == CardView.CardType.FIELDCARD && getStage() == GameStages.APPLYMAGIC) {
			if (cv.getCard() instanceof MonsterCard) {
				MagicCard magicCard = (MagicCard) selectedCard;
				magicCard.getSpecialEffect().applyMagic(getPlayer(), cv.getCard());
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


//			for (var observer : observers) {
//				observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "1"));
//			}
//			if (!(cv.getCard() instanceof MonsterCard)) {
//				for (var observer : observers) {
//					observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "1"));
//				}
//			}
//			if (getPlayer() == player) {
//				selectedCard = cv.getCard();
//			} else {
//				if (selectedCard == null) {
//					for (var observer : observers) {
//						observer.notify(new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVALIDATTACK, "1"));
//					}
//				} else {
//					MonsterCard playerCard = (MonsterCard) selectedCard;
//					MonsterCard otherCard = (MonsterCard) cv.getCard();
//
//					if (playerCard.getAttack() > otherCard.getAttack()) {
//						int damage = playerCard.getAttack() - otherCard.getAttack();
//						statusPlayerJ2.reduceLife(damage);
//
//					} else {
//						// Perguntar para o Henrique
//					}
//
//					for (var observer: observers) {
//						observer.notify(null);
//					}
//
//				}
//			}
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

//	public void play(CardDeck deckAcionado) {
//		GameEvent gameEvent = null;
//		if (playerStage == 3) {
//			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
//			for (var observer : observers) {
//				observer.notify(gameEvent);
//			}
//			return;
//		}
//		if (deckAcionado == deckJ1) {
//			if (playerStage != 1) {
//				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
//				for (var observer : observers) {
//					observer.notify(gameEvent);
//				}
//			} else {
//				// Vira a carta
//				Card J1Card = deckJ1.getSelectedCard();
//				deckJ1.removeSel();
//
//				try {
//					fieldJ1.addCard(J1Card);
//
//					if (J1Card instanceof MonsterCard)
//						nextPlayer();
//				} catch (SizeLimitExceededException e) {
//					// Add an alert here
//					throw new RuntimeException(e);
//				}
//
//				for (var observer : observers) {
//					if (observer instanceof FieldView)
//						observer.notify(gameEvent);
//				}
//			}
//		} else if (deckAcionado == deckJ2) {
//			if (playerStage != 2) {
//				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
//				for (var observer : observers) {
//					observer.notify(gameEvent);
//				}
//			} else {
//				// Vira a carta
//
//				Card J2Card = deckJ2.getSelectedCard();
//				deckJ2.removeSel();
//
//				try {
//					fieldJ2.addCard(J2Card);
//
//					if (J2Card instanceof MonsterCard) {
//						nextPlayer();
//						nextPlayer();
//					}
//				} catch (SizeLimitExceededException e) {
//					// Add an alert here
//					throw new RuntimeException(e);
//				}
//
//				// deckJ2.getSelectedCard().flip();
//				// // Verifica quem ganhou a rodada
//				// if (deckJ1.getSelectedCard().getValue() > deckJ2.getSelectedCard().getValue()) {
//				// 	ptsJ1++;
//				// } else if (deckJ1.getSelectedCard().getValue() < deckJ2.getSelectedCard().getValue()) {
//				// 	ptsJ2++;
//				// }
//
//
//				for (var observer : observers) {
//					observer.notify(gameEvent);
//				}
//				// Próximo jogador
//			}
//		}
//	}

	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (playerStage != 3) {
			return;
		}
		jogadas--;
		if (jogadas == 0) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.ENDGAME, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
		}
		deckJ1.removeSel();
		deckJ2.removeSel();
		nextPlayer();
	}
	
	public void addGameListener(GameListener listener) {
		observers.add(listener);
	}
}

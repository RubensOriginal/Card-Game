package poo;

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

	private int numMonstersAdded;

	public enum GameStages { BUYCARDSSTAGE, PREPAREATTACKSTAGE, PREPARECOUNTERATACKSTAGE, ATTACKSTAGE, PREPAREDEFENCESTAGE};
	
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
		statusPlayerJ1 = new StatusPlayer();
		statusPlayerJ2 = new StatusPlayer();
		deckJ1 = new CardDeck();
		deckJ2 = new CardDeck();
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

	public void play (CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		if (player == 1) {
			if (stage == GameStages.PREPAREATTACKSTAGE) {
				Card J1Card = deckJ1.getSelectedCard();

				try {
					if (J1Card instanceof MonsterCard && numMonstersAdded == 1)
						return; // Retornar alerta

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
					if (J2Card instanceof MonsterCard && numMonstersAdded == 1)
						return; // Retornar alerta

					fieldJ1.addCard(J2Card);
					deckJ1.removeSel();
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

	public void nextStage() {
		switch (stage) {
			case BUYCARDSSTAGE:
				numMonstersAdded = 0;
				stage = GameStages.PREPAREATTACKSTAGE;
				break;
			case PREPAREATTACKSTAGE:
				stage = GameStages.ATTACKSTAGE;
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
				if (player == 1)
					player = 2;
				else {
					player = 1;
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

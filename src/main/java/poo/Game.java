package poo;

import javax.naming.SizeLimitExceededException;
import java.util.LinkedList;
import java.util.List;

public class Game {
	private static Game game = new Game();
	private int ptsJ1, ptsJ2;
	private CardDeck deckJ1, deckJ2;
	private Field fieldJ1, fieldJ2;
	private int player;
	private int jogadas;
	private List<GameListener> observers;
	
	public static Game getInstance() {
		return game;
	}

	private Game() {
		ptsJ1 = 0;
		ptsJ2 = 0;
		deckJ1 = new CardDeck();
		deckJ2 = new CardDeck();
		fieldJ1 = new Field(null);
		fieldJ2 = new Field(null);
		player = 1;
		jogadas = CardDeck.NCARDS;
		observers = new LinkedList<>();
	}

	private void nextPlayer() {
		player++;
		if (player == 4) {
			player = 1;
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

	public void play(CardDeck deckAcionado) {
		GameEvent gameEvent = null;
		if (player == 3) {
			gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.MUSTCLEAN, "");
			for (var observer : observers) {
				observer.notify(gameEvent);
			}
			return;
		}
		if (deckAcionado == deckJ1) {
			if (player != 1) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "2");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta
				Card J1Card = deckJ1.getSelectedCard();
				deckJ1.removeSel();

				try {
					fieldJ1.addCard(J1Card);

					
					nextPlayer();
				} catch (SizeLimitExceededException e) {
					// Add an alert here
					throw new RuntimeException(e);
				}

				for (var observer : observers) {
					if (observer instanceof FieldView)
						observer.notify(gameEvent);
				}
			}
		} else if (deckAcionado == deckJ2) {
			if (player != 2) {
				gameEvent = new GameEvent(this, GameEvent.Target.GWIN, GameEvent.Action.INVPLAY, "1");
				for (var observer : observers) {
					observer.notify(gameEvent);
				}
			} else {
				// Vira a carta

				Card J2Card = deckJ2.getSelectedCard();
				deckJ2.removeSel();

				try {
					fieldJ2.addCard(J2Card);

					nextPlayer();
				} catch (SizeLimitExceededException e) {
					// Add an alert here
					throw new RuntimeException(e);
				}

				// deckJ2.getSelectedCard().flip();
				// // Verifica quem ganhou a rodada
				// if (deckJ1.getSelectedCard().getValue() > deckJ2.getSelectedCard().getValue()) {
				// 	ptsJ1++;
				// } else if (deckJ1.getSelectedCard().getValue() < deckJ2.getSelectedCard().getValue()) {
				// 	ptsJ2++;
				// }


				for (var observer : observers) {
					observer.notify(gameEvent);
				}
				// Próximo jogador
				nextPlayer();
			}
		}
	}

	// Acionada pelo botao de limpar
	public void removeSelected() {
		GameEvent gameEvent = null;
		if (player != 3) {
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

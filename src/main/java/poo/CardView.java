package poo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.scene.control.Button;

public class CardView extends Button implements PropertyChangeListener{
	private Card card;
	private CardView thisCardView;
	private CardViewListener observer;

	private CardType cardType;

	public enum CardType {
		DECKCARD, FIELDCARD, GRAVEYARDCARD, NULLCARD, STACKCARD
	}

	public CardView(Card aCard, CardType cardType) {
		super("", ImageFactory.getInstance().createImage(aCard.getImageId()));

		card = aCard;
		card.addPropertyChangeListener(this);
		thisCardView = this;

		this.cardType = cardType;

		this.setOnAction(e -> {
			if (observer != null) {
				observer.handle(new CardViewEvent(thisCardView));
			}
		});
	}

	public void setCardViewObserver(CardViewListener obs) {
		observer = obs;
	}

	public Card getCard() {
		return card;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (card.isFacedUp()) {
			this.setGraphic(ImageFactory.getInstance().createImage(card.getImageId()));
		} else {
			this.setGraphic(ImageFactory.getInstance().createImage("/cards/back.jpg"));
		}		
	}
}

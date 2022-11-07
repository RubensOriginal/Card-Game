package poo;

import javafx.event.Event;

public class CardViewEvent extends Event {

	private static final long serialVersionUID = 1138616432432038288L;

	public CardViewEvent(CardView source) {
		super(source, null, ANY);
	}
	
	public CardView getCardView() {
		return (CardView) getSource();
	}
}

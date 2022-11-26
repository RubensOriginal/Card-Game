package poo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class CardView extends Button implements PropertyChangeListener{
	private Card card;
	private CardView thisCardView;
	private CardViewListener observer;

	private CardType cardType;

	public enum CardType {
		DECKCARD, FIELDCARD, GRAVEYARDCARD, NULLCARD, STACKCARD
	}

	public CardView(Card aCard, CardType cardType) {
		super("", ImageFactory.getInstance().createImage(aCard.getImageId(), false));

		card = aCard;
		card.addPropertyChangeListener(this);
		thisCardView = this;

		this.cardType = cardType;

		this.setOnAction(e -> {
			if (observer != null) {
				observer.handle(new CardViewEvent(thisCardView));
			}
		});

		this.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				Stage stage = new Stage();
				stage.setTitle("Carta Ampliada");
				GridPane grid = new GridPane();
				grid.setHgap(10);
				grid.setVgap(10);
				grid.setPadding(new Insets(10, 10, 10, 10));

				ImageView image = ImageFactory.getInstance().createImage(getCard().getImageId(), true);

				grid.add(image, 0,0);

				Scene scene = new Scene(grid);
				stage.setScene(scene);
				stage.show();
			}
		});
	}

	public void setCardViewObserver(CardViewListener obs) {
		observer = obs;
	}

	public Card getCard() {
		return card;
	}

	public CardType getCardType() {
		return cardType;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (card.isFacedUp()) {
			this.setGraphic(ImageFactory.getInstance().createImage(card.getImageId(), false));
		} else {
			this.setGraphic(ImageFactory.getInstance().createImage("/cards/back.jpg", false));
		}		
	}
}

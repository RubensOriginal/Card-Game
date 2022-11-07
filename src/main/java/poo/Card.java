package poo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Card {
	private String id;
	private String imageId;
	private int value;
	private boolean faceUp;
	private final PropertyChangeSupport pcs;

	public Card(String anId, String anImageId, int val) {
		id = anId;
		imageId = anImageId;
		value = val;
		faceUp = true;
		pcs = new PropertyChangeSupport(this);
	}

	public String getId() {
		return id;
	}

	public String getImageId() {
		return imageId;
	}

	public int getValue() {
		return value;
	}

	public boolean isFacedUp() {
		return faceUp;
	}

	public void flip() {
		boolean old = faceUp;
		faceUp = !faceUp;
		pcs.firePropertyChange("facedUp", old, faceUp);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}

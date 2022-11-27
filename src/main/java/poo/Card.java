package poo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Card {

	private String id;
	private String imageUrl;
	private boolean faceUp;
	private boolean used;
	private final PropertyChangeSupport pcs;

	public Card(String id, String imageUrl) {
		this.id = id;
		this.imageUrl = imageUrl;
		faceUp = true;
		pcs = new PropertyChangeSupport(this);
	}

	public String getId() {
		return id;
	}

	public String getImageId() {
		return imageUrl;
	}

	public boolean isFacedUp() {
		return faceUp;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isUsed() {
		return used;
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

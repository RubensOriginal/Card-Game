package poo;

public class MagicCard extends Card{

	SpecialEffect specialEffect;
	private boolean used;

	public MagicCard(String anId, String anImageId, SpecialEffect specialEffect) {
		super(anId, anImageId);
		this.specialEffect = specialEffect;
		used = false;
	}

	public SpecialEffect getSpecialEffect() {
		return specialEffect;
	}

	@Override
	public boolean isUsed() {
		return used;
	}

	@Override
	public void setUsed(boolean used) {
		this.used = used;
	}
}

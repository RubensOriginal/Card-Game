package poo;

public class MagicCard extends Card{

	SpecialEffect specialEffect;

	public MagicCard(String anId, String anImageId, SpecialEffect specialEffect) {
		super(anId, anImageId);
		this.specialEffect = specialEffect;
	}

	public SpecialEffect getSpecialEffect() {
		return specialEffect;
	}
}

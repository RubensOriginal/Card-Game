package poo;

public class MonsterCard extends Card{

	private int level;
	private int attack;
	private int defence;

	public MonsterCard(String anId, String anImageId, int val, int level, int attack, int defence) {
		super(anId, anImageId, val);
		this.level = level;
		this.attack = attack;
		this.defence = defence;
	}

	public int getLevel() {
		return level;
	}

	public int getAttack() {
		return attack;
	}

	public int getDefence() {
		return defence;
	}
}
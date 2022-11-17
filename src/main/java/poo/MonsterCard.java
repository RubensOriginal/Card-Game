package poo;

public class MonsterCard extends Card{

	private int level;
	private int attack;
	private int defence;

	public MonsterCard(String id, String imageUrl, int value, int level, int attack, int defence) {
		super(id, imageUrl, value);
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

package poo;

public class MonsterCard extends Card{

	private int level;
	private int attack;
	private int defence;
	private int boostDamage;

	public MonsterCard(String id, String imageUrl, int level, int attack, int defence) {
		super(id, imageUrl);
		this.level = level;
		this.attack = attack;
		this.defence = defence;
	}

	public int getLevel() {
		return level;
	}

	public int getAttack() {
		return attack + boostDamage;
	}

	public int getDefence() {
		return defence;
	}

	public void setBoostDamage(int boostDamage) {
		this.boostDamage = boostDamage;
	}

	public int getBoostDamage() {
		return boostDamage;
	}

	public void addDamage(int damage) {
		attack += damage;
	}

	public void remodeDamage(int damage) {
		attack -= damage;
	}
}

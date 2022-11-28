package poo;

public interface SpecialEffect {

	void applyMagic(int player, Card card);

	MagicEnviroments getEnviroment();
	boolean stayInField();

}

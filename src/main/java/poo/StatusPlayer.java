package poo;

public class StatusPlayer {

	private int life;

	public StatusPlayer() {
		this.life = 8000;
	}

	public void addLife(int life) {
		life += life;
	}

	public void reduceLife(int life) {
		life -= life;
	}

	public int getLife() {
		return life;
	}
}

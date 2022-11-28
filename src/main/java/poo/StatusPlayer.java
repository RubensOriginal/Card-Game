package poo;

public class StatusPlayer {

	private int life;

	public StatusPlayer() {
		this.life = 8000;
	}

	public void addLife(int life) {
		this.life += life;
	}

	public void reduceLife(int life) {
		this.life -= life;

		if (this.life <= 0) {
			this.life = 0;
		}
	}

	public int getLife() {
		return life;
	}
}

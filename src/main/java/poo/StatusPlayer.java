package poo;

public class StatusPlayer {

	private int life;
	private int player;

	public StatusPlayer(int player) {
		this.life = 10;
		this.player = player;
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

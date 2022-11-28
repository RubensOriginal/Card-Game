package poo;

import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class StatusPlayerView extends GridPane implements GameListener {

	private StatusPlayer status;
	private int player;

	private Text lifeText;
	private Text stageText;


	public StatusPlayerView(StatusPlayer status, int player) {

		this.status = status;
		this.player = player;

		this.setHgap(10);
		this.setVgap(10);
		this.setPadding(new Insets(5, 5, 5, 5));

		lifeText = new Text();
		stageText = new Text();

		lifeText.setText("Vida do Jogador " + player + ": " + status.getLife());

		if (Game.getInstance().getPlayer() == player) {
			switch (Game.getInstance().getStage()) {
				case BUYCARDSSTAGE:
					stageText.setText("Estágio Atual: Compre Cartas");
					break;
				case PREPAREATTACKSTAGE:
					stageText.setText("Estágio Atual: Prepare o seu Ataque");
					break;
				case PREPARECOUNTERATACKSTAGE:
					stageText.setText("Estágio Atual: Prepare o seu Contra-Ataque");
					break;
				case APPLYMAGIC:
					stageText.setText("Estágio Atual: Aplique a magia na carta necessária");
				case ATTACKSTAGE:
					stageText.setText("Estágio Atual: ATAQUE!!!");
					break;
				case ATTACKSTAGETWO:
					stageText.setText("Selecione o monstro que você quer atacar");
					break;
				case PREPAREDEFENCESTAGE:
					stageText.setText("Estágio Atual: Prepare sua Defesa");
					break;
			}
		} else {
			stageText.setText("");
		}


		this.add(lifeText, 0,0);
		this.add(stageText, 1, 0);

		Game.getInstance().addGameListener(this);
	}

	@Override
	public void notify(GameEvent event) {
		lifeText.setText("Vida do Jogador" + player + ": " + status.getLife());

		if (Game.getInstance().getPlayer() == player) {
			switch (Game.getInstance().getStage()) {
				case BUYCARDSSTAGE:
					stageText.setText("Estágio Atual: Compre Cartas");
					break;
				case PREPAREATTACKSTAGE:
					stageText.setText("Estágio Atual: Prepare o seu Ataque");
					break;
				case PREPARECOUNTERATACKSTAGE:
					stageText.setText("Estágio Atual: Prepare o seu Contra-Ataque");
					break;
				case APPLYMAGIC:
					stageText.setText("Estágio Atual: Aplique a magia na carta necessária");
				case ATTACKSTAGE:
					stageText.setText("Estágio Atual: ATAQUE!!!");
					break;
				case ATTACKSTAGETWO:
					stageText.setText("Selecione o monstro que você quer atacar");
					break;
				case PREPAREDEFENCESTAGE:
					stageText.setText("Estágio Atual: Prepare sua Defesa");
					break;
			}
		} else {
			stageText.setText("");
		}
	}
}

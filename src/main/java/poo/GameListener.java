package poo;

import java.util.EventListener;

public interface GameListener extends EventListener {
	void notify(GameEvent event);
}

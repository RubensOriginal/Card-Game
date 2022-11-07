package poo;

import java.util.EventObject;

public class GameEvent extends EventObject {
	private static final long serialVersionUID = 2066588332276925470L;

	public enum Target {
		DECK, GWIN
	};

	public enum Action {
		INVPLAY, MUSTCLEAN, ENDGAME, REMOVESEL
	};

	private Target target;
	private Action action;
	private String arg;

	public GameEvent(Object source, Target aTarget, Action anAction, String anArg) {
		super(source);
		target = aTarget;
		action = anAction;
		arg = anArg;
	}

	public Target getTarget() {
		return target;
	}

	public Action getAction() {
		return action;
	}

	public String getArg() {
		return arg;
	}

	public String toString() {
		return target + ":" + action + ":" + arg;
	}
}

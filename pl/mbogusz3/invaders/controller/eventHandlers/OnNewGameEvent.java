package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersNewGameException;

import java.util.HashMap;

public class OnNewGameEvent extends InvadersEventHandler {
	public OnNewGameEvent(InvadersModel model) {
		this.model = model;
	}

	public void execute(HashMap<String, String> payload) throws InvadersNewGameException {
		synchronized (model) {
			model.startNewGame();
		}

		throw new InvadersNewGameException();
	}
}
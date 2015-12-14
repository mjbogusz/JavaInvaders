package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersExitException;
import java.util.HashMap;

public class OnExitEvent extends InvadersEventHandler {
	public OnExitEvent(InvadersModel model) {
		this.model = model;
	}

	public void execute(HashMap<String, String> payload) throws InvadersExitException {
		synchronized (model) {
			model.exit();
		}
		throw new InvadersExitException();
	}
}
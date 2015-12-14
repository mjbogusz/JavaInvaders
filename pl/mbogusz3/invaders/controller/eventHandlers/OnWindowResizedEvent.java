package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import java.util.HashMap;

/**
 *
 */
public class OnWindowResizedEvent extends InvadersEventHandler {
	public OnWindowResizedEvent(InvadersModel model) {
		this.model = model;
	}

	public void execute(HashMap<String, String> payload) {
		synchronized (model) {
			model.recalculate(0.0, true);
		}
	}
}
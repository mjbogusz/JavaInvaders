package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import java.util.HashMap;

/**
 * This event handler class is used for handling "windowResized" event.
 */
public class OnWindowResizedEvent extends InvadersEventHandler {
	/**
	 * OnNewGameEvent class constructor.
	 * @param model Invaders' model for this event handler.
	 */
	public OnWindowResizedEvent(InvadersModel model) {
		this.model = model;
	}

	/**
	 * Handle "windowResized" event by calling model's {@link InvadersModel#recalculate(double, boolean)} method and enforcing update.
	 * @param payload payload of event to handle.
	 */
	public void execute(HashMap<String, String> payload) {
		synchronized (model) {
			model.recalculate(0.0, true);
		}
	}
}
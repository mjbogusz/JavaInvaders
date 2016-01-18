package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import java.util.HashMap;

/**
 * This event handler class is used for handling "keyUp" event.
 */
public class OnKeyUpEvent extends InvadersEventHandler {
	/**
	 * OnKeyUpEvent class constructor.
	 * @param model Invaders' model for this event handler.
	 */
	public OnKeyUpEvent(InvadersModel model) {
		this.model = model;
	}

	/**
	 * Handle "keyUp" event by calling model's {@link InvadersModel#setKeyUp(int)} method.
	 * @param payload payload of event to handle, should contain Integer on "keyCode" field.
	 */
	public void execute(HashMap<String, String> payload) {
		int keyCode = Integer.parseInt(payload.get("keyCode"));

		// Ignore extended keymap buttons and deduplicate
		if(keyCode > 255 || !model.getKeyState(keyCode)) {
			return;
		}

		synchronized (model) {
			model.setKeyUp(keyCode);
		}
	}
}
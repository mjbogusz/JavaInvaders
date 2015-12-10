package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import java.util.HashMap;

public class OnKeyUpEvent extends InvadersEventHandler {
	public OnKeyUpEvent(InvadersModel model) {
		this.model = model;
	}

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
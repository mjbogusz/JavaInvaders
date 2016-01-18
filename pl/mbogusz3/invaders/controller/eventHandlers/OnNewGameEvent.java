package pl.mbogusz3.invaders.controller.eventHandlers;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.types.InvadersEventHandler;
import pl.mbogusz3.invaders.types.InvadersNewGameException;

import java.util.HashMap;

/**
 * This event handler class is used for handling "newGame" event.
 */
public class OnNewGameEvent extends InvadersEventHandler {
	/**
	 * OnNewGameEvent class constructor.
	 * @param model Invaders' model for this event handler.
	 */
	public OnNewGameEvent(InvadersModel model) {
		this.model = model;
	}

	/**
	 * Handle "newGame" event by calling model's {@link InvadersModel#startNewGame()} method.
	 * @param payload payload of event to handle.
	 */
	public void execute(HashMap<String, String> payload) throws InvadersNewGameException {
		synchronized (model) {
			model.startNewGame();
		}

		throw new InvadersNewGameException();
	}
}
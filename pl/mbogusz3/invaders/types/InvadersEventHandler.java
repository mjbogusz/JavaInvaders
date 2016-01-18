package pl.mbogusz3.invaders.types;

import pl.mbogusz3.invaders.model.InvadersModel;
import java.util.HashMap;

/**
 * Invaders' event handler abstract class.
 * Child classes are responsible of handling events of single types.
 */
public abstract class InvadersEventHandler {
	/**
	 * Invaders' model object to perform event handling actions on.
	 */
	protected InvadersModel model;

	/**
	 * Execute handler's actions on model, according to event payload.
	 * @param payload payload of event to handle.
	 * @throws Exception optional exception for notifying controller about specific events like game exit.
	 */
	public abstract void execute(HashMap<String, String> payload) throws Exception;
}
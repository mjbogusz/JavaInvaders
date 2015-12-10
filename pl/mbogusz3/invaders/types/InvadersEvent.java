package pl.mbogusz3.invaders.types;

import java.util.HashMap;

/**
 * Game's universal event class.
 */
public class InvadersEvent {
	/**
	 * Event type, e.g. "newGame", "keyUp".
	 */
	public String type;
	/**
	 * Event's payload, containing additional information for controller/event handler
	 */
	public HashMap<String, String> payload;

	public InvadersEvent(String type) {
		this.type = type;
		this.payload = new HashMap<String, String>();
	}
}
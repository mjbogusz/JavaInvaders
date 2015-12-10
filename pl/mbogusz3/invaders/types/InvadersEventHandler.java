package pl.mbogusz3.invaders.types;

import pl.mbogusz3.invaders.model.InvadersModel;
import java.util.HashMap;

public abstract class InvadersEventHandler {
	protected InvadersModel model;

	public abstract void execute(HashMap<String, String> payload) throws Exception;
}
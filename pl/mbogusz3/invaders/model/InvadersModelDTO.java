package pl.mbogusz3.invaders.model;

import pl.mbogusz3.invaders.model.InvadersModel;
import pl.mbogusz3.invaders.model.Player;
import java.io.Serializable;

/**
 */
public class InvadersModelDTO implements Serializable {
	public final PlayerDTO player;

	public InvadersModelDTO(InvadersModel model) {
		this.player = new PlayerDTO(model.getPlayer());
	}

	public class PlayerDTO {
		public final int health;
		public final double width;
		public final double position;

		public PlayerDTO(Player player) {
			this.health = player.getHealth();
			this.width = player.getWidth();
			this.position = player.getPosition();
		}

		public int getHealth() {
			return health;
		}

		public double getWidth() {
			return width;
		}

		public double getPosition() {
			return position;
		}
	}

	public PlayerDTO getPlayer() {
		return player;
	}
}

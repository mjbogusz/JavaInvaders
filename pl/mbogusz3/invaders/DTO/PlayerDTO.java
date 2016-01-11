package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Player;

/**
 */
public class PlayerDTO {
	public final int health;
	public final double width;
	public final double position;

	public PlayerDTO(Player player) {
		this.health = player.getHealth();
		this.width = Player.width;
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

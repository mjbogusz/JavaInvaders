package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Player;

/**
 */
public class PlayerDTO {
	public final int health;
	public final double width;
	public final double height;
	public final double position;
	public final double positionTop;

	public PlayerDTO(Player player) {
		this.health = player.getHealth();
		this.width = Player.width;
		this.height = Player.height;
		this.position = player.getPosition();
		this.positionTop = Player.positionTop;
	}

	public int getHealth() {
		return health;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public double getPosition() {
		return position;
	}

	public double getPositionTop() {
		return positionTop;
	}
}

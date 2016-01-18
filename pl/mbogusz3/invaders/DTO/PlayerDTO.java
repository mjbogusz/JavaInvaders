package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Player;

/**
 * {@link Player}'s model DTO class
 */
public class PlayerDTO {
	/**
	 * Player's health.
	 */
	public final int health;
	/**
	 * Player's width.
	 */
	public final double width;
	/**
	 * Player's height.
	 */
	public final double height;
	/**
	 * Player's horizontal position.
	 */
	public final double position;
	/**
	 * Player's vertical position.
	 */
	public final double positionTop;

	/**
	 * Player DTO class constructor.
	 * @param player {@link Player} object to copy data from.
	 */
	public PlayerDTO(Player player) {
		this.health = player.getHealth();
		this.width = Player.width;
		this.height = Player.height;
		this.position = player.getPosition();
		this.positionTop = Player.positionTop;
	}

	/**
	 * Retrieve player's health.
	 * @return player's health.
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Retrieve player's width.
	 * @return player's width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Retrieve player's height.
	 * @return player's height.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Retrieve player's horizontal position.
	 * @return player's horizontal position.
	 */
	public double getPosition() {
		return position;
	}

	/**
	 * Retrieve player's vertical position.
	 * @return player's vertical position.
	 */
	public double getPositionTop() {
		return positionTop;
	}
}

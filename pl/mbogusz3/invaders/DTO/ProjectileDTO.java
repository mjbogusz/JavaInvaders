package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Projectile;

/**
 * {@link Projectile}'s model DTO class
 */
public class ProjectileDTO {
	/**
	 * Projectile's orientation.
	 */
	public final int orientation;
	/**
	 * Projectile's horizontal position.
	 */
	public final double positionX;
	/**
	 * Projectile's vertical position.
	 */
	public final double positionY;
	/**
	 * Projectile's width.
	 */
	public final double width;
	/**
	 * Projectile's height.
	 */
	public final double height;

	/**
	 * Projectile DTO class constructor.
	 * @param projectile {@link Projectile} object to copy data from.
	 */
	public ProjectileDTO(Projectile projectile) {
		this.orientation = projectile.getOrientation();
		this.positionX = projectile.getPositionX();
		this.positionY = projectile.getPositionY();
		this.width = Projectile.width;
		this.height = Projectile.height;
	}

	/**
	 * Retrieve projectile's orientation.
	 * @return projectile's orientation.
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Retrieve projectile's horizontal position.
	 * @return projectile's horizontal position.
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Retrieve projectile's vertical position.
	 * @return projectile's vertical position.
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Retrieve projectile's width.
	 * @return projectile's width.
	 */
	public double getWidth() {
		return width;
	}

	/**
	 * Retrieve projectile's height.
	 * @return projectile's height.
	 */
	public double getHeight() {
		return height;
	}
}

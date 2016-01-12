package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Projectile;

/**
 */
public class ProjectileDTO {
	public final int orientation;
	public final double positionX;
	public final double positionY;
	public final double width;
	public final double height;

	public ProjectileDTO(Projectile projectile) {
		this.orientation = projectile.getOrientation();
		this.positionX = projectile.getPositionX();
		this.positionY = projectile.getPositionY();
		this.width = Projectile.width;
		this.height = Projectile.height;
	}

	public int getOrientation() {
		return orientation;
	}

	public double getPositionX() {
		return positionX;
	}

	public double getPositionY() {
		return positionY;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
}

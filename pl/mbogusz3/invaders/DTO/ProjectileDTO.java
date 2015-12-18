package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Projectile;

/**
 */
public class ProjectileDTO {
	public final int orientation;
	public final double positionX;
	public final double positionY;

	public ProjectileDTO(Projectile projectile) {
		this.orientation = projectile.getOrientation();
		this.positionX = projectile.getPositionX();
		this.positionY = projectile.getPositionY();
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
}
package pl.mbogusz3.invaders.model;

/**
 */
public class Projectile {
	private final static double speed = 0.5;
	/**
	 * 1 is down, -1 is up
	 */
	private final int orientation;
	private final double positionX;
	private double positionY;
	private boolean invalid;

	public Projectile(double positionX, double positionY) {
		this(positionX, positionY, -1);
	}

	public Projectile(double positionX, double positionY, int orientation) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.orientation = orientation;
		this.invalid = false;
	}

	public void move(double time) {
		if(this.invalid) {
			return;
		}
		this.positionY += speed * time * this.orientation;
		if(this.positionY > 1.0) {
			this.positionY = 1.0;
			this.invalid = true;
		} else if(this.positionY < 0.0) {
			this.positionY = 0.0;
			this.invalid = true;
		}
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

	public boolean isInvalid() {
		return invalid;
	}
}

package pl.mbogusz3.invaders.model;

/**
 */
public class Projectile {
	public final static double defaultSpeed = 0.5;
	/**
	 * 1 is down, -1 is up - consistent with Y axis direction (increasing from top to bottom)
	 */
	private final int orientation;
	private final double positionX;
	private final double speed;
	private double positionY;
	private boolean invalid;

	public Projectile(double positionX, double positionY) {
		this(positionX, positionY, -1);
	}

	public Projectile(double positionX, double positionY, int orientation) {
		this(positionX, positionY, orientation, Projectile.defaultSpeed);
	}

	public Projectile(double positionX, double positionY, double speed) {
		this(positionX, positionY, -1, speed);
	}

	public Projectile(double positionX, double positionY, int orientation, double speed) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.orientation = orientation;
		this.speed = speed;
		this.invalid = false;
	}

	public void move(double time) {
		if(this.invalid) {
			return;
		}
		this.positionY += this.speed * time * this.orientation;
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

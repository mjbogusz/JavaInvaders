package pl.mbogusz3.invaders.model;

/**
 * Invader's projectile model (universal - both player and enemy).
 */
public class Projectile {
	/**
	 * Projectile's default speed.
	 */
	public final static double defaultSpeed = 0.5;
	/**
	 * Projectile's width.
	 */
	public final static double width = 0.02;
	/**
	 * Projectile's height.
	 */
	public final static double height = 0.02;
	/**
	 * Projectile's orientation.
	 * 1 is down, -1 is up - consistent with Y axis direction (increasing from top to bottom).
	 */
	private final int orientation;
	/**
	 * Projectile's horizontal position.
	 */
	private final double positionX;
	/**
	 * Projectile's speed, defaults to {@link #defaultSpeed}.
	 */
	private final double speed;
	/**
	 * Projectile's current vertical position.
	 */
	private double positionY;
	/**
	 * Whether the projectile is invalid (i.e. has flown out of range not hitting anything).
	 */
	private boolean invalid;

	/**
	 * Projectile's constructor, defaulting {@link #orientation} to -1 (up - player's projectile) and {@link #speed} to {@link #defaultSpeed}.
	 * @param positionX horizontal position of the projectile.
	 * @param positionY initial vertical position of the projectile.
	 */
	public Projectile(double positionX, double positionY) {
		this(positionX, positionY, -1);
	}

	/**
	 * Projectile's constructor, {@link #speed} to {@link #defaultSpeed}.
	 * @param positionX horizontal position of the projectile.
	 * @param positionY initial vertical position of the projectile.
	 * @param orientation orientation of the projectile (1 is down, -1 is up, see {@link #orientation}).
	 */
	public Projectile(double positionX, double positionY, int orientation) {
		this(positionX, positionY, orientation, Projectile.defaultSpeed);
	}

	/**
	 * Projectile's constructor, defaulting {@link #orientation} to -1 (up - player's projectile).
	 * @param positionX horizontal position of the projectile.
	 * @param positionY initial vertical position of the projectile.
	 * @param speed speed of the projectile.
	 */
	public Projectile(double positionX, double positionY, double speed) {
		this(positionX, positionY, -1, speed);
	}

	/**
	 * Projectile's constructor.
	 * @param positionX horizontal position of the projectile.
	 * @param positionY initial vertical position of the projectile.
	 * @param orientation orientation of the projectile (1 is down, -1 is up, see {@link #orientation}).
	 * @param speed speed of the projectile.
	 */
	public Projectile(double positionX, double positionY, int orientation, double speed) {
		this.positionX = positionX;
		this.positionY = positionY;
		this.orientation = orientation;
		this.speed = speed;
		this.invalid = false;
	}

	/**
	 * Move the projectile.
	 * @param time time for which the projectile has moved.
	 */
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

	/**
	 * Destroy the projectile.
	 */
	public void destroy() {
		this.invalid = true;
	}

	/**
	 * Retrieve orientation of the projectile.
	 * @return projectile's orientation (1 is down, -1 is up).
	 */
	public int getOrientation() {
		return orientation;
	}

	/**
	 * Retrieve horizontal position of the projectile.
	 * @return projectile's horizontal position.
	 */
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Retrieve vertical position of the projectile.
	 * @return projectile's vertical position.
	 */
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Retrieve whether the projectile is invalid.
	 * @return whether the projectile is invalid.
	 */
	public boolean isInvalid() {
		return invalid;
	}
}

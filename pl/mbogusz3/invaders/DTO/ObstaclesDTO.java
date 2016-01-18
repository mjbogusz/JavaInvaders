package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Obstacles;

/**
 * {@link Obstacles}' model DTO class
 */
public class ObstaclesDTO {
	/**
	 * Vertical position of obstacles.
	 */
	public final double positionTop;
	/**
	 * Height of obstacles.
	 */
	public final double height;
	/**
	 * Count of obstacles.
	 */
	public final int count;
	/**
	 * State of obstacles.
	 */
	public final int[] state;

	/**
	 * Obstacles DTO class constructor.
	 * @param obstacles {@link Obstacles} object to copy data from.
	 */
	public ObstaclesDTO(Obstacles obstacles) {
		this.positionTop = Obstacles.positionTop;
		this.height = Obstacles.height;
		this.count = obstacles.getCount();
		this.state = new int[this.count];
		System.arraycopy(obstacles.getState(), 0, this.state, 0, this.count);
	}

	/**
	 * Retrieve vertical position of obstacles.
	 * @return vertical position of obstacles.
	 */
	public double getPositionTop() {
		return positionTop;
	}

	/**
	 * Retrieve height of obstacles.
	 * @return height of obstacles.
	 */
	public double getHeight() {
		return height;
	}

	/**
	 * Retrieve count of obstacles.
	 * @return count of obstacles.
	 */
	public int getCount() {
		return count;
	}

	/**
	 * Retrieve state of obstacles.
	 * @return state of obstacles.
	 */
	public int[] getState() {
		return state;
	}
}

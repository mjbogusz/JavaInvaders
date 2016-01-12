package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Obstacles;

/**
 */
public class ObstaclesDTO {
	public final int count;
	public final int[] state;

	public ObstaclesDTO(Obstacles obstacles) {
		this.count = obstacles.getCount();
		this.state = new int[this.count];
		System.arraycopy(obstacles.getState(), 0, this.state, 0, this.count);
	}

	public int getCount() {
		return count;
	}

	public int[] getState() {
		return state;
	}
}

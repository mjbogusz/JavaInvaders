package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Obstacles;

/**
 */
public class ObstaclesDTO {
	public final int count;
	public final int strength;
	public final int[] state;

	public ObstaclesDTO(Obstacles obstacles) {
		this.count = obstacles.getCount();
		this.strength = obstacles.getStrength();
		this.state = obstacles.getState();
	}

	public int getCount() {
		return count;
	}

	public int getStrength() {
		return strength;
	}

	public int[] getState() {
		return state;
	}
}

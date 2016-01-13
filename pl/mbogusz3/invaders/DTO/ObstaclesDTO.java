package pl.mbogusz3.invaders.DTO;

import pl.mbogusz3.invaders.model.Obstacles;

/**
 */
public class ObstaclesDTO {
	public final double positionTop;
	public final double height;
	public final int count;
	public final int[] state;

	public ObstaclesDTO(Obstacles obstacles) {
		this.positionTop = Obstacles.positionTop;
		this.height = Obstacles.height;
		this.count = obstacles.getCount();
		this.state = new int[this.count];
		System.arraycopy(obstacles.getState(), 0, this.state, 0, this.count);
	}

	public double getPositionTop() {
		return positionTop;
	}

	public double getHeight() {
		return height;
	}

	public int getCount() {
		return count;
	}

	public int[] getState() {
		return state;
	}
}

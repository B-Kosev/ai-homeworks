package homework3;

public class Chromosome {
	private final City[] path;
	private double fitness = -1;

	public Chromosome(City[] path) {
		this.path = new City[path.length];
		System.arraycopy(path, 0, this.path, 0, path.length);
		this.fitness = calculateFitness();
	}

	public City[] getPath() {
		return path;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	private double calculateFitness() {
		double distance = 0.0;
		for (int i = 0; i < path.length; i++) {
			if (i + 1 == path.length)
				distance += calculateDistance(path[i].getX(), path[i].getY(), path[0].getX(), path[0].getY());
			else
				distance += calculateDistance(path[i].getX(), path[i].getY(), path[i + 1].getX(), path[i + 1].getY());
		}
		return distance;
	}

	private double calculateDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

}

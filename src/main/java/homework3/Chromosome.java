package main.java.homework3;

import java.util.List;

public class Chromosome implements Comparable<Chromosome> {
	private int[] path;
	private double fitness = -1;

	public Chromosome(int n) {
		this.path = new int[n];
		for (int i = 0; i < n; i++) {
			path[i] = -1;
		}
	}

	public int[] getPath() {
		return path;
	}

	public void setPath(int[] path) {
		this.path = path;
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	/**
	 * Calculates the fitness of the current individual
	 * 
	 * @param cities
	 *            - the list with the citis
	 */
	public void calculateFitness(List<City> cities) {
		double distance = 0.0;

		for (int i = 0; i < path.length - 1; i++) {
			distance += calculateDistance(cities.get(path[i]).getX(), cities.get(path[i]).getY(), cities.get(path[i + 1]).getX(),
					cities.get(path[i + 1]).getY());
		}
		setFitness(distance);
	}

	/**
	 * Calculate the distance between two points
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the calculated distance
	 */
	private double calculateDistance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	@Override
	public int compareTo(Chromosome o) {
		return Double.compare(this.getFitness(), o.getFitness());
	}
}

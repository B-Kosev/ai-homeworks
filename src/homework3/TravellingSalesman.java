package homework3;

import java.util.*;

public class TravellingSalesman {
	private static List<City> cities;

	private static PriorityQueue<Chromosome> currentGeneration = new PriorityQueue<>(Collections.reverseOrder());
	private static PriorityQueue<Chromosome> nextGeneration = new PriorityQueue<>(Collections.reverseOrder());

	private static void initializeCities(int n) {
		cities = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			City city = new City(i, getRandomNumberBetween(0.0, 100.0), getRandomNumberBetween(0.0, 100.0));
			cities.add(city);
		}
	}

	private static void reproduce() {

	}

	private static void createNewGeneration() {

	}

	private static void solve() {
		int iter = 0, generation = 20;
		// Generate random population

		while (iter++ < generation) {

			reproduce();

			createNewGeneration();

		}
	}

	private static double getRandomNumberBetween(double lowerBound, double upperBound) {
		Random random = new Random();
		return lowerBound + (upperBound - lowerBound) * random.nextDouble();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		int n = scanner.nextInt();
		if (n > 100)
			return;

		initializeCities(n);

		solve();

		City[] arr = cities.toArray(new City[0]);

		Chromosome chromosome = new Chromosome(arr);

		// System.out.println(calculateDistance(cities.get(0).getX(), cities.get(0).getY(), cities.get(1).getX(), cities.get(1).getY()));
	}
}

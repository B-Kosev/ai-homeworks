package homework3;

import java.util.*;

public class TravellingSalesman {
	private static int n;
	private static final Random random = new Random();
	private static List<City> cities;

	private static PriorityQueue<Chromosome> currentGeneration = new PriorityQueue<>(Collections.reverseOrder());
	private static final PriorityQueue<Chromosome> nextGeneration = new PriorityQueue<>(Collections.reverseOrder());

	private static void initializeCities(int n) {
		cities = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			City city = new City(i, getRandomNumberBetween(0.0, 100.0), getRandomNumberBetween(0.0, 100.0));
			cities.add(city);
		}
	}

	private static void reproduce() {
		Chromosome parent1, parent2;
		int size = currentGeneration.size();

		while (currentGeneration.size() > size / 2) {
			parent1 = currentGeneration.remove();
			parent2 = currentGeneration.remove();

			crossover(parent1, parent2);

			nextGeneration.add(parent1);
			nextGeneration.add(parent2);
		}
	}

	private static void crossover(Chromosome parent1, Chromosome parent2) {
		Chromosome child1 = new Chromosome(n);
		Chromosome child2 = new Chromosome(n);

		int slice = random.nextInt(cities.size());

		// Copy from 0 to slice genes from the parent into the child
		for (int i = 0; i < slice; i++) {
			child1.getPath()[i] = parent1.getPath()[i];
			child2.getPath()[i] = parent2.getPath()[i];
		}

		// Crossover the rest
		addRemainingGenes(parent2, child1, slice);
		addRemainingGenes(parent1, child2, slice);

		// Mutation
		mutate(child1);
		mutate(child2);

		// Calculate fitness
		child1.calculateFitness(cities);
		child2.calculateFitness(cities);

		// Add to new generation
		nextGeneration.add(child1);
		nextGeneration.add(child2);
	}

	private static void mutate(Chromosome chromosome) {
		int gene1 = random.nextInt(cities.size());
		int gene2 = random.nextInt(cities.size());

		int temp = chromosome.getPath()[gene1];
		chromosome.getPath()[gene1] = chromosome.getPath()[gene2];
		chromosome.getPath()[gene2] = temp;
	}

	private static void addRemainingGenes(Chromosome parent, Chromosome child, int slice) {
		// Iterator for the parent genes
		int k = 0;
		// Is the current gene found in the children
		boolean isThere;
		// The current gene
		int currentGene = -1;

		// Start from the first empty position in the genome
		for (int i = slice; i < n; i++) {
			isThere = true;
			while (isThere) {
				currentGene = parent.getPath()[k++];
				isThere = false;

				for (int j = 0; j < n; j++) {
					if (child.getPath()[i] == currentGene) {
						isThere = true;
						break;
					}
				}
			}
			child.getPath()[i] = currentGene;
		}
	}

	private static void createNewGeneration() {
		currentGeneration = nextGeneration;
		nextGeneration.clear();
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
		return lowerBound + (upperBound - lowerBound) * random.nextDouble();
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		n = scanner.nextInt();
		if (n > 100)
			return;

		initializeCities(n);

		solve();

		// System.out.println(calculateDistance(cities.get(0).getX(), cities.get(0).getY(), cities.get(1).getX(), cities.get(1).getY()));
	}
}

package homework3;

import java.util.*;

public class TravellingSalesman {
	private static int n;
	private static final int POPULATION_SIZE = 100;
	private static final Random random = new Random();
	private static List<City> cities;

	private static final PriorityQueue<Chromosome> currentGeneration = new PriorityQueue<>();
	private static final PriorityQueue<Chromosome> nextGeneration = new PriorityQueue<>();

	private static void initializeCities(int n) {
		cities = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			City city = new City(i, getRandomNumberBetween(0.0, 100.0), getRandomNumberBetween(0.0, 100.0));
			cities.add(city);
		}
	}

	private static void generateRandomPopulation() {
		List<Integer> arrList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			arrList.add(i);
		}

		for (int i = 0; i < POPULATION_SIZE; i++) {
			Chromosome c = new Chromosome(n);
			Collections.shuffle(arrList);
			c.setPath(arrList.stream().mapToInt(Integer::intValue).toArray());
			c.calculateFitness(cities);
			currentGeneration.add(c);
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

		int slice = random.nextInt(n);

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
		int gene1 = random.nextInt(n);
		int gene2 = random.nextInt(n);

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
			// Iterate parent genes
			for (int j = 0; j < n; j++) {
				currentGene = parent.getPath()[j];
				isThere = false;

				// Check if this gene is already present from the other parent
				for (int l = 0; l < i; l++) {
					if (currentGene == child.getPath()[l]) {
						isThere = true;
						break;
					}
				}
				if (!isThere) {
					child.getPath()[i] = currentGene;
					break;
				}
			}
		}
	}

	private static void createNewGeneration() {
		currentGeneration.clear();
		currentGeneration.addAll(nextGeneration);
		nextGeneration.clear();
	}

	private static void solve() {
		int iter = 1, generations = 100;
		// Generate random population
		generateRandomPopulation();

		while (iter <= generations) {
			if (iter == 1 || iter % 10 == 0) {
				Chromosome best = currentGeneration.peek();
				System.out.printf("Best for generation %d with distance %f and path %s.\n", iter, best.getFitness(),
						Arrays.toString(best.getPath()));
			}

			reproduce();

			createNewGeneration();

			iter++;
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

		long startTime = System.currentTimeMillis();

		initializeCities(n);

		solve();

		long endTime = System.currentTimeMillis();

		System.out.printf("Algorithm finished in %f seconds.", (endTime - startTime) / 1000.0);
	}
}

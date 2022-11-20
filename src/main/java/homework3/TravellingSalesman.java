package main.java.homework3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

// Github: https://github.com/B-Kosev/ai-homeworks

public class TravellingSalesman {
	private static int n;
	private static final int POPULATION_SIZE = 600;
	private static final int GENERATIONS = 100;
	private static final Random random = new Random();
	private static final List<City> cities = new ArrayList<>();
	// Analytics
	private static int seqDraws = 0;
	private static int randomDraws = 0;

	private static final PriorityQueue<Individual> currentGeneration = new PriorityQueue<>();
	private static final PriorityQueue<Individual> nextGeneration = new PriorityQueue<>();

	/**
	 * Creates the cities objects from the csv entries
	 * 
	 * @param testCities
	 *            - the csv entries
	 */
	private static void initializeCities(List<String[]> testCities) {
		int i = 0;
		for (String[] row : testCities) {
			City city = new City(i++, Double.parseDouble(row[0]), Double.parseDouble(row[1]), row[2]);
			cities.add(city);
		}
	}

	/**
	 * Creates the cities objects with random coordinates and no names
	 * 
	 * @param n
	 *            - number of cities
	 */
	private static void initializeRandomCities(int n) {
		for (int i = 0; i < n; i++) {
			City city = new City(i, getRandomNumberBetween(0.0, 100.0), getRandomNumberBetween(0.0, 100.0), "");
			cities.add(city);
		}
	}

	/**
	 * Generates the first population randomly
	 */
	private static void generateRandomPopulation() {
		List<Integer> arrList = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			arrList.add(i);
		}

		for (int i = 0; i < POPULATION_SIZE; i++) {
			Individual individual = new Individual(n);
			Collections.shuffle(arrList);
			individual.setPath(arrList.stream().mapToInt(Integer::intValue).toArray());
			individual.calculateFitness(cities);
			currentGeneration.add(individual);
		}
	}

	/**
	 * The main reproducing method. Gets the best parents and crossovers them. Then adds the parents to the next generation.
	 */
	private static void reproduce() {
		Individual parent1, parent2;
		int size = currentGeneration.size();

		List<Individual> parents = new ArrayList<>();

		while (currentGeneration.size() > size / 2) {
			parents.add(currentGeneration.remove());
		}

		while (!parents.isEmpty()) {
			if (parents.size() == 1) {
				break;
			}

			// Draw parents sequentially with 75% probability, and draw randomly with 25% probability
			if (getRandomNumberBetween(1, 101) <= 75) {
				// Draw sequentially
				parent1 = parents.remove(0);
				parent2 = parents.remove(0);
				seqDraws++;

			} else {
				// Draw randomly
				parent1 = parents.remove(getRandomNumberBetween(0, parents.size() - 1));
				parent2 = parents.remove(getRandomNumberBetween(0, parents.size() - 1));
				randomDraws++;

			}

			crossover(parent1, parent2);

			nextGeneration.add(parent1);
			nextGeneration.add(parent2);
		}
	}

	/**
	 * The main crossover function. Creates children from the parents using the One-Point Crossover algorithm. Then performs mutation and
	 * adds the children to the new generation.
	 * 
	 * @param parent1
	 *            - the first parent
	 * @param parent2
	 *            - the second parent
	 */
	private static void crossover(Individual parent1, Individual parent2) {
		Individual child1 = new Individual(n);
		Individual child2 = new Individual(n);

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

	/**
	 * Randomly swaps two genes
	 * 
	 * @param individual
	 *            - the individual
	 */
	private static void mutate(Individual individual) {
		int gene1 = random.nextInt(n);
		int gene2 = random.nextInt(n);

		int temp = individual.getPath()[gene1];
		individual.getPath()[gene1] = individual.getPath()[gene2];
		individual.getPath()[gene2] = temp;
	}

	/**
	 * After getting the genes from the first parent, adds the missing genes from the second parent.
	 * 
	 * @param parent
	 *            - the second parent
	 * @param child
	 *            - the child with the genes from the first parent
	 * @param slice
	 *            - the slicing index
	 */
	private static void addRemainingGenes(Individual parent, Individual child, int slice) {
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

	/**
	 * Creates the new generation
	 */
	private static void createNewGeneration() {
		currentGeneration.clear();
		currentGeneration.addAll(nextGeneration);
		nextGeneration.clear();
	}

	/**
	 * The main driver method. Calls the function creating the first population and then iterates over generations.
	 */
	private static void solve() {
		int iter = 1;
		// Generate random population
		generateRandomPopulation();

		while (iter <= GENERATIONS) {
			// Printing
			if (iter == 1 || iter % 10 == 0) {
				Individual best = currentGeneration.peek();
				int[] path = best.getPath();
				System.out.printf("Best for generation %d with distance %f and path %s.\n", iter, best.getFitness(), Arrays.toString(path));

				if (iter == 100) {
					for (int j : path) {
						System.out.println(cities.get(j));
					}
				}

			}

			reproduce();

			createNewGeneration();

			iter++;
		}
	}

	/**
	 * Generates a random number
	 * 
	 * @param lowerBound
	 *            - the lower bound
	 * @param upperBound
	 *            - the upper bound
	 * @return the random number
	 */
	private static double getRandomNumberBetween(double lowerBound, double upperBound) {
		return lowerBound + (upperBound - lowerBound) * random.nextDouble();
	}

	/**
	 * Generates a random number
	 *
	 * @param lowerBound
	 *            - the lower bound
	 * @param upperBound
	 *            - the upper bound
	 * @return the random number
	 */
	private static int getRandomNumberBetween(int lowerBound, int upperBound) {
		return lowerBound + random.nextInt(upperBound - lowerBound + 1);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		n = scanner.nextInt();
		if (n > 100)
			return;

		// Read from CSV
		BufferedReader reader = null;
		List<String[]> testCities = new ArrayList<>();
		try {
			File file = new File("D:\\SI - 4 KURS\\AI\\Homeworks\\AI-Homeworks\\src\\main\\resources\\uk12_xy_names.csv");
			reader = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				testCities.add(row);
			}
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println("Exception: " + e);
			}
		}

		initializeCities(testCities);
		// System.out.println(cities);

		// initializeRandomCities(n);

		long startTime = System.currentTimeMillis();

		solve();

		long endTime = System.currentTimeMillis();

		System.out.printf("Algorithm finished in %f seconds.\n", (endTime - startTime) / 1000.0);

		System.out.println(seqDraws + " sequential draws and " + randomDraws + " random draws.");
	}
}

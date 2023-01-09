package main.java.homework5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DemocratsAndRepublicans {
	private static final List<String[]> entries = new ArrayList<>();
	private static final List<List<String[]>> dataSets = new ArrayList<>();

	private static int[][] republicans = new int[3][16];
	private static int[][] democrats = new int[3][16];

	private static int repCounter = 0;
	private static int demCounter = 0;

	/**
	 * Fills the tables with the given dataset
	 * 
	 * @param dataSet
	 *            - the dataset to be processed. One of the 10 folds
	 */
	public static void transformData(List<String[]> dataSet) {
		// Refresh fields for each dataset

		for (String[] row : dataSet) {
			boolean isRepublican = row[0].equals("republican");
			if (isRepublican) {
				repCounter++;
			} else {
				demCounter++;
			}

			for (int i = 1; i < 17; i++) {
				switch (row[i]) {
				case "y":
					if (isRepublican) {
						republicans[0][i - 1]++;
					} else {
						democrats[0][i - 1]++;
					}
					break;
				case "n":
					if (isRepublican) {
						republicans[1][i - 1]++;
					} else {
						democrats[1][i - 1]++;
					}
					break;
				case "?":
					if (isRepublican) {
						republicans[2][i - 1]++;
					} else {
						democrats[2][i - 1]++;
					}
					break;
				}
			}
		}
	}

	/**
	 * Test the given dataset
	 * 
	 * @param testSet
	 *            - the dataset to be tested. One of the 10 folds
	 * @return the accuracy of the classifier
	 */
	private static double testSet(List<String[]> testSet) {
		int lambda = 1;
		int correct = 0;
		for (String[] row : testSet) {
			boolean isRepublican = row[0].equals("republican");
			double pRepublican = 0;
			double pDemocrat = 0;

			for (int i = 1; i < 17; i++) {
				switch (row[i]) {
				case "y" -> {
					pRepublican += Math.log((republicans[0][i - 1] + lambda) / (double) (repCounter + 2 * lambda));
					pDemocrat += Math.log((democrats[0][i - 1] + lambda) / (double) (demCounter + 2 * lambda));
				}
				case "n" -> {
					pRepublican += Math.log((republicans[1][i - 1] + lambda) / (double) (repCounter + 2 * lambda));
					pDemocrat += Math.log((democrats[1][i - 1] + lambda) / (double) (demCounter + 2 * lambda));
				}
				case "?" -> {
					pRepublican += Math.log((republicans[2][i - 1] + lambda) / (double) (repCounter + 2 * lambda));
					pDemocrat += Math.log((democrats[2][i - 1] + lambda) / (double) (demCounter + 2 * lambda));
				}
				}
			}
			pRepublican += Math.log((repCounter) / (double) (repCounter + demCounter));
			pDemocrat += Math.log((demCounter) / (double) (repCounter + demCounter));

			if (isRepublican && pRepublican > pDemocrat)
				correct++;
			else if (!isRepublican && pDemocrat > pRepublican)
				correct++;
		}

		double accuracy = correct / (double) testSet.size() * 100;
		System.out.printf("Correct: %d/43 -> %f%%\n", correct, accuracy);
		return accuracy;
	}

	/**
	 * Utility function to count the distribution of the data
	 * 
	 * @param testSet
	 *            - the dataset to be counted
	 */
	private static void countEntries(List<String[]> testSet) {
		long reps = testSet.stream().filter(row -> row[0].equals("republican")).count();
		System.out.printf("Republicans: %d, Democrats: %d\n", reps, testSet.size() - reps);
	}

	private static int getRandomNumberBetween(int lowerBound, int upperBound) {
		Random random = new Random();
		return lowerBound + random.nextInt(upperBound - lowerBound + 1);
	}

	public static void main(String[] args) {
		// Read from CSV
		BufferedReader reader = null;
		try {
			File file = new File("D:\\SI - 4 KURS\\AI\\Homeworks\\AI-Homeworks\\src\\main\\resources\\house-votes-84.data");
			reader = new BufferedReader(new FileReader(file));
			String line = "";

			while ((line = reader.readLine()) != null) {
				String[] row = line.split(",");
				entries.add(row);
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

		// Split the data into 10 sets
		int chunkSize = entries.size() / 10;
		for (int i = 0; i < 10; i++) {
			List<String[]> testSet = new ArrayList<>();
			for (int j = 0; j < chunkSize; j++) {
				testSet.add(entries.remove(getRandomNumberBetween(0, entries.size() - 1)));
			}
			dataSets.add(testSet);
			countEntries(testSet);
		}

		// Training and testing
		int testIndex = 0;
		double overallAccuracy = 0;
		for (int i = 0; i < 10; i++) {
			// Populate tables with data
			for (int j = 0; j < 10; j++) {
				if (j == testIndex)
					continue;

				transformData(dataSets.get(j));
			}

			// Test
			overallAccuracy += testSet(dataSets.get(testIndex));

			testIndex++;

			// Refresh fields for the new iteration
			republicans = new int[3][16];
			democrats = new int[3][16];

			repCounter = 0;
			demCounter = 0;
		}

		System.out.printf("\nOverall accuracy: %f%%", overallAccuracy / 10);
	}
}

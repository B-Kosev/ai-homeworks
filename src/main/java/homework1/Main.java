package main.java.homework1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
	/**
	 * Performs the deepening search with each threshold until a solution is found or no such is found
	 * 
	 * @param root
	 *            - the initial board node
	 */
	private static List<String> search(Node root) {
		// Initial f score
		int threshold = root.getfScore();

		// List of the nodes that form the current path from the root
		List<Node> currentPath = new ArrayList<>();
		currentPath.add(root);

		// List with the steps leading to the solution
		List<String> steps = new ArrayList<>();

		int minThreshold;
		do {
			// Returns the minimum threshold, bigger than the current or Integer.MAX_VALUE if no there is no solution
			minThreshold = recursiveSearch(currentPath, 0, threshold, steps);

			// If the goal is found
			if (minThreshold == 0) {
				return steps;
			}

			threshold = minThreshold;
		} while (threshold != Integer.MAX_VALUE);
		System.out.println("No solution found!");
		return null;
	}

	/**
	 * Explores all children that have fScore <= threshold
	 * 
	 * @param currentPath
	 *            - list of the nodes in the path from the root
	 * @param cost
	 *            - the cost to the current node
	 * @param threshold
	 *            - the current fScore
	 * @param steps
	 *            - the list with steps to the current node
	 * @return 0 if the solution is found, Integer.MAX_VALUE if there is no solution, the smallest fScore which is higher than the current
	 *         threshold
	 */
	private static int recursiveSearch(List<Node> currentPath, int cost, double threshold, List<String> steps) {
		Node current = currentPath.get(currentPath.size() - 1);
		current.sethScore(current.getBoard().manhattanDistance());
		current.setgScore(cost);
		current.setfScore(current.gethScore() + cost);

		// If the current node has fScore larger than the given threshold, don't expand
		if (current.getfScore() > threshold) {
			return current.getfScore();
		}

		// If current board is the solution return 0 to exit recursion
		if (current.getBoard().isSolved()) {
			return 0;
		}

		// The minimum fScore to be found, greater than the current threshold
		int minNewFscore = Integer.MAX_VALUE;
		// Calculate neighbours for the current node
		Map<Board, String> neighbours = current.getBoard().neighbours();

		// Iterate over all neighbours
		for (Map.Entry<Board, String> entry : neighbours.entrySet()) {
			// gScore for every new node is current gScore + 1 as it costs one move to get to the new board
			Node node = new Node(entry.getKey(), current.getgScore() + 1);

			// Don't expand nodes on the current path - avoid loops (e.g. A -> B -> A)
			if (!currentPath.contains(node)) {
				currentPath.add(node);
				steps.add(entry.getValue());

				// Call recursively until goal is reached or all children with fScore > threshold are visited
				int minFscoreOverThreshold = recursiveSearch(currentPath, node.getgScore(), threshold, steps);

				// If solution is found exit recursion
				if (minFscoreOverThreshold == 0) {
					return 0;
				}

				// Set the new threshold to be the minimum of all greater than the current one
				if (minFscoreOverThreshold < minNewFscore) {
					minNewFscore = minFscoreOverThreshold;
				}

				// Remove the current node from the path as it is not part of the solution
				currentPath.remove(currentPath.size() - 1);
				steps.remove(steps.size() - 1);
			}
		}
		return minNewFscore;
	}

	/**
	 * Check whether a given matrix is solvable. Counts the numbers of inversion and the row of the blank tile. Inversion is formed when A <
	 * B but B appears before A in the matrix.
	 * 
	 * @param array
	 *            - the matrix transformed to 1D array
	 * @param rowSize
	 *            - the number of rows
	 * @return true if the puzzle is solvable, false otherwise
	 */
	private static boolean isSolvable(int[] array, int rowSize) {
		int inversions = 0;
		int currentRow = 0;
		int rowWithBlank = 0;

		for (int i = 0; i < array.length; i++) {
			// Advance to next row
			if (i % rowSize == 0) {
				currentRow++;
			}
			// Save the row of the blank tile
			if (array[i] == 0) {
				rowWithBlank = currentRow;
				continue;
			}
			// Count inversions
			for (int j = i + 1; j < array.length; j++) {
				if (array[i] > array[j] && array[j] != 0) {
					inversions++;
				}
			}
		}

		// If the grid is even we count if the blank tile is on odd row
		if (rowSize % 2 == 0) {
			// If the blank is on even row, counting from the bottom, we need even number of inversions
			if (rowWithBlank % 2 == 0) {
				return inversions % 2 == 0;

			}
			// If the blank is on odd row, we need odd number of inversions
			else {
				return inversions % 2 != 0;
			}
		}
		// If the grid is odd, we need even number of inversions
		else {
			return inversions % 2 == 0;
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Input for n and the zero index
		int n = scanner.nextInt();
		int zeroIndex = scanner.nextInt();

		// Initialize the starting board
		int rowSize = (int) Math.sqrt(n + 1);
		int[][] startingTiles = new int[rowSize][rowSize];

		// Populate the starting and goal board
		for (int i = 0; i < rowSize; i++) {
			for (int j = 0; j < rowSize; j++) {
				if (zeroIndex == rowSize * i + j) {
					continue;
				}
				startingTiles[i][j] = scanner.nextInt();
			}
		}

		Board board = new Board(rowSize, startingTiles);

		System.out.println(board);

		// Solving...
		long startTime = System.currentTimeMillis();

		// Convert the 2D matrix to 1D array
		int[] array = Stream.of(board.getTiles()).flatMapToInt(IntStream::of).toArray();
		if (!isSolvable(array, rowSize)) {
			System.out.println("The puzzle is not solvable.");
			return;
		}

		List<String> steps = search(new Node(board, 0));
		long endTime = System.currentTimeMillis();

		if (steps != null) {
			System.out.println(steps.size());
			steps.forEach(System.out::println);
		} else {
			System.out.println("No path found.");
		}

		System.out.println((endTime - startTime) / 1000.0);
	}

}

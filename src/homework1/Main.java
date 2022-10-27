package homework1;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
	/**
	 * Performs the deepening search with each threshold until a solution is found or no such is found
	 * 
	 * @param root
	 *            - the initial board node
	 */
	private static void search(Node root) {
		// Initial f score
		int threshold = root.getfScore();

		List<Node> visited = new ArrayList<>();
		visited.add(root);
		List<String> steps = new ArrayList<>();

		int minThreshold;
		do {
			System.out.println("Threshold: " + threshold);
			// Returns the minimum threshold, bigger than the current or Integer.MAX_VALUE if no there is no solution
			minThreshold = recursiveSearch(visited, 0, threshold, steps);

			// If the goal is found
			if (minThreshold == 0) {
				System.out.println(steps.size());
				steps.forEach(System.out::println);
				return;
			}

			threshold = minThreshold;
		} while (threshold != Integer.MAX_VALUE);
		System.out.println("No solution found!");
	}

	/**
	 * Explores all children that have fScore <= threshold
	 * 
	 * @param visited
	 *            - list of visited nodes
	 * @param cost
	 *            - the cost to the current node
	 * @param threshold
	 *            - the current fScore
	 * @param steps
	 *            - the list with steps to the current node
	 * @return 0 if the solution is found, Integer.MAX_VALUE if there is no solution, the smallest fScore which is higher than the current
	 *         threshold
	 */
	private static int recursiveSearch(List<Node> visited, int cost, double threshold, List<String> steps) {
		Node current = visited.get(visited.size() - 1);
		current.sethScore(current.getBoard().manhattanDistance());
		current.setgScore(cost);
		current.setfScore(current.gethScore() + cost);

		// If the current node has fScore larger than the given threshold, return
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

			// Optimization - skip visited nodes
			if (!visited.contains(node)) {
				visited.add(node);
				steps.add(entry.getValue());

				// Call recursively until goal is reached or all children with fScore > threshold are visited
				int minFscoreOverThreshold = recursiveSearch(visited, node.getgScore(), threshold, steps);

				// If solution is found exit recursion
				if (minFscoreOverThreshold == 0) {
					return 0;
				}

				// Set the new threshold to be the minimum of all greater than the current one
				if (minFscoreOverThreshold < minNewFscore) {
					minNewFscore = minFscoreOverThreshold;
				}

				// Remove the current node as it is visited
				visited.remove(visited.size() - 1);
				steps.remove(steps.size() - 1);
			}
		}
		return minNewFscore;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Input for n and the zero index
		int n = scanner.nextInt();
		int zeroIndex = scanner.nextInt();

		// Initialize the starting board
		int rowSize = (int) Math.sqrt(n + 1);
		int[][] startingTiles = new int[rowSize][rowSize];

		// Default position - lower left corner
		if (zeroIndex == -1) {
			zeroIndex = rowSize * (rowSize - 1);
		}

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

		System.out.println("Solving...");
		search(new Node(board, 0));
	}
}

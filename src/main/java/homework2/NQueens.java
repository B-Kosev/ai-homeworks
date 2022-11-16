package main.java.homework2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

// Github: https://github.com/B-Kosev/ai-homeworks
public class NQueens {
	// Holds the index of the row with the queen for each column
	private static int[] chessboard;
	// Counts the number of queens in each row and diagonal
	private static int[] rows, mainDiagonal, secondaryDiagonal;
	// Size of the board
	private static int n;
	private static boolean hasConflicts = true;
	// Counts the number of moves to solve the board
	private static int steps = 0;

	/**
	 * A method, initializing the board for the given n. Places the queens respecting the 'horse pattern'.
	 */
	private static void initializeBoard() {
		chessboard = new int[n];
		rows = new int[n];
		mainDiagonal = new int[2 * n - 1];
		secondaryDiagonal = new int[2 * n - 1];
		int row = 1;

		for (int col = 0; col < n; col++) {
			// Setting the row of the queen for this column
			chessboard[col] = row;
			// Incrementing the queens in the current row
			rows[row]++;
			// Incrementing the queens in the main diagonal
			mainDiagonal[col - row + n - 1]++;
			// Incrementing the number of queens in the secondary diagonal
			secondaryDiagonal[col + row]++;

			// Horse pattern
			row += 2;
			if (row >= n) {
				row = 0;
			}
		}
	}

	/**
	 * A method solving the NQueens problem.
	 */
	private static void solve() {
		int k = 1;
		int iter = 0;
		int col, row;

		// Try to find solution in k*n steps
		while (iter++ <= k * n) {
			// Get column with max conflicts
			col = getColumnWithMaxConflicts();
			if (!hasConflicts) {
				return;
			}

			// Get row with min conflicts
			row = getRowWithMinConflicts(col);

			// Move the queen
			int oldRow = chessboard[col];
			moveQueen(col, oldRow, row);

			steps++;
		}
		if (hasConflicts) {
			solve();
		}
	}

	/**
	 * Calculates the number of conflicts for each column and returns the one with the most conflicts.
	 * 
	 * @return the column, where the queen has the most conflicts. If multiple columns with the same number of conflicts returns one of them
	 *         randomly.
	 */
	private static int getColumnWithMaxConflicts() {
		int conflicts;
		int maxConflicts = -1;

		List<Integer> result = new ArrayList<>();
		Random rand = new Random();

		// Iterate over all columns
		for (int col = 0; col < n; col++) {
			int row = chessboard[col];
			conflicts = countConflicts(row, col);

			if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				result.clear();
				result.add(col);
			} else if (conflicts == maxConflicts) {
				result.add(col);
			}
		}

		if (maxConflicts == 0) {
			hasConflicts = false;
		}

		return result.get(rand.nextInt(result.size()));
	}

	/**
	 * Calculates the row where the queen would have minimum conflicts for the given column.
	 *
	 * @return the row, where the queen has the least conflicts. If multiple rows with the same number of conflicts returns one of them
	 *         randomly.
	 */
	private static int getRowWithMinConflicts(int col) {
		int conflicts;
		int minConflicts = Integer.MAX_VALUE;
		int oldRow = chessboard[col];

		List<Integer> result = new ArrayList<>();
		Random rand = new Random();

		// Iterate over all rows for the given column
		for (int row = 0; row < n; row++) {
			// Move the queen to the new row
			moveQueen(col, oldRow, row);

			conflicts = countConflicts(row, col);

			if (conflicts < minConflicts) {
				minConflicts = conflicts;
				result.clear();
				result.add(row);
			} else if (conflicts == minConflicts) {
				result.add(row);
			}

			// Move back
			moveQueen(col, row, oldRow);
		}

		return result.get(rand.nextInt(result.size()));
	}

	/**
	 * Moves the queen for the given column to a new row
	 * 
	 * @param col
	 *            - the given column
	 * @param oldRow
	 *            - the current row of the queen
	 * @param newRow
	 *            - the new row of the queen
	 */
	private static void moveQueen(int col, int oldRow, int newRow) {
		chessboard[col] = newRow;

		// Decrease the count for the old row and diagonals
		rows[oldRow]--;
		mainDiagonal[col - oldRow + n - 1]--;
		secondaryDiagonal[col + oldRow]--;

		// Increase the count for the new row and diagonals
		rows[newRow]++;
		mainDiagonal[col - newRow + n - 1]++;
		secondaryDiagonal[col + newRow]++;
	}

	/**
	 * Counts the conflicts for the given queen
	 * 
	 * @param row
	 *            - the row of the queen
	 * @param col
	 *            - the column of the queen
	 * @return the number of conflicts
	 */
	private static int countConflicts(int row, int col) {
		int queensPerRow = rows[row];
		int queensPerMainDiagonal = mainDiagonal[col - row + n - 1];
		int queensPerSecondaryDiagonal = secondaryDiagonal[col + row];

		// The formula is calculating the number of edges in a complete graph as every queen is connected to every other queen for the row
		// or diagonal
		// (N * (N-1)) / 2
		int conflicts = (queensPerRow * (queensPerRow - 1)) / 2;
		conflicts += (queensPerMainDiagonal * (queensPerMainDiagonal - 1)) / 2;
		conflicts += (queensPerSecondaryDiagonal * (queensPerSecondaryDiagonal - 1)) / 2;

		return conflicts;
	}

	private static void printChessboard() {
		// Iterates rows
		for (int i = 0; i < n; i++) {
			// Iterates columns
			for (int j = 0; j < n; j++) {
				if (chessboard[j] == i) {
					System.out.print("* ");
				} else {
					System.out.print("_ ");
				}
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		n = scanner.nextInt();
		if (n == 2 || n == 3 || n < 1) {
			System.out.println("No solution for this n");
			return;
		}

		long startTime = System.currentTimeMillis();

		initializeBoard();

		solve();

		long endTime = System.currentTimeMillis();

		if (n < 100) {
			System.out.println();
			printChessboard();
		}

		System.out.println("Solved in " + steps + " steps.");

		System.out.println((endTime - startTime) / 1000.0);
	}
}

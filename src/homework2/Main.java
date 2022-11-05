package homework2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	private static int[] chessboard;
	private static int[] rows, mainDiagonal, secondaryDiagonal;
	private static int n;
	private static boolean hasConflicts = true;

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
			// Incrementing the queens in the main diagonal ( column - row + n - 1 )
			mainDiagonal[col - row + n - 1]++;
			// Incrementing the number of queens in the secondary diagonal ( column + row )
			secondaryDiagonal[col + row]++;

			row += 2;
			if (row >= n) {
				row = 0;
			}
		}
	}

	private static int solve() {
		int k = 3;
		int iter = 0;
		int col, row;

		// Try to find solution in k*n steps
		while (iter++ <= k * n) {
			// Get column with max conflicts
			col = getColumnWithMaxConflicts();
			if (!hasConflicts) {
				return iter;
			}

			// Get row with min conflicts
			row = getRowWithMinConflicts(col);

			// Move the queen
			int oldRow = chessboard[col];
			moveQueen(col, oldRow, row);
		}
		if (hasConflicts) {
			solve();
		}
		return 0;
	}

	private static int getColumnWithMaxConflicts() {
		int conflicts = 0;
		int maxConflicts = -1;
		List<Integer> result = new ArrayList<>();
		Random rand = new Random();

		for (int col = 0; col < n; col++) {
			int row = chessboard[col];
			conflicts = countConflicts(row, col);

			if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				result.clear();
				result.add(col);
			}

			if (conflicts == maxConflicts) {
				result.add(col);
			}
		}

		if (maxConflicts == 0) {
			hasConflicts = false;
		}

		return result.get(rand.nextInt(result.size()));
	}

	private static int getRowWithMinConflicts(int col) {
		int conflicts;
		int minConflicts = Integer.MAX_VALUE;
		int oldRow = chessboard[col];

		List<Integer> result = new ArrayList<>();
		Random rand = new Random();

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

	private static void moveQueen(int col, int oldRow, int newRow) {
		chessboard[col] = newRow;

		// Lower counts for the old row and diagonals
		rows[oldRow]--;
		mainDiagonal[col - oldRow + n - 1]--;
		secondaryDiagonal[col + oldRow]--;

		// Bump counts for the new row and diagonals
		rows[newRow]++;
		mainDiagonal[col - newRow + n - 1]++;
		secondaryDiagonal[col + newRow]++;
	}

	private static int countConflicts(int row, int col) {
		int queensPerRow = rows[row];
		int queensPerMainDiagonal = mainDiagonal[col - row + n - 1];
		int queensPerSecondaryDiagonal = secondaryDiagonal[col + row];

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
					System.out.print("W ");
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

		if (n < 100) {
			System.out.println("Starting chessboard: ");
			printChessboard();
		}

		int steps = solve();

		long endTime = System.currentTimeMillis();

		System.out.println("Solved in " + --steps + " steps.");

		if (n < 100) {
			System.out.println();
			printChessboard();
		}

		// System.out.println("Rows: " + Arrays.toString(rows));
		// System.out.println("Main diagonal: " + Arrays.toString(mainDiagonal));
		// System.out.println("Secondary diagonal: " + Arrays.toString(secondaryDiagonal));

		System.out.println((endTime - startTime) / 1000.0);
	}
}

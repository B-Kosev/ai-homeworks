package homework2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	private static int[] chessboard;
	private static int[] rows, mainDiagonal, secondaryDiagonal;
	private static int n;

	private static void initializeBoard() {
		for (int col = 0; col < n; col++) {
			int row = getRandomNumberBetween(0, n - 1);
			// Setting the row of the queen for this column
			chessboard[col] = row;
			// Incrementing the queens in the current row
			rows[row]++;
			// Incrementing the queens in the main diagonal ( column - row + n - 1 )
			mainDiagonal[col - row + n - 1]++;
			// Incrementing the number of queens in the secondary diagonal ( column + row )
			secondaryDiagonal[col + row]++;
		}
	}

	private static int solve() {
		int k = 10000;
		int col = -1, row = -1;
		Random rand = new Random();
		List<Integer> columnsList, rowsList;

		for (int i = 0; i < k * n; i++) {
			// Get column with max conflicts
			columnsList = getColumnWithMaxConflicts();
			col = columnsList.get(rand.nextInt(columnsList.size()));
			if (col == -1) {
				return i;
			}

			// Get row with min conflicts
			rowsList = getRowWithMinConflicts(col);
			row = rowsList.get(rand.nextInt(rowsList.size()));
			if (row == -1) {
				return i;
			}

			// Move the queen
			int oldRow = chessboard[col];
			moveQueen(col, oldRow, row);
		}

		System.out.println("Failed");
		return 0;
	}

	private static List<Integer> getColumnWithMaxConflicts() {
		int conflicts = 0;
		int maxConflicts = 0;
		List<Integer> result = new ArrayList<>();

		for (int col = 0; col < n; col++) {
			int row = chessboard[col];
			conflicts = countConflicts(row, col);

			if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				result.add(col);
			}

			if (conflicts == maxConflicts && conflicts != 0) {
				result.add(col);
			}
		}

		// If no conflicts are found, signal for solution
		if (result.isEmpty()) {
			result.add(-1);
		}

		return result;
	}

	private static List<Integer> getRowWithMinConflicts(int col) {
		int conflicts = 0;
		int minConflicts = Integer.MAX_VALUE;
		List<Integer> result = new ArrayList<>();
		int oldRow = chessboard[col];

		for (int row = 0; row < n; row++) {
			// Move the queen to the new row
			moveQueen(col, oldRow, row);

			conflicts = countConflicts(row, col);

			if (conflicts < minConflicts) {
				minConflicts = conflicts;
				result.add(row);
			}

			if (conflicts == minConflicts && conflicts != 0) {
				result.add(row);
			}

			// Move back
			moveQueen(col, row, oldRow);
		}

		// If no row is found, signal for solution
		if (result.isEmpty()) {
			result.add(-1);
		}

		return result;
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

	private static int getRandomNumberBetween(int lowerBound, int upperBound) {
		Random random = new Random();
		return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		n = scanner.nextInt();
		if (n == 2 || n == 3 || n < 1) {
			System.out.println("No solution for this n");
			return;
		}

		chessboard = new int[n];
		rows = new int[n];
		mainDiagonal = new int[2 * n - 1];
		secondaryDiagonal = new int[2 * n - 1];

		long startTime = System.currentTimeMillis();

		initializeBoard();

		if (n < 100) {
			System.out.println("Starting chessboard: ");
			printChessboard();
		}

		int steps = solve();

		long endTime = System.currentTimeMillis();

		System.out.println("Solved in " + steps + " steps.");

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

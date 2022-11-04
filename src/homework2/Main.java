package homework2;

import java.util.Random;
import java.util.Scanner;

public class Main {
	private static int[] chessboard;
	private static int[] rows, mainDiagonal, secondaryDiagonal;
	private static int n;

	private static void initializeBoard() {
		for (int i = 0; i < n; i++) {
			int pos = getRandomNumberBetween(0, n - 1);
			// Setting the row of the queen for this column
			chessboard[i] = pos;
			// Incrementing the queens in the current row
			rows[pos]++;
			// Incrementing the queens in the main diagonal ( column - row + n - 1 )
			mainDiagonal[i - pos + n - 1]++;
			// Incrementing the number of queens in the secondary diagonal ( column + row )
			secondaryDiagonal[i + pos]++;
		}
	}

	private static void solve() {
		int k = 10;
		int col = -1, row = -1;

		for (int i = 0; i < k * n; i++) {
			// Get column with max conflicts
			col = getColumnWithMaxConflicts();
			if (col == -1) {
				System.out.println("col return");
				return;
			}
			row = getRowWithMinConflicts(col);
			if (row == -1) {
				System.out.println("row return");
				return;
			}
			chessboard[col] = row;
			System.out.println(i);
		}

	}

	private static int getColumnWithMaxConflicts() {
		int conflicts = 0;
		int maxConflicts = 0;
		int result = -1;

		for (int col = 0; col < n; col++) {
			int row = chessboard[col];
			conflicts = countConflicts(row, col);

			if (conflicts > maxConflicts) {
				maxConflicts = conflicts;
				result = col;
			}
		}
		return result;
	}

	// NOT WORKING
	private static int getRowWithMinConflicts(int col) {
		int conflicts = 0;
		int minConflicts = Integer.MAX_VALUE;
		int result = -1;

		for (int row = 0; row < n; row++) {
			conflicts = countConflicts(row, col);

			if (conflicts < minConflicts) {
				minConflicts = conflicts;
				result = row;
			}
		}
		return result;
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

		solve();

		long endTime = System.currentTimeMillis();

		if (n < 100) {
			System.out.println("Resulting chessboard: ");
			printChessboard();
		}

		// System.out.println("Rows: " + Arrays.toString(rows));
		// System.out.println("Main diagonal: " + Arrays.toString(mainDiagonal));
		// System.out.println("Secondary diagonal: " + Arrays.toString(secondaryDiagonal));

		System.out.println((endTime - startTime) / 1000.0);
	}
}

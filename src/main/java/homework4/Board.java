package main.java.homework4;

public class Board {
	private final char[][] tiles;

	public Board() {
		this.tiles = new char[][] { { ' ', ' ', ' ' }, { ' ', ' ', ' ' }, { ' ', ' ', ' ' } };
	}

	public char[][] getTiles() {
		return tiles;
	}

	public void printBoard() {
		System.out.println("───────────");
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(" " + tiles[i][j]);
				if (j < 2)
					System.out.print(" │");
			}
			System.out.println("\n───────────");
		}
	}

	public int evaluateBoard(char maximizer, char minimizer) {
		// Check for rows
		for (int i = 0; i < 3; i++) {
			if (tiles[i][0] == tiles[i][1] && tiles[i][1] == tiles[i][2]) {
				if (tiles[i][0] == maximizer) {
					return countEmptyCells() + 1;
				} else if (tiles[i][0] == minimizer) {
					return -1 * (countEmptyCells() + 1);
				}
			}
		}

		// Check for cols
		for (int i = 0; i < 3; i++) {
			if (tiles[0][i] == tiles[1][i] && tiles[1][i] == tiles[2][i]) {
				if (tiles[0][i] == maximizer) {
					return countEmptyCells() + 1;
				} else if (tiles[0][i] == minimizer) {
					return -1 * (countEmptyCells() + 1);
				}
			}
		}

		// Check diagonals
		if (tiles[0][0] == tiles[1][1] && tiles[1][1] == tiles[2][2]) {
			if (tiles[0][0] == maximizer) {
				return countEmptyCells() + 1;
			} else if (tiles[0][0] == minimizer) {
				return -1 * (countEmptyCells() + 1);
			}
		}

		if (tiles[0][2] == tiles[1][1] && tiles[1][1] == tiles[2][0]) {
			if (tiles[0][2] == maximizer) {
				return countEmptyCells() + 1;
			} else if (tiles[0][2] == minimizer) {
				return -1 * (countEmptyCells() + 1);
			}
		}

		// Tie
		return 0;
	}

	public int countEmptyCells() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (tiles[i][j] == TicTacToe.EMPTY)
					count++;
			}
		}
		return count;
	}

	public boolean isTerminal(char maximizer, char minimizer) {
		return evaluateBoard(maximizer, minimizer) != 0 || countEmptyCells() == 0;
	}
}

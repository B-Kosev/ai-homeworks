package main.java.homework1;

import java.util.HashMap;
import java.util.Map;

public class Board {
	private final int rows;
	private final int[][] tiles;

	public Board(int rows, int[][] tiles) {
		this.rows = rows;
		this.tiles = new int[rows][rows];
		for (int i = 0; i < rows; i++) {
			System.arraycopy(tiles[i], 0, this.tiles[i], 0, rows);
		}
	}

	public int getRows() {
		return rows;
	}

	public int[][] getTiles() {
		return tiles;
	}

	/**
	 * Calculates the manhattan distance to the goal board
	 *
	 * @return the calculated heuristic
	 */
	public int manhattanDistance() {
		int manhattan = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				int val = tiles[i][j];
				if (val != 0) {
					int targetRow = (val - 1) / rows;
					int targetCol = (val - 1) % rows;
					manhattan += Math.abs(targetRow - i) + Math.abs(targetCol - j);
				}
			}
		}
		return manhattan;
	}

	/**
	 * Calculates all possible boards with one move of the zero
	 * 
	 * @return list of possible boards with single move of the zero
	 */
	public Map<Board, String> neighbours() {
		Map<Board, String> boards = new HashMap<>();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				// Find the zero as we are moving it
				if (tiles[i][j] != 0) {
					continue;
				}
				// Calculate possible moves
				if (i > 0) {
					boards.put(move(i, j, i - 1, j), "down");
				}
				if (i < rows - 1) {
					boards.put(move(i, j, i + 1, j), "up");
				}
				if (j > 0) {
					boards.put(move(i, j, i, j - 1), "right");
				}
				if (j < rows - 1) {
					boards.put(move(i, j, i, j + 1), "left");
				}
				return boards;
			}
		}
		// Shouldn't be reached
		return null;
	}

	/**
	 * Checks if the current board is solved
	 *
	 * @return true if the board is solved, false otherwise
	 */
	public boolean isSolved() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				// Last cell must be 0
				if (i == rows - 1 && j == rows - 1 && tiles[i][j] == 0) {
					continue;
				}
				if (tiles[i][j] != i * rows + j + 1) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Swaps two tiles
	 * 
	 * @param x1
	 *            row of the first tile
	 * @param y1
	 *            column of the first tile
	 * @param x2
	 *            row of the second tile
	 * @param y2
	 *            column of the second tile
	 */
	private void swap(int x1, int y1, int x2, int y2) {
		int temp = tiles[x1][y1];
		tiles[x1][y1] = tiles[x2][y2];
		tiles[x2][y2] = temp;
	}

	/**
	 * Creates a new board with the zero at the specified position
	 * 
	 * @param x1
	 *            current row of the zero
	 * @param y1
	 *            current column of the zero
	 * @param x2
	 *            new row of the zero
	 * @param y2
	 *            new column of the zero
	 * @return the new board
	 */
	private Board move(int x1, int y1, int x2, int y2) {
		Board newBoard = new Board(rows, tiles);
		newBoard.swap(x1, y1, x2, y2);
		return newBoard;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Current board: \n");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < rows; j++) {
				sb.append(tiles[i][j]);
				sb.append(" ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}

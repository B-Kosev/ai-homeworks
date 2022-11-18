package main.java.homework4;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Scanner;

public class TicTacToe {
	public static char PC_SYMBOL;
	public static char PLAYER_SYMBOL;
	public static char EMPTY = ' ';
	private static Board board;

	public static boolean makeMove(int x, int y, char symbol) {
		if (x >= 0 && x < 3 && y >= 0 && y < 3) {
			if (board.getTiles()[x][y] == EMPTY) {
				board.getTiles()[x][y] = symbol;

				System.out.println("========================");
				board.printBoard();

				return true;
			}
		}
		System.out.println("Pick an empty tile.");
		return false;
	}

	// PC is MIN
	public static Map.Entry<Integer, Integer> calculateMoveForPc() {
		int best = Integer.MAX_VALUE;
		int x = -1, y = -1, v;

		// Try every move
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board.getTiles()[i][j] == EMPTY) {
					board.getTiles()[i][j] = PC_SYMBOL;

					v = maximizer(Integer.MIN_VALUE, Integer.MAX_VALUE);

					// Update
					if (v < best) {
						best = v;
						x = i;
						y = j;
					}

					// Undo
					board.getTiles()[i][j] = EMPTY;
				}
			}
		}
		return new AbstractMap.SimpleEntry<>(x, y);
	}

	public static int maximizer(int alpha, int beta) {
		if (board.isTerminal(PLAYER_SYMBOL, PC_SYMBOL))
			return board.evaluateBoard(PLAYER_SYMBOL, PC_SYMBOL);

		int best = Integer.MIN_VALUE, v;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board.getTiles()[i][j] == EMPTY) {
					// Player always is MAX
					board.getTiles()[i][j] = PLAYER_SYMBOL;

					v = minimizer(alpha, beta);

					if (v > best) {
						best = v;
					}

					// Undo
					board.getTiles()[i][j] = EMPTY;

					if (best >= beta)
						return best;
					alpha = Math.max(alpha, best);
				}
			}
		}
		return best;
	}

	public static int minimizer(int alpha, int beta) {
		if (board.isTerminal(PLAYER_SYMBOL, PC_SYMBOL))
			return board.evaluateBoard(PLAYER_SYMBOL, PC_SYMBOL);

		int best = Integer.MAX_VALUE, v;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board.getTiles()[i][j] == EMPTY) {
					// PC always is MIN
					board.getTiles()[i][j] = PC_SYMBOL;

					v = maximizer(alpha, beta);

					if (v < best) {
						best = v;
					}

					// Undo
					board.getTiles()[i][j] = EMPTY;

					if (best <= alpha)
						return best;
					beta = Math.min(beta, best);
				}
			}
		}
		return best;
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		// Is the human first
		String first;
		boolean playerTurn;
		do {
			System.out.println("Do you want to be first? y/n");
			first = scanner.next();
		} while (!first.equals("y") && !first.equals("n"));

		if (first.equals("y")) {
			PLAYER_SYMBOL = 'X';
			PC_SYMBOL = 'O';
			playerTurn = true;
		} else {
			PLAYER_SYMBOL = 'O';
			PC_SYMBOL = 'X';
			playerTurn = false;
		}

		board = new Board();
		board.printBoard();

		// In all cases Player is MAX and PC is MIN
		while (!board.isTerminal(PLAYER_SYMBOL, PC_SYMBOL)) {
			if (playerTurn) {
				int x, y;
				do {
					System.out.println("Enter x and y for your move:");
					x = scanner.nextInt();
					y = scanner.nextInt();
				} while (!makeMove(x - 1, y - 1, PLAYER_SYMBOL));
				playerTurn = !playerTurn;
				continue;
			}

			Map.Entry<Integer, Integer> move = calculateMoveForPc();
			makeMove(move.getKey(), move.getValue(), PC_SYMBOL);
			playerTurn = !playerTurn;
		}

		if (board.isTerminal(PLAYER_SYMBOL, PC_SYMBOL)) {
			int eval = board.evaluateBoard(PLAYER_SYMBOL, PC_SYMBOL);

			if (eval == 0)
				System.out.println("Tie.");
			else if (eval > 0)
				System.out.println("You won!");
			else
				System.out.println("Haha n00b, you lost");
		}
	}
}

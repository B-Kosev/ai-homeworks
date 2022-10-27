package homework1;

import java.util.List;

/**
 * A node in the tree containing own board, calculated f score, number of moves to that board, it's parent and the path to the current
 * board. Implementing comparable, so it can be compared in the priority queue
 */
public class Node implements Comparable<Node> {
	private Board board;
	private int fScore;
	private int moves;
	private List<String> path;

	public Node(Board board, int moves, List<String> path) {
		this.board = board;
		this.fScore = board.manhattanDistance() + moves;
		this.moves = moves;
		this.path = path;
	}

	public Board getBoard() {
		return board;
	}

	public int getfScore() {
		return fScore;
	}

	public int getMoves() {
		return moves;
	}

	public List<String> getPath() {
		return path;
	}

	@Override
	public int compareTo(Node other) {
		return this.fScore + this.moves - other.fScore - other.moves;
	}
}

package homework1;

import java.util.Arrays;
import java.util.Objects;

/**
 * A node in the tree containing a board, the calculated heuristic for this board, the number of moves to this board and the fScore
 */
public class Node {
	private Board board;

	// Manhattan distance
	private int hScore;
	// Number of moves
	private int gScore;
	// hScore + gScore
	private int fScore;

	public Node(Board board, int gScore) {
		this.board = board;
		this.hScore = board.manhattanDistance();
		this.gScore = gScore;
		this.fScore = this.hScore + this.gScore;
	}

	public Board getBoard() {
		return board;
	}

	public int gethScore() {
		return hScore;
	}

	public int getgScore() {
		return gScore;
	}

	public int getfScore() {
		return fScore;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void sethScore(int hScore) {
		this.hScore = hScore;
	}

	public void setgScore(int gScore) {
		this.gScore = gScore;
	}

	public void setfScore(int fScore) {
		this.fScore = fScore;
	}

	/**
	 * Checks if two nodes are equal. We agree that two nodes are equal if they have the same board. It is this way, so we can check and
	 * avoid expanding already expanded board when iterating the neighbours
	 * 
	 * @param o
	 *            - the other node
	 * @return true if the nodes are equal, false otherwise
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Node))
			return false;
		Node node = (Node) o;
		return Arrays.deepEquals(getBoard().getTiles(), node.getBoard().getTiles());
	}

	@Override
	public int hashCode() {
		return Objects.hash((Object) getBoard().getTiles());
	}
}

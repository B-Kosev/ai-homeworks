package homework1;

/**
 * A node in the tree containing own board, calculated f score, number of moves to that board, it's parent and the path to the current
 * board. Implementing comparable, so it can be compared in the priority queue
 */
public class Node implements Comparable<Node> {
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

	@Override
	public int compareTo(Node other) {
		return this.fScore - other.fScore;
	}
}

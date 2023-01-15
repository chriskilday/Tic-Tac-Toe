import java.util.ArrayList;
import java.util.List;

public class Board {
	private static final int BOARD_SIZE = 3;
	private static final int WIN_LENGTH = BOARD_SIZE;

	private int[][] board;
	private int moves;

	public Board() {
		board = new int[BOARD_SIZE][BOARD_SIZE];
		this.moves = 0;
	}

	public boolean full() {
		return moves == BOARD_SIZE * BOARD_SIZE;
	}

	public void add(int player, int row, int col) {
		this.moves++;
		this.board[row][col] = player;
	}

	public void clear() {
		this.moves = 0;
		this.board = new int[BOARD_SIZE][BOARD_SIZE];
	}

	public boolean isOpen(int row, int col) {
		return board[row][col] == 0;
	}

	public int checkWin() {
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (i <= BOARD_SIZE - WIN_LENGTH && board[i][j] != 0 && board[i][j] == board[i + 1][j]
						&& board[i][j] == board[i + 2][j]) {
					return board[i][j];
				}
				if (j <= BOARD_SIZE - WIN_LENGTH && board[i][j] != 0 && board[i][j] == board[i][j + 1]
						&& board[i][j] == board[i][j + 2]) {
					return board[i][j];
				}
				if (i <= BOARD_SIZE - WIN_LENGTH && j <= BOARD_SIZE - WIN_LENGTH && board[i][j] != 0
						&& board[i][j] == board[i + 1][j + 1] && board[i][j] == board[i + 2][j + 2]) {
					return board[i][j];
				}
				if (i >= WIN_LENGTH - 1 && j <= BOARD_SIZE - WIN_LENGTH && board[i][j] != 0
						&& board[i][j] == board[i - 1][j + 1] && board[i][j] == board[i - 2][j + 2]) {
					return board[i][j];
				}
			}
		}
		return 0;
	}

	public ArrayList<int[]> openSpaces() {
		ArrayList<int[]> spaces = new ArrayList<int[]>();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (board[i][j] == 0) {
					int[] xy = { i, j };
					spaces.add(xy);
				}
			}
		}
		return spaces;
	}

	public String board() {
		String str = "";
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				str += "" + board[i][j];
			}
		}
		return str;
	}

	public Board rotate() {
		Board newBoard = new Board();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				newBoard.board[col][BOARD_SIZE - 1 - row] = this.board[row][col];
			}
		}
		return newBoard;
	}

	public int[] rotate(int[] coord) {
		int row = coord[0], col = coord[1];
		return new int[] { col, BOARD_SIZE - 1 - row };
	}

	public Board flipVert() {
		Board newBoard = new Board();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				newBoard.board[BOARD_SIZE - 1 - row][col] = this.board[row][col];
			}
		}
		return newBoard;
	}

	public int[] flipVert(int[] coord) {
		int row = coord[0], col = coord[1];
		return new int[] { BOARD_SIZE - 1 - row, col };
	}

	public Board flipHorz() {
		Board newBoard = new Board();
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				newBoard.board[row][BOARD_SIZE - 1 - col] = this.board[row][col];
			}
		}
		return newBoard;
	}

	public int[] flipHorz(int[] coord) {
		int row = coord[0], col = coord[1];
		return new int[] { row, BOARD_SIZE - 1 - col };
	}

	public int size() {
		return this.BOARD_SIZE;
	}
}

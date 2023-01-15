import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TicTacToeMain extends Application {
	private static final int PLAYER1 = 1;
	private static final int PLAYER2 = 2;
	private static final int SQUARE_SIZE = 150;

	private static Board board;
	private static int curPlayer;
	private static boolean isOver;

	private static boolean automatic = false;

	public static void main(String[] args) {
		board = new Board();
		curPlayer = PLAYER1;
		isOver = false;
		launch(args);
	}

	public void start(Stage stage) {
		Group root = new Group();
		drawGrid(root);

		Computer computer1 = new Computer();
		Computer computer2 = new Computer();

		Scene scene = new Scene(root, SQUARE_SIZE * board.size(), SQUARE_SIZE * board.size());

		scene.addEventHandler(MouseEvent.MOUSE_CLICKED, mouse -> {
			if (!automatic) {
				playerVsComputer(mouse, computer2, root);
			}
		});

		AnimationTimer timer = new AnimationTimer() {
			public void handle(long time) {
				computerVsComputer(computer1, computer2, root);
			}
		};

		scene.addEventHandler(KeyEvent.KEY_PRESSED, key -> {
			if (key.getCode() == KeyCode.R) {
				reset(root);
			} else if (key.getCode() == KeyCode.Q) {
				System.exit(0);
			} else if (key.getCode() == KeyCode.O) {
				automatic = !automatic;
				if (automatic) {
					timer.start();
				} else {
					timer.stop();
				}
				reset(root);
			}
		});

		stage.setScene(scene);
		stage.show();
	}

	private void playerVsComputer(MouseEvent mouse, Computer computer, Group root) {
		int col = ((int) mouse.getSceneX()) / SQUARE_SIZE;
		int row = ((int) mouse.getSceneY()) / SQUARE_SIZE;

		if (board.isOpen(row, col) && !isOver) {
			add(row, col, root);
			curPlayer = PLAYER2;

			if (board.checkWin() == 0 && !board.full()) {
				int[] computerMove = computer.move(board, false, false, false);
				int cRow = computerMove[0];
				int cCol = computerMove[1];
				add(cRow, cCol, root);
				curPlayer = PLAYER1;
			}

			if (board.checkWin() == PLAYER1) {
				computer.lost();
				win(PLAYER1, root);
			} else if (board.checkWin() == PLAYER2) {
				computer.won();
				win(PLAYER2, root);
			} else if (board.full()) {
				tie(root);
			}
		}
	}

	public void computerVsComputer(Computer computer1, Computer computer2, Group root) {
		int[] move1 = computer1.move(board, false, false, false);
		board.add(curPlayer, move1[0], move1[1]);
		curPlayer = PLAYER2;

		if (board.checkWin() == 0 && !board.full()) {
			int[] move2 = computer2.move(board, false, false, false);
			board.add(curPlayer, move2[0], move2[1]);
			curPlayer = PLAYER1;
		}

		if (board.checkWin() == PLAYER1) {
			computer1.won();
			computer2.lost();
			win(PLAYER1, root);
			reset(root);
			System.out.println("W");
			curPlayer = PLAYER1;
		} else if (board.checkWin() == PLAYER2) {
			computer1.lost();
			computer2.won();
			win(PLAYER1, root);
			reset(root);
			System.out.println("L");
			curPlayer = PLAYER1;
		} else if (board.full()) {
			tie(root);
			reset(root);
			System.out.println("T");
			curPlayer = PLAYER1;
		}
	}

	private void win(int winner, Group root) {
		Text win = new Text();
		isOver = true;

		win.setText("Player " + winner + " Wins");
		win.setFont(new Font("Grupo", SQUARE_SIZE / 5));
		win.setX((SQUARE_SIZE * board.size()) / 2 - 3 * win.getFont().getSize() - win.getFont().getSize() / 10);
		win.setY((SQUARE_SIZE * board.size()) / 2 + win.getFont().getSize() / 4);

		root.getChildren().add(win);
	}

	private void tie(Group root) {
		Text draw = new Text();
		isOver = true;

		draw.setText("Draw");
		draw.setFont(new Font("Grupo", SQUARE_SIZE / 5));
		draw.setX((SQUARE_SIZE * board.size()) / 2 - SQUARE_SIZE / 3 + SQUARE_SIZE / 15);
		draw.setY((SQUARE_SIZE * board.size()) / 2 + draw.getFont().getSize() / 4);

		root.getChildren().add(draw);
	}

	private void reset(Group root) {
		board.clear();
		isOver = false;
		root.getChildren().clear();
		drawGrid(root);
		curPlayer = PLAYER1;
	}

	private void drawGrid(Group root) {
		for (int i = 1; i < board.size(); i++) {
			Line line1 = new Line(0, SQUARE_SIZE * i, SQUARE_SIZE * board.size(), SQUARE_SIZE * i);
			Line line2 = new Line(SQUARE_SIZE * i, 0, SQUARE_SIZE * i, SQUARE_SIZE * board.size());
			root.getChildren().add(line1);
			root.getChildren().add(line2);
		}
	}

	private void add(int row, int col, Group root) {
		board.add(curPlayer, row, col);

		if (curPlayer == PLAYER1) {
			Line line1 = new Line(SQUARE_SIZE * col, SQUARE_SIZE * row, SQUARE_SIZE * (col + 1) - 1,
					SQUARE_SIZE * (row + 1) - 1);
			line1.setStrokeWidth(3);
			line1.setStroke(Color.RED);

			Line line2 = new Line(SQUARE_SIZE * (col + 1) - 1, SQUARE_SIZE * row, SQUARE_SIZE * col,
					SQUARE_SIZE * (row + 1) - 1);
			line2.setStrokeWidth(3);
			line2.setStroke(Color.RED);

			root.getChildren().add(line1);
			root.getChildren().add(line2);
		} else {
			Circle circle = new Circle(SQUARE_SIZE * col + SQUARE_SIZE / 2, SQUARE_SIZE * row + SQUARE_SIZE / 2,
					SQUARE_SIZE / 2 - 2);
			circle.setStrokeWidth(3);
			circle.setStroke(Color.BLUE);
			circle.setFill(Color.WHITE);

			root.getChildren().add(circle);
		}
	}

	private class Computer {
		private Map<String, ArrayList<int[]>> options;
		private List<String> lastBoard;
		private List<int[]> lastMove;

		public Computer() {
			this.options = new HashMap<String, ArrayList<int[]>>();
			this.lastBoard = new ArrayList<String>();
			this.lastMove = new ArrayList<int[]>();
		}

		public void won() {
			ArrayList<int[]> move = new ArrayList<int[]>();
			move.add(lastMove.get(lastMove.size() - 1));
			this.options.put(lastBoard.get(lastBoard.size() - 1), move);

			this.lastBoard = new ArrayList<String>();
			this.lastMove = new ArrayList<int[]>();
		}

		public void lost() {
			removeLastMove();

			this.lastBoard = new ArrayList<String>();
			this.lastMove = new ArrayList<int[]>();
		}

		public int[] move(Board board, boolean rotated, boolean flippedVert, boolean flippedHorz) {
			if (!rotated) {
				if (options.keySet().contains(board.rotate().board())
						|| options.keySet().contains(board.rotate().flipVert().board())
						|| options.keySet().contains(board.rotate().flipHorz().board())) {
					return board.rotate(
							board.rotate(board.rotate(this.move(board.rotate(), true, flippedVert, flippedHorz))));
				}
				if (options.keySet().contains(board.rotate().rotate().board())
						|| options.keySet().contains(board.rotate().rotate().flipVert().board())
						|| options.keySet().contains(board.rotate().rotate().flipHorz().board())) {
					return board
							.rotate(board.rotate(this.move(board.rotate().rotate(), true, flippedVert, flippedHorz)));
				}
				if (options.keySet().contains(board.rotate().rotate().rotate().board())
						|| options.keySet().contains(board.rotate().rotate().rotate().flipVert().board())
						|| options.keySet().contains(board.rotate().rotate().rotate().flipHorz().board())) {
					return board.rotate(this.move(board.rotate().rotate().rotate(), true, flippedVert, flippedHorz));
				}
			}

			if (!flippedVert && options.keySet().contains(board.flipVert().board())) {
				return board.flipVert(this.move(board.flipVert(), rotated, true, flippedHorz));
			}

			if (!flippedHorz && options.keySet().contains(board.flipHorz().board())) {
				return board.flipHorz(this.move(board.flipHorz(), rotated, flippedVert, true));
			}

			String curBoard = board.board();
			if (!options.keySet().contains(curBoard)) {
				options.put(curBoard, board.openSpaces());
			}

			if (options.get(curBoard).size() == 0) {
				lastBoard.add(curBoard);
				lastMove.add(board.openSpaces().get(0));
				return board.openSpaces().get(0);
			}

			int rand = (int) (Math.random() * options.get(curBoard).size());

			lastBoard.add(curBoard);
			lastMove.add(options.get(curBoard).get(rand));

			return options.get(curBoard).get(rand);
		}

		private void removeLastMove() {
			if (lastBoard.size() > 0 && lastMove.size() > 0) {
				String board = lastBoard.remove(lastBoard.size() - 1);
				int[] move = lastMove.remove(lastMove.size() - 1);

				this.options.get(board).remove(move);

				while (options.get(board).size() == 0 && lastBoard.size() > 0 && lastMove.size() > 0) {
					board = lastBoard.remove(lastBoard.size() - 1);
					move = lastMove.remove(lastMove.size() - 1);

					this.options.get(board).remove(move);
				}
			}
		}
	}
}

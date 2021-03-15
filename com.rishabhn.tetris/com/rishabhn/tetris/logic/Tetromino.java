package com.rishabhn.tetris.logic;

import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

public class Tetromino implements Cloneable {
	public List<Square> squares;
	private static final Tetromino[] blocks;
	public static final int totalPieces;
	private int x, y;
	private int width, height;
	// 0, 1, 2, 3 (4 different rotations)
	static {
		blocks = new Tetromino[] {
			new Tetromino(new Color(132, 196, 196), 0, 1, 2, 3), // 4-Row
			new Tetromino(new Color(112, 178, 121), 1, 2, 4, 5), // S
			new Tetromino(new Color(212, 77, 51), 0, 1, 5, 6), // Back-S
			new Tetromino(new Color(228, 200, 78), 0, 1, 4, 5), // 4-Square
			new Tetromino(new Color(52, 99, 150), 0, 4, 5, 6), // Back-L
			new Tetromino(new Color(232, 159, 65), 2, 4, 5, 6), // L
			new Tetromino(new Color(81, 58, 122), 0, 1, 2, 5), // T
		};
		totalPieces = blocks.length;
	}
	private final int size = 4;
	public final Color color;

	public Tetromino(Color color, int... positions) {
		// positions correspond to indexes in a 4x4 grid (0 being top left, 3 being top
		// right, 15 being bottom right)
		squares = new ArrayList<>();
		this.color = color;
		int minY = positions[0] / size, minX = positions[0] % size, maxY = minY, maxX = minX;
		for (int i = 0; i < positions.length; i++) {
			int x = positions[i] % size, y = positions[i] / size;
			if (x < minX)
				minX = x;
			else if (x > maxX)
				maxX = x;
			if (y < minY)
				minY = y;
			else if (y > maxY)
				maxY = y;
		}
		width = maxX - minX;
		height = maxY - minY;
		x = minX;
		y = minY;
		for (int i = 0; i < positions.length; i++) {
			int x = positions[i] % size, y = positions[i] / size;
			squares.add(new Square(color, x - minX, y - minY));
		}
	}

	public void right(Square[][] arr) {
		int gridWidth = arr[0].length;
		for (Square square : squares) {
			if (square.getX() + 1 >= gridWidth) {
				return;
			}
			if (arr[square.getY()][square.getX() + 1] != null) {
				return;
			}
		}
		x++;
		for (Square square : squares) {
			square.right();
		}

	}
	public void left(Square[][] arr) {
		int gridWidth = arr[0].length;
		for (Square square : squares) {
			if (square.getX() - 1 < 0) {
				return;
			}
			if (arr[square.getY()][square.getX() - 1] != null) {
				return;
			}
		}
		x--;
		for (Square square : squares) {
			square.left();
		}
	}
	public void down(Square[][] arr) {
		int gridHeight = arr.length;
		for (Square square : squares) {
			if (square.getY() + 1 >= gridHeight) {
				return;
			}
			if (arr[square.getY() + 1][square.getX()] != null) {
				return;
			}
		}
		y++;
		for (Square square : squares) {
			square.down();
		}
	}
	public int downSpaces(Square[][] arr) {
		int spaces = 0;
		int gridHeight = arr.length;
		while (true) {
			for (Square square : squares) {
				if (square.getY() + 1 + spaces >= gridHeight) {
					return spaces;
				}
				if (arr[square.getY() + 1 + spaces][square.getX()] != null) {
					return spaces;
				}
			}
			spaces++;
		}
	}
	public void slam(Square[][] arr) {
		int spaces = downSpaces(arr);
		for (Square square : squares) {
			square.slam(spaces);
		}
	}
	public void rotateClockwise(Square[][] arr) {
		int centerX = x + width / 2;
		int centerY = y + height / 2;
		int gridWidth = arr[0].length;
		int gridHeight = arr.length;

		for (Square square : squares) {
			int rotationY = centerY - (centerX - square.getX());
			int rotationX = centerX + (centerY - square.getY());
			if (rotationX < 0 || rotationX >= gridWidth || rotationY < 0 || rotationY >= gridHeight) {
				return;
			}
			if (arr[rotationY][rotationX] != null) {
				return;
			}
		}
		for (Square square : squares) {
			int rotationY = centerY - (centerX - square.getX());
			int rotationX = centerX + (centerY - square.getY());
			square.setPosition(rotationX, rotationY);
		}
	}
	public void rotateCounterClockwise(Square[][] arr) {
		int centerX = x + width / 2;
		int centerY = y + height / 2;
		int gridWidth = arr[0].length;
		int gridHeight = arr.length;

		for (Square square : squares) {
			int rotationY = centerY + (centerX - square.getX());
			int rotationX = centerX - (centerY - square.getY());
			if (rotationX < 0 || rotationX >= gridWidth || rotationY < 0 || rotationY >= gridHeight) {
				return;
			}
			if (arr[rotationY][rotationX] != null) {
				return;
			}
		}
		for (Square square : squares) {
			int rotationY = centerY + (centerX - square.getX());
			int rotationX = centerX - (centerY - square.getY());
			square.setPosition(rotationX, rotationY);
		}
	}

	@Override
	public Tetromino clone() throws CloneNotSupportedException {
		Tetromino t = (Tetromino) super.clone();
		t.squares = new ArrayList<Square>();
		for (Square square : squares) {
			t.squares.add(new Square(square.color, square.getX(), square.getY()));
		}
		return t;
	}

	private static List<Tetromino> cycle = new ArrayList<>();
	public static Tetromino randomPiece() {
		if (cycle.size() == 0) {
			for (Tetromino block : blocks) {
				Tetromino clone;
				try {
					clone = block.clone();
					cycle.add(clone);
				} catch (CloneNotSupportedException e) {}
			}
		}
		int randomIndex = (int)(Math.random() * cycle.size());
		Tetromino piece = cycle.get(randomIndex);
		cycle.remove(randomIndex);
		return piece;
	}
}

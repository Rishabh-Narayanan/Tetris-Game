package com.rishabhn.tetris.application;

import javax.swing.JPanel;

import com.rishabhn.tetris.logic.Square;
import com.rishabhn.tetris.logic.Tetromino;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Grid extends JPanel implements Runnable, KeyListener {
	int width = 10, height = 20;
	int squareSize = 30;
	Thread animator;
	
	Square[][] grid;
	public Grid() {
		setPreferredSize(new Dimension(width * squareSize, height * squareSize));
		grid = new Square[height][width];
		piece = Tetromino.randomPiece();
		downSpaces = piece.downSpaces(grid);
		addKeyListener(this);
		setFocusable(true);
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		animator = new Thread(this);
		animator.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		drawBoard(g);
		drawExisting(g);
		drawPiece(g);
		drawProjection(g);
	}

	final Color background = Color.WHITE;
	final Color outline = Color.LIGHT_GRAY;
	final Color projectionColor = Color.BLACK;
	public void drawBoard(Graphics g) {
		g.setColor(background);
		g.fillRect(0, 0, width * squareSize, height * squareSize);
		g.setColor(outline);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				g.drawRect(i * squareSize, j * squareSize, squareSize, squareSize);
			}
		}
	}

	public Tetromino piece;
	int margin = 2;
	public void drawPiece(Graphics g) {
		if (piece == null) return;
		g.setColor(piece.color);
		for (Square square : piece.squares) {
			g.fillRect(square.getX() * squareSize + margin, square.getY() * squareSize + margin, squareSize - 2 * margin, squareSize - 2 * margin);
		}
	}

	public void drawExisting(Graphics g) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				Square square = grid[j][i];
				if (square == null) continue;
				g.setColor(square.color);
				g.fillRect(square.getX() * squareSize + margin, square.getY() * squareSize + margin, squareSize - 2 * margin, squareSize - 2 * margin);
			}
		}
	}

	int downSpaces = 0;
	public void drawProjection(Graphics g) {
		g.setColor(projectionColor);
		for (Square square : piece.squares) {
			g.drawRect(square.getX() * squareSize + margin, (square.getY() + downSpaces) * squareSize + margin, squareSize - 2 * margin, squareSize - 2 * margin);
		}
	}
	
	boolean hasUpdated = true;
	boolean didSlam = false;
	@Override
	public void keyPressed(KeyEvent e) {
		if (didSlam) return;
		if (piece == null) piece = Tetromino.randomPiece();
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				piece.left(grid);
				break;
			case KeyEvent.VK_RIGHT:
				piece.right(grid);
				break;
			case KeyEvent.VK_DOWN:
				piece.down(grid);
				break;
			case KeyEvent.VK_SPACE:
				didSlam = true;
				piece.slam(grid);
				break;
			case KeyEvent.VK_Z:
				piece.rotateCounterClockwise(grid);
				break;
			case KeyEvent.VK_C:
				piece.rotateClockwise(grid);
				break;
			default:
				return;
		}
		downSpaces = piece.downSpaces(grid);
		hasUpdated = true;
		// else {
		// 	// if (++pieceIndex >= Tetromino.blocks.length) {
		// 	// 	pieceIndex = 0;
		// 	// }
		// 	// piece = Tetromino.blocks[pieceIndex];
		// 	// piece.down(grid);
		// }
	}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
	double gravity = 1.0 / 45;
	int framesAlive = 0;
	public void controlDown() {
		framesAlive++;
		if (didSlam || gravity * framesAlive >= 1) {
			framesAlive = 0;
			didSlam = false;
			if (downSpaces == 0) {
				for (Square square : piece.squares) {
					grid[square.getY()][square.getX()] = square;
				}

				piece = Tetromino.randomPiece();
				downSpaces = piece.downSpaces(grid);
			} else {
				piece.down(grid);
				downSpaces--;
			}
			hasUpdated = true;
		}
	}
	public void clearFullRows() {
		for (int r = grid.length - 1; r >= 0; r--) {
			boolean isRowFull = true;
			int clearCount = 0;
			for (int c = 0; c < grid[r].length; c++) {
				if (grid[r][c] == null) {
					isRowFull = false;
					clearCount++;
				}
			}
			if (clearCount == grid[r].length) {
				break;
			}
			if (isRowFull) {
				for (int i = r; i > 0; i--) {
					grid[i] = null;
					grid[i] = grid[i - 1];
					for (Square square : grid[i]) {
						if (square != null) {
							square.down();
						}
					}
				}
				r++;
			}
		}
	}
	public void checkIfTop() {
		for (Square square : grid[0]) {
			if (square != null) {
				grid = new Square[height][width];
			}
		}
	}

	public void update() {
		controlDown();
		if (hasUpdated) {
			clearFullRows();
			checkIfTop();
			hasUpdated = false;
		}
	}

	final long MILLISECONDS_BETWEEN_FRAMES = 1000 / 60;
	@Override
	public void run() {
		long end, start;
		while (true) {
			start = System.currentTimeMillis();
			update();
			repaint();
			end = System.currentTimeMillis();
			try {
				long timeToSleep = MILLISECONDS_BETWEEN_FRAMES - (end - start);
				Thread.sleep(timeToSleep <= 0 ? 1 : timeToSleep);
			} catch (Exception e) {
			}
		}
	}
}

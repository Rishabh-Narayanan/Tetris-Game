package com.rishabhn.tetris.logic;

import java.awt.Color;
public class Square {
	public final Color color;
	private int x, y;
	public Square(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public void left() {
		x--;
	}
	public void right() {
		x++;
	}
	public void down() {
		y++;
	}
	public void slam(int amount) {
		y += amount;
	}
	void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

}

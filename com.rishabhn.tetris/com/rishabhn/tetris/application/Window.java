package com.rishabhn.tetris.application;

import javax.swing.JFrame;
import javax.swing.JPanel;
public class Window extends JFrame {
	public Window() {
		JPanel panel = new JPanel();
		add(panel);
		panel.add(new Grid());
		pack();
		setLocationRelativeTo(null);
		
		setTitle("Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}	
}

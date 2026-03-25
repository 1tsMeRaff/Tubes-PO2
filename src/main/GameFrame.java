package main;

import javax.swing.JFrame;

public class GameFrame {
	private JFrame Jframe;
	public GameFrame(GamePanel gamePanel) {
		
		Jframe = new JFrame();
		
		Jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Jframe.add(gamePanel);
		Jframe.setLocationRelativeTo(null);
		Jframe.setResizable(false);
		Jframe.pack();
		Jframe.setVisible(true);
		
	}

}

package main;

import javax.swing.JFrame;

public class GameFrame {
	private JFrame Jframe;
	public GameFrame(GamePanel gamePanel) {
		
		Jframe = new JFrame();
		
		Jframe.setSize(400, 400);
		Jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Jframe.add(gamePanel);
		Jframe.setLocationRelativeTo(null);
		Jframe.setVisible(true);
		
	}

}

package main;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.JFrame;

public class GameFrame {
	private JFrame jFrame;
	private boolean isFullScreen = false;

	public GameFrame(GamePanel gamePanel) {
		
		jFrame = new JFrame();
		
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.add(gamePanel);
		jFrame.setResizable(false);
		jFrame.setTitle("Feline Souls : Witch Curse");
		
		jFrame.pack();
		
		jFrame.setLocationRelativeTo(null);
		
		jFrame.setVisible(true);
		jFrame.addWindowFocusListener(new WindowFocusListener() {
			
			@Override
			public void windowLostFocus(WindowEvent e) {
				gamePanel.getGame().windowFocusLost();
				
			}
			
			@Override
			public void windowGainedFocus(WindowEvent e) {
				// Kosongkan sesuai aslinya
			}
		});
	}

	// Method baru untuk mengatur Full Screen
	public void toggleFullScreen() {
		isFullScreen = !isFullScreen;
		
		// Dispose JFrame sementara untuk memodifikasi dekorasinya
		jFrame.dispose();
		
		// Set undecorated (tanpa border/tombol close) jika fullscreen
		jFrame.setUndecorated(isFullScreen);
		
		if (isFullScreen) {
			jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			jFrame.setExtendedState(JFrame.NORMAL);
			jFrame.pack(); // Kembalikan ke ukuran sesuai GamePanel
			jFrame.setLocationRelativeTo(null); // Tengahkan kembali
		}
		
		jFrame.setVisible(true);
	}
}
package gui;
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class CardBattleGUI {
	
	public static void main(String[] args) throws IOException {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CBMainFrame frame = new CBMainFrame("Card Battle");
				frame.setSize(1024, 768);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		});
		
	}

}

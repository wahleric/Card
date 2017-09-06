package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CBMenu extends JMenuBar {
	
	public CBMenu() {
		super();
		
		/*
		 * Create "Game" menu
		 */
		
		JMenu game = new JMenu("Game");
		game.setMnemonic(KeyEvent.VK_G);
		game.getAccessibleContext().setAccessibleDescription("Game options");
		
		//Create "New Game" menu
		JMenu newGame = new JMenu("New Game    [N]");
		newGame.setMnemonic(KeyEvent.VK_N);
		
		// Create "Easy", "Medium", and "Hard" menu items
		JMenuItem easy = new JMenuItem("Easy	[E]");
		easy.setMnemonic(KeyEvent.VK_E);
		JMenuItem medium = new JMenuItem("Medium	[M]");
		medium.setMnemonic(KeyEvent.VK_M);
		JMenuItem hard = new JMenuItem("Hard	[H]");
		hard.setMnemonic(KeyEvent.VK_H);
		
		// Create action for "Easy", "Medium", and "Hard" menu items
		
		// Create "Quit Card Battle" menu item
		JMenuItem quit = new JMenuItem("Quit Card Battle    [Q]");
		quit.setMnemonic(KeyEvent.VK_Q);
		
		// Add action for "Quit"
		quit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		
		
		// Add all items to "Game"
		game.add(newGame);
		newGame.add(easy);
		newGame.add(medium);
		newGame.add(hard);
		game.add(quit);
		
		// Add "Game" to menu bar
		
		add(game);
		
		/*
		 * Create "About" tab
		 */
		
		JMenu about = new JMenu("About");
		about.setMnemonic(KeyEvent.VK_A);
		
		// Add "About Card Battle" menu item
		
		JMenuItem aboutCB = new JMenuItem("About Card Battle    [A]");
		aboutCB.setMnemonic(KeyEvent.VK_C);
		
		// Add action for "About"
		
		CBAbout aboutFrame = new CBAbout("About");
		aboutFrame.setSize(300, 200);
		
		aboutCB.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				aboutFrame.setVisible(true);
			}
		});
		
		// Add all items to "About"
		
		about.add(aboutCB);
		
		// Add "About" to menu bar
		add(about);
	}
}

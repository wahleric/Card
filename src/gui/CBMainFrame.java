package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CBMainFrame extends JFrame {
	
	public CBMainFrame(String title) {
		super(title);
		
		// Set layout manager
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		
		/*
		 * Create Swing components
		 */
		
		// Create menu bar
		CBMenu menu = new CBMenu();
		
		// Add swing components to content pane
		Container c = getContentPane();
		c.setBackground(Color.BLACK);
		c.add(menu, BorderLayout.NORTH);
		
	}

}

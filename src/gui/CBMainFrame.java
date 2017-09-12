package gui;
import javax.swing.*;
import java.awt.*;

public class CBMainFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

	public CBMainFrame(String title) {
		super(title);
		
		// Set layout manager
		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.BLACK);
		
		/*
		 * Create Swing components
		 */
		
		// Add swing components
		setJMenuBar(new CBMenu());
		
	}

}

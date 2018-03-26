package gui;
import javax.swing.*;

import Main.Board;
import Main.CardBattleAI;

import java.awt.*;

public class CBMainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public CBMainFrame(String title, Board board, CardBattleAI ai) {
		super(title);
		
		// Set layout manager
		setLayout(new BorderLayout());
		
		/*
		 * Create Swing components
		 */
		
		// Create the Board view panel
		ViewPanel view = new ViewPanel(board);
		
		// Game control panel
		ControlPanel control = new ControlPanel(view, board);
		
		// Add swing components
		setJMenuBar(new CBMenu(board, view));
		add(view, BorderLayout.CENTER);
		add(control, BorderLayout.EAST);
	}

}

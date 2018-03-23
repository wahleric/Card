package gui;
import java.io.*;
import java.awt.*;
import javax.swing.*;

import Main.Board;
import Main.Card;
import Main.CardBattleAI;
import Main.Player;

public class CardBattleGUI {
	
	public static void main(String[] args) throws IOException {
	    
	    Player human = new Player("Human");
	    Board board = new Board(human, new Player("Computer"));
	    board.setDifficulty("Easy");
	    CardBattleAI ai = new CardBattleAI(board);
	    ai.initialDraw();
	    
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CBMainFrame frame = new CBMainFrame("Card Battle", board, ai);
				frame.setPreferredSize(new Dimension(1000, 1100));
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
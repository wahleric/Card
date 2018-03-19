package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Main.*;

public class ControlPanel extends JPanel {
    
    private static final Color BORDER_COLOR = Color.GRAY;
    private ViewPanel view;
    private Board board;
    
    public ControlPanel(ViewPanel view, Board board) {
        this.view = view;
        this.board = board;
        setPreferredSize(new Dimension(1024, 100));
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, BORDER_COLOR));
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        int x = 15;
        int y = 15;
        for (Card card : board.getHumanPlayer().getHand()) {
            g.drawRect(x, y, 25, 25);
            x += 25;
            y += 25;
        }
    }

}

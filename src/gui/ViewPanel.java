package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Main.Board;
import Main.Card;

public class ViewPanel extends JPanel {
    
    public static final Color BACKGROUND_COLOR = Color.BLACK;
    public static final Color BORDER_COLOR = Color.GRAY;

    private static final long serialVersionUID = 1L;
    
    private Board board;
    private CardPanel[][] cards;

    public ViewPanel(Board board) {
        
        // Set panel attributes
        this.board = board;
        this.cards = new CardPanel[5][5];
        setPreferredSize(new Dimension(1024, 768));
        setDoubleBuffered(true);
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, BORDER_COLOR));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        // Add the Card panels
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                CardPanel cardPanel = new CardPanel(null);
                cards[i][j] = cardPanel;
                add(cardPanel, gc);
                gc.gridx++;
            }
            gc.gridx = 0;
            gc.gridy++;
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(BACKGROUND_COLOR);
        g.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cards[i][j].updateCard(board.getCardAtNode((i * 5) + (j + 1)));
            }
        }
    }

}

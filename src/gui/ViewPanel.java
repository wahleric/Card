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
    private CardPanel[] slots;

    public ViewPanel(Board board) {
        
        // Set panel attributes
        this.board = board;
        this.slots = new CardPanel[25];
        setPreferredSize(new Dimension(1024, 768));
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, BORDER_COLOR));
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        // Add the Card panels
        for (int i = 0; i < 5; i++) {
            for (int j = 1; j < 6; j++) {
                CardPanel cardPanel = new CardPanel(board.getCardAtNode((i * 5) + j));
                add(cardPanel, gc);
                slots[(i * 5) + j - 1] = cardPanel;
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
        for (int i = 0; i < 25; i++) {
            slots[i].repaint();
        }
    }

}

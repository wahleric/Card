package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
        
        // Add cards
        
        // Add buttons
        
        /**
         * TEST BUTTON
         */
        JButton test = new JButton("Test");
        test.addActionListener(new ActionListener () {
            
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                int slot = r.nextInt(24) + 1;
                board.placeCard(board.getHumanPlayer().getHand().get(0), slot);
                System.out.println(slot);
                view.repaint();
            }
        });
        
        add(test, BorderLayout.SOUTH);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
    }

}

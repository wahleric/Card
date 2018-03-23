package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Main.*;

public class ControlPanel extends JPanel {
    
    private static final Color BORDER_COLOR = Color.GRAY;
    private ViewPanel view;
    private Board board;
    
    public ControlPanel(ViewPanel view, Board board) {
        this.view = view;
        this.board = board;
        setPreferredSize(new Dimension(1000, 100));
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, BORDER_COLOR));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        
        // Add cards
        
        // Add buttons
        
        /**
         * TEST ADD BUTTON
         */
        JButton test = new JButton("Test");
        test.addActionListener(new ActionListener () {
            
            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                int slot = r.nextInt(24) + 1;
                int player = r.nextInt(2);
                if (player == 0) {
                    board.placeCard(board.getHumanPlayer().getHand().get(0), slot / 5, slot % 5);
                } else {
                    board.placeCard(board.getComputerPlayer().getHand().get(0), slot / 5, slot % 5);
                }
                
                System.out.println("Row: " + slot / 5 + ", Column: " + slot % 5);
                view.repaint();
            }
        });
        
        /**
         * TEST SELECT ROW/COLUMN
         */
        
        JLabel rowLabel = new JLabel();
        rowLabel.setText("Row: ");
        JTextField rowSelector = new JTextField(2);
        
        JLabel columnLabel = new JLabel();
        columnLabel.setText("Column: ");
        JTextField columnSelector = new JTextField(2);
        
        
        /**
         * TEST REMOVE BUTTON
         */
        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener () {
            
            public void actionPerformed(ActionEvent e) {
                String rowString = rowSelector.getText();
                String columnString = columnSelector.getText();
                try {
                    int row = Integer.parseInt(rowString);
                    int column = Integer.parseInt(columnString);
                    board.removeCard(row, column);
                } catch (Exception ex) {
                    System.out.println("Row and columns must be a valid number 0 - 4");
                }
                view.repaint();
            }
        });
        
        gc.gridx = 0;
        gc.gridy = 0;
        add(rowLabel, gc);
        gc.gridx = 1;
        add(rowSelector, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        add(columnLabel, gc);
        gc.gridx = 1;
        add(columnSelector, gc);
        
        gc.gridx = 2;
        gc.gridy = 0;
        add(test, gc);
        gc.gridy = 1;
        add(remove, gc);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
    }

}

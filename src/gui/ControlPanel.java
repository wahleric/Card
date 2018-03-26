package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Main.*;

public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Color BORDER_COLOR = Color.GRAY;
    private ViewPanel view;
    private Board board;
    private int[] selectedSlot;
    private int selectedCard;

    public ControlPanel(ViewPanel view, Board board) {
        this.view = view;
        this.board = board;
        this.selectedSlot = new int[2];
        selectedSlot[0] = -1;
        selectedSlot[1] = -1;
        this.selectedCard = -1;
        setPreferredSize(new Dimension(300, 900));
        this.setBorder(BorderFactory.createMatteBorder(0, 1, 2, 1, BORDER_COLOR));
        this.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();

        view.addMouseListener(new ViewListener());

        /**
         * TEST ADD BUTTON
         */
        JButton test = new JButton("Test");
        test.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Random r = new Random();
                int player = r.nextInt(2);
                if (selectedSlot[0] > -1 && selectedSlot[1] > -1) {
                    if (player == 0) {
                        board.placeCard(board.getHumanPlayer().getHand().get(0), selectedSlot[0], selectedSlot[1]);
                    } else {
                        board.placeCard(board.getComputerPlayer().getHand().get(0), selectedSlot[0], selectedSlot[1]);
                    }
                }
                view.repaint();
            }
        });

        /**
         * TEST REMOVE BUTTON
         */
        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (selectedSlot[0] > -1 && selectedSlot[1] > -1) {
                    if (board.getCard(selectedSlot[0], selectedSlot[1]) != null) {
                        board.removeCard(selectedSlot[0], selectedSlot[1]);
                    }
                }
                view.repaint();
            }
        });

        gc.gridx = 2;
        gc.gridy = 0;
        add(test, gc);
        gc.gridy = 1;
        add(remove, gc);
        
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.LIGHT_GRAY);
        List<Card> hand = board.getHumanPlayer().getHand();
        for (int i = 0; i < hand.size(); i++) {
            Card card = board.getHumanPlayer().getHand().get(i);
            g.drawImage(view.drawCard(card, null, i == selectedCard), 100, 50 + (120 * i), 120, 120, this);
        }
    }

    private class ViewListener implements MouseListener {

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            int row = e.getY() / (view.getHeight() / 5);
            int column = e.getX() / (view.getWidth() / 5);
            selectedSlot[0] = row;
            selectedSlot[1] = column;
            view.updateSelected(row, column);
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }
    }
    
    private class cardLabel extends JLabel {
        
        Card card;
        
        public cardLabel(Card card) {
            super();
            this.card = card;
        }
        
        public void paintComponent(Graphics g) {
            
        }
    }
}

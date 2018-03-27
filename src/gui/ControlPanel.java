package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Main.*;

public class ControlPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final Color BORDER_COLOR = Color.GRAY;
    private ViewPanel view;
    private Board board;
    private int[] selectedSlot;
    private int selectedCard;
    private JLabel gameInfo;
    private JTextArea cardInfo;

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
        JButton test = new JButton("Add");
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
                cardInfo.setText(getCardInfo(board.getCard(selectedSlot[0], selectedSlot[1])));
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
                cardInfo.setText("");
            }
        });

        // Create JLabel, which shows information about the state of the game
        this.cardInfo = new JTextArea();
        cardInfo.setEditable(false);
        cardInfo.setPreferredSize(new Dimension(280, 200));
        cardInfo.setBackground(Color.BLACK);
        cardInfo.setFont(ViewPanel.CARD_FONT.deriveFont(20));
        cardInfo.setForeground(Color.WHITE);

        gc.gridx = 0;
        gc.gridy = 0;
        add(test, gc);
        gc.gridx = 1;
        add(remove, gc);
        gc.weighty = 0.1;
        gc.gridwidth = 2;
        gc.gridx = 0;
        gc.gridy = 1;
        gc.anchor = GridBagConstraints.PAGE_END;
        gc.insets = new Insets(0, 0, 10, 0);
        add(cardInfo, gc);

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
            Card card = board.getCard(row, column);
            if (card == null) {
                cardInfo.setText("");
            } else {
                cardInfo.setText(getCardInfo(card));
            }

        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }
    }

    private String getCardInfo(Card card) {
        String s = "";
        s += "Monster: " + card.getName() + "\n";
        s += "Type: " + card.getType() + "\n";
        s += "Attack: \n";
        s += "    Upper: " + card.getCurrentUpperAP() + "\n";
        s += "    Lower: " + card.getCurrentLowerAP() + "\n";
        s += "    Left:  " + card.getCurrentLeftAP() + "\n";
        s += "    Right: " + card.getCurrentRightAP() + "\n";
        s += "Hit Points: " + card.getCurrentHP() + "/" + card.getMaxHP() + "\n";
        return s;
    }

    private class gameInfoLabel extends JLabel {

        private Board board;

        public gameInfoLabel(Board board) {
            super();
            this.setPreferredSize(new Dimension(280, 100));
            this.board = board;
        }
        
        public void paintComponent(Graphics g) {
            
        }
    }
}

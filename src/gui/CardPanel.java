package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Main.Card;

public class CardPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private static final int CARD_SIZE = 150;
    private Card card;
    
    public CardPanel(Card card) {
        
        // Set up the panel attributes
        super();
        setPreferredSize(new Dimension(CARD_SIZE, CARD_SIZE));
        this.card = card;
        setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        setBackground(Color.BLACK);
        g2d.setColor(Color.WHITE);
        
        if (card != null) {

            // Draw card's upper AP
            String upperAP = card.getCurrentUpperAP() + "";
            g2d.drawString(upperAP, (CARD_SIZE / 2) - (4*upperAP.length()), 20);
            
            // Draw card's right AP
            String rightAP = card.getCurrentRightAP() + "";
            g2d.drawString(rightAP, (CARD_SIZE - 15) - (4*rightAP.length()), CARD_SIZE / 2);
            
            // Draw card's lower AP
            String lowerAP = card.getCurrentLowerAP() + "";
            g2d.drawString(lowerAP, (CARD_SIZE / 2) - (4*upperAP.length()), CARD_SIZE - 10);
            
            // Draw card's left AP
            String leftAP = card.getCurrentLeftAP() + "";
            g2d.drawString(leftAP, 5, CARD_SIZE / 2);
            
            // Draw card name
            g2d.drawString(card.getName(), (CARD_SIZE / 2) - (4*card.getName().length()), CARD_SIZE / 3);
            
            // Draw card's HP
            String hp = card.getCurrentHP() + "/" + card.getMaxHP();
            g2d.drawString(hp, (CARD_SIZE / 2) - (4*hp.length()), (CARD_SIZE / 3) + 15);
        }         
    }
    
    public void updateCard(Card card) {
        this.card = card;
    }

}

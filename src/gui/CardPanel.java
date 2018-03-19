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
    private static final int CARD_SIZE = 120;
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
            System.out.println("TEST");
            // Draw card's upper AP
            
            
            // Draw card's right AP
            // Draw card's lower AP
            // Draw card's left AP
            
            // Center card name
            g2d.drawString(card.getName(), (CARD_SIZE / 2) - (4*card.getName().length()), CARD_SIZE / 2);
            
            // Draw card's HP
            String hp = card.getCurrentHP() + "/" + card.getMaxHP();
            g2d.drawString(hp, (CARD_SIZE / 2) - (4*hp.length()), (CARD_SIZE / 2) + 10);
        }         
    }
    
    public void setCard(Card card) {
        this.card = card;
    }

}

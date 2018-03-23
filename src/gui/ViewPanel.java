package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Main.Board;
import Main.Card;

public class ViewPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.GRAY;
    private static final Color NEUTRAL_COLOR = Color.WHITE;
    private static final Color FRIENDLY_COLOR = Color.GREEN;
    private static final Color ENEMY_COLOR = Color.RED;
    private static final Color TEXT_COLOR = Color.WHITE;

    private static final long serialVersionUID = 1L;

    private Board board;

    public ViewPanel(Board board) {

        // Set panel attributes
        this.board = board;
        setPreferredSize(new Dimension(1000, 900));
        setDoubleBuffered(true);
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(BACKGROUND_COLOR);
        BufferedImage boardImage = new BufferedImage(780, 780, BufferedImage.TYPE_INT_ARGB);
        drawBoard(boardImage.getGraphics());
        g.drawImage(boardImage, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private void drawBoard(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;

        g2d.setBackground(BACKGROUND_COLOR);
        
        Font font = new Font("Arial", Font.BOLD, 12);
        FontMetrics fm = getFontMetrics(font);

        int x = 5;
        int y = 5;

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card card = board.getCard(row, column);
                if (card == null) {

                    // Draw the empty slot background image

                    // No card in this slot, so draw it as an empty slot
                    g2d.setColor(NEUTRAL_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(x, y, 150, 150, 5, 5);
                    
                    // Draw the zone information
                    if (board.getZone(row, column) != null) {
                        String zone = "**" + board.getZone(row, column).getType() + " ZONE**";
                        Rectangle2D zoneSize = fm.getStringBounds(zone, g);
                        g2d.drawString(zone, (int)(x + ((150 - zoneSize.getWidth()) / 2.0)), y + 75);
                    }
                } else {

                    // Draw the appropriate background image of the card

                    // Determine the color to draw the slot depending on card owner
                    if (card.getOwner() == board.getHumanPlayer()) {
                        g2d.setColor(FRIENDLY_COLOR);

                    } else {
                        g2d.setColor(ENEMY_COLOR);
                    }
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawRoundRect(x, y, 150, 150, 5, 5);

                    // Draw the card's information
                    g2d.setColor(TEXT_COLOR);
        
                    g2d.setFont(font);
                    
                    // Draw the card's name
                    String name = card.getName();
                    Rectangle2D nameSize = fm.getStringBounds(name, g);
                    g2d.drawString(name, (int)(x + ((150 - nameSize.getWidth()) / 2.0)), y + 50);
                    
                    // Draw the card's HP
                    String hp = card.getCurrentHP() + "/" + card.getMaxHP();
                    Rectangle2D hpSize = fm.getStringBounds(hp, g);
                    g2d.drawString(hp, (int)(x + ((150 - hpSize.getWidth()) / 2.0)), y + 75);
                    
                    // Draw the card's upper AP
                    String upAP = card.getCurrentUpperAP() + "";
                    Rectangle2D upAPSize = fm.getStringBounds(upAP, g);
                    g2d.drawString(upAP, (int)(x + ((150 - upAPSize.getWidth()) / 2.0)), y + 15);
                    
                    // Draw the card's lower AP
                    String lowAP = card.getCurrentLowerAP() + "";
                    Rectangle2D lowAPSize = fm.getStringBounds(lowAP, g);
                    g2d.drawString(lowAP, (int)(x + ((150 - lowAPSize.getWidth()) / 2.0)), y + 145);
                    
                    // Draw the card's left AP
                    String leftAP = card.getCurrentLeftAP() + "";
                    Rectangle2D leftAPSize = fm.getStringBounds(leftAP, g);
                    g2d.drawString(leftAP, x + 5, y + 75);
                    
                    // Draw the card's right AP
                    String rightAP = card.getCurrentRightAP() + "";
                    Rectangle2D rightAPSize = fm.getStringBounds(rightAP, g);
                    g2d.drawString(rightAP, x + (int)(145 - rightAPSize.getWidth()), y + 75);
                }
                x += 155;
            }
            x = 5;
            y += 155;
        }
    }
}

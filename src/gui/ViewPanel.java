package gui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Main.Board;
import Main.Card;
import Main.Zone;

public class ViewPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.GRAY;
    private static final Color NEUTRAL_COLOR = Color.LIGHT_GRAY;
    private static final Color FRIENDLY_COLOR = Color.GREEN;
    private static final Color ENEMY_COLOR = Color.RED;
    public static final Font CARD_FONT = loadFont();

    private static final long serialVersionUID = 1L;

    private Board board;
    private int[] selected;
    Map<String, BufferedImage> backgrounds;

    public ViewPanel(Board board) {

        // Set panel attributes
        this.board = board;
        this.backgrounds = new HashMap<String, BufferedImage>();
        this.selected = new int[2];
        selected[0] = -1;
        selected[1] = -1;
        setPreferredSize(new Dimension(900, 900));
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, BORDER_COLOR));
        loadBackgroundImages();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(BACKGROUND_COLOR);
        g.drawImage(drawBoard(), 0, 0, this.getWidth(), this.getHeight(), this);
    }

    private BufferedImage drawBoard() {
        BufferedImage image = new BufferedImage(900, 900, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                Card card = board.getCard(row, column);
                Zone zone = board.getZone(row, column);
                BufferedImage cardImage = drawCard(card, zone, selected[0] == row && selected[1] == column);
                g2d.drawImage(cardImage, column * cardImage.getHeight(), row * cardImage.getWidth(),
                        cardImage.getWidth(), cardImage.getHeight(), this);
            }
        }
        return image;
    }

    protected BufferedImage drawCard(Card card, Zone zone, boolean selected) {
        BufferedImage image = new BufferedImage(180, 180, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) image.getGraphics();

        // Draw the card's background
        BufferedImage background = null;

        if (card == null) {
            if (zone == null) {
                background = backgrounds.get("Empty.jpg");
            } else {
                background = backgrounds.get(zone.getType() + ".jpg");
            }

        } else {
            background = backgrounds.get(card.getType() + ".jpg");
        }

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        g2d.drawImage(background, 5, 5, 170, 170, this);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // Draw the Card's information

        if (card == null) {
            g2d.setColor(NEUTRAL_COLOR);
            g2d.setStroke(new BasicStroke(2));

            if (zone != null) {
                String zoneString = "**" + zone.getType() + " zone**";
                int width = g2d.getFontMetrics().stringWidth(zoneString);
                g2d.drawString(zoneString, (int) ((image.getWidth() - width) / 2.0), image.getHeight() / 2);
            }

        } else {
            if (card.getOwner() == board.getHumanPlayer()) {
                g2d.setColor(FRIENDLY_COLOR);
            } else {
                g2d.setColor(ENEMY_COLOR);
            }
            g2d.setStroke(new BasicStroke(5));

            g2d.setFont(CARD_FONT);

            // Draw the card's name
            String name = card.getName();
            int width = g2d.getFontMetrics().stringWidth(name);
            g2d.drawString(name, (int) ((image.getWidth() - width) / 2.0), image.getHeight() / 3);

            // Draw the card's HP
            String hp = card.getCurrentHP() + "/" + card.getMaxHP();
            width = g2d.getFontMetrics().stringWidth(hp);
            g2d.drawString(hp, (int) ((image.getWidth() - width) / 2.0), image.getHeight() / 2);

            // Draw the card's upper AP
            String upAP = card.getCurrentUpperAP() + "";
            width = g2d.getFontMetrics().stringWidth(upAP);
            g2d.drawString(upAP, (int) ((image.getWidth() - width) / 2.0), image.getHeight() / 7);

            // Draw the card's lower AP
            String lowAP = card.getCurrentLowerAP() + "";
            width = g2d.getFontMetrics().stringWidth(lowAP);
            g2d.drawString(lowAP, (int) ((image.getWidth() - width) / 2.0),
                    image.getHeight() - (image.getHeight() / 11));

            // Draw the card's left AP
            String leftAP = card.getCurrentLeftAP() + "";
            g2d.drawString(leftAP, image.getWidth() / 10, image.getHeight() / 2);

            // Draw the card's right AP
            String rightAP = card.getCurrentRightAP() + "";
            width = g2d.getFontMetrics().stringWidth(rightAP);
            g2d.drawString(rightAP, (int) (image.getWidth() - ((image.getWidth() / 10) + width)),
                    image.getHeight() / 2);
        }

        if (selected) {
            g2d.setStroke(new BasicStroke(7));
            g2d.setColor(Color.WHITE);
        } else if (zone != null) {
            g2d.setColor(Color.YELLOW);
        }
        g2d.drawRoundRect(5, 5, 170, 170, 5, 5);
        return image;
    }

    public void updateSelected(int row, int column) {
        this.selected[0] = row;
        this.selected[1] = column;
        System.out.println("Row: " + selected[0] + ", Column: " + selected[1]);
        repaint();
    }

    private static Font loadFont() {
        Font cardFont = null;
        try {
            cardFont = Font.createFont(Font.TRUETYPE_FONT, new File("art/fonts/8bit.ttf"));
            cardFont = cardFont.deriveFont(18f);
        } catch (IOException | FontFormatException e) {
            throw new IllegalArgumentException("Error loading fonts!");
        }
        return cardFont;
    }

    private void loadBackgroundImages() {
        File directory = new File("art/types/");
        File[] images = directory.listFiles();
        for (int i = 0; i < images.length; i++) {
            if (!images[i].isHidden()) {
                try {
                    BufferedImage background = ImageIO.read(images[i]);
                    backgrounds.put(images[i].getName(), background);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Error loading card background images");
                }
            }
        }
    }
}

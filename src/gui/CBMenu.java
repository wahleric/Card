package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CBMenu extends JMenuBar {

    private static final long serialVersionUID = 1L;

    private CBAbout aboutFrame;
    private CBInstructions instructionsFrame;

    public CBMenu() {
        super();
        setOpaque(true);
        setBackground(Color.RED);

        /*
         * Create "Game" menu
         */

        JMenu game = new JMenu("Game");
        game.getAccessibleContext().setAccessibleDescription("Game options");

        // Create "New Game" menu
        JMenu newGame = new JMenu("New Game");

        // Create "Easy", "Medium", and "Hard" menu items
        JMenuItem easy = new JMenuItem("Easy");
        JMenuItem medium = new JMenuItem("Medium");
        JMenuItem hard = new JMenuItem("Hard");

        // Create action for "Easy", "Medium", and "Hard" menu items

        // Create "Quit Card Battle" menu item
        JMenuItem quit = new JMenuItem("Quit Card Battle");

        // Add action for "Quit"
        quit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Add all items to "Game"
        game.add(newGame);
        newGame.add(easy);
        newGame.add(medium);
        newGame.add(hard);
        game.add(quit);

        // Add "Game" to menu bar

        add(game);

        /*
         * Create "Help" tab
         */

        JMenu help = new JMenu("Help");

        // Add "Instructions" menu item

        JMenuItem instructionsCB = new JMenuItem("Instructions");

        // Add action for "Instructions"

        instructionsFrame = new CBInstructions();
        instructionsFrame.setSize(850, 650);
        instructionsFrame.setLocationRelativeTo(null);
        instructionsFrame.setResizable(false);

        instructionsCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                instructionsFrame.setVisible(true);
            }
        });

        // Add "About Card Battle" menu item

        JMenuItem aboutCB = new JMenuItem("About Card Battle");

        // Add action for "About Card Battle"

        aboutFrame = new CBAbout();
        aboutFrame.setSize(320, 240);
        aboutFrame.setLocationRelativeTo(null);

        aboutCB.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                aboutFrame.setVisible(true);
            }
        });

        // Add all items to "Help"

        help.add(instructionsCB);
        help.add(aboutCB);

        // Add "Help" to menu bar
        add(help);
    }
}

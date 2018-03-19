package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CBAbout extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = Color.GRAY;

    public CBAbout() {

        super("About");

        // Set the window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(0, 3, 3, 3, BORDER_COLOR));

        // Create text for Title
        JTextArea gameName = new JTextArea();
        gameName.setBackground(BACKGROUND_COLOR);
        gameName.setForeground(TEXT_COLOR);
        gameName.append("Card Battle");

        // Create text for Version
        JTextArea version = new JTextArea();
        version.setBackground(BACKGROUND_COLOR);
        version.setForeground(TEXT_COLOR);
        version.append("Version 1.0");

        // Create text for Author
        JTextArea author = new JTextArea();
        author.setBackground(BACKGROUND_COLOR);
        author.setForeground(TEXT_COLOR);
        author.append("Author: Eric Wahlquist");

        // Create text for e-mail
        JTextArea email = new JTextArea();
        email.setBackground(BACKGROUND_COLOR);
        email.setForeground(TEXT_COLOR);
        email.append("E-mail: wahlquisteric@gmail.com");

        // Create button for close
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        // Create the inner panel to add these to
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(new GridBagLayout());

        // Add all components to the "About" frame

        GridBagConstraints gc = new GridBagConstraints();
        gc.anchor = GridBagConstraints.CENTER;

        gc.weightx = 0.5;
        gc.weighty = 0.5;

        gc.gridx = 0;
        gc.gridy = 0;

        panel.add(gameName, gc);

        gc.gridy = 1;

        panel.add(version, gc);

        gc.gridy = 2;

        panel.add(author, gc);

        gc.gridy = 3;

        panel.add(email, gc);

        gc.gridy = 4;

        panel.add(close, gc);

        add(panel, BorderLayout.CENTER);
    }
}

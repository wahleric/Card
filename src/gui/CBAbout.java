package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CBAbout extends JFrame {

	public CBAbout(String title) {
		
		super(title);
		
		setLayout(new GridBagLayout());
		
		// Create text for Title
		JTextArea gameName = new JTextArea();
		gameName.append("Card Battle");
		
		// Create text for Version
		JTextArea version = new JTextArea();
		version.append("Version 1.0");
		
		// Create text for Author
		JTextArea author = new JTextArea();
		author.append("Author: Eric Wahlquist");
		
		// Create text for e-mail
		JTextArea email = new JTextArea();
		email.append("E-mail: wahlquisteric@gmail.com");
		
		// Create button for close
		JButton close = new JButton("Close");
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		
		// Add all components to the "About" frame
		
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.CENTER;
		
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;
		
		add(gameName, gc);
		
		gc.gridy = 1;
		
		add(version, gc);
		
		gc.gridy = 2;
		
		add(author, gc);
		
		gc.gridy = 3;
		
		add(email, gc);
		
		gc.gridy = 4;
		
		add(close, gc);
	}
}

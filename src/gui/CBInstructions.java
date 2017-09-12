package gui;

import javax.swing.*;
import java.awt.*;

public class CBInstructions extends JFrame {

	private static final long serialVersionUID = 1L;

	public CBInstructions() {

		super("Instructions");

		setLayout(new BorderLayout());
		getContentPane().setBackground(Color.BLACK);
		// Create the tabbed pane and prepare each tab

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent gamePanel = makeTextPanel(new ImageIcon("InstructionsGame.jpg"));
		JComponent cardPanel = makeTextPanel(new ImageIcon("InstructionsCard.jpg"));
		JComponent typePanel = makeTextPanel(new ImageIcon("InstructionsGame.jpg"));
		JComponent zonePanel = makeTextPanel(new ImageIcon("InstructionsGame.jpg"));
		
		// Add tabs to tabbedPane
		
		tabbedPane.addTab("The Game", gamePanel);
		tabbedPane.addTab("The Cards", cardPanel);
		tabbedPane.addTab("The Types", typePanel);
		tabbedPane.addTab("The Zones", zonePanel);

		// Add tabbedPane to this frame
		
		add(tabbedPane);
	}

	private JComponent makeTextPanel(ImageIcon picture) {
		JPanel panel = new JPanel(false);
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		JLabel label = new JLabel();
		label.setIcon(picture);
		panel.add(label, gc);
		return panel;
	}

}

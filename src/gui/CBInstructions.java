package gui;

import javax.swing.*;
import java.awt.*;

public class CBInstructions extends JFrame {
    
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BORDER_COLOR = Color.GRAY;

	private static final long serialVersionUID = 1L;

	public CBInstructions() {

		super("Instructions");

		setLayout(new BorderLayout());
		getContentPane().setBackground(BACKGROUND_COLOR);
		
		// Set the window border
        getRootPane().setBorder(BorderFactory.createMatteBorder(0, 3, 3, 3, BORDER_COLOR));
        
		// Create the tabbed pane and prepare each tab

		JTabbedPane tabbedPane = new JTabbedPane();

		JComponent gamePanel = makeTextPanel(new ImageIcon("art/instructionsGame.jpg"));
		JComponent cardPanel = makeTextPanel(new ImageIcon("art/instructionsCard.jpg"));
		JComponent typePanel = makeTextPanel(new ImageIcon("art/instructionsTypes.jpg"));
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

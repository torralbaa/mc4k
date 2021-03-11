/*
 * MCFrame.java - Helper class.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k;

import java.awt.Panel;
import java.net.URL;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class MCFrame extends JFrame {
	public static MCFrame other;
	public static Panel panel;

	public MCFrame(int width, int height, Panel tmpPanel) {
		panel = tmpPanel;

		add(panel);
		pack();

		setSize(width, height);
		setResizable(false);
		setTitle("mc4k - Minecraft 4k");

		URL iconURL = MCFrame.class.getResource("/res/icon.png");
		if (iconURL != null) {
			ImageIcon icon = new ImageIcon(iconURL);
			setIconImage(icon.getImage());
		}
	}
}

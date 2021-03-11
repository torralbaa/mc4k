/*
 * MCSettings.java - Main settings logic/interface.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.imageio.ImageIO;

import mc4k.MCFrame;

public class MCSettings extends Panel implements Runnable, KeyListener {
	public static MCFrame frame;

	public boolean runMC = false;
	public String worldName = null;
	public String worldsDirectoryName = System.getProperty("user.home") + "/.mc4k";
	public String worldDirectoryName = null;
	public String worldFileName = null;
	public String playerFileName = null;

	String titleString = "mc4k - Minecraft 4k";
	String worldNameLabelString = "World name: ";
	int titleWidth = 0;
	int worldNameWidth = 0;
	int worldNameLabelWidth = 0;
	int width = 0;
	int height = 0;
	int i = 1;
	BufferedImage backgroundImage;
	BufferedImage screenshotImage;
	Graphics graphics;
	Graphics panelGraphics;
	StringBuffer inputBuffer = new StringBuffer("world");
	Font titleFont = new Font("", Font.BOLD, 26);
	Font inputFont = new Font("", Font.PLAIN, 18);
	BufferStrategy strategy;

	public void start() {
		mkdirp(worldsDirectoryName);
		panelGraphics = getGraphics();
		try {
			screenshotImage = ImageIO.read(MCSettings.class.getResourceAsStream("/res/screenshot.png"));
		} catch (Exception err) {
			err.printStackTrace();
			System.exit(-1);
		}

		width = frame.getWidth();
		height = frame.getHeight();

		backgroundImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		graphics = backgroundImage.createGraphics();

		graphics.setFont(titleFont);
		titleWidth = graphics.getFontMetrics().stringWidth(titleString);
		graphics.setFont(inputFont);
		worldNameLabelWidth = graphics.getFontMetrics().stringWidth(worldNameLabelString);
		(new Thread(this)).start();
		addKeyListener(this);
	}

	public void run() {
		while (true) {
			graphics.setColor(Color.WHITE);
			graphics.drawImage(screenshotImage, 0, 0, width, height, (ImageObserver)null);
			graphics.setFont(titleFont);
			graphics.drawString(titleString, width / 2 - titleWidth / 2, 32);
			graphics.setFont(inputFont);
			graphics.drawString(worldNameLabelString, width / 2 - worldNameLabelWidth, 110);
			graphics.drawString(inputBuffer.toString(), width / 2, 110);
			worldNameWidth = graphics.getFontMetrics().stringWidth(inputBuffer.toString());
			if (i % 25 != 0) {
				graphics.drawRect(width / 2 + worldNameWidth + 2, 95, 6, 15);
				i++;
			} else {
				if (i > 1000) {
					i = 0;
				}
				i++;
			}
			panelGraphics.drawImage(backgroundImage, 0, 0, width, height, (ImageObserver)null);

			try {
				Thread.sleep(1000/30);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
		}
	}

	public void mkdirp(String directoryName) {
		File directory = new File(directoryName);
		if (directory.exists() && !directory.isDirectory()) {
			System.out.println(directory + " exists but isn't a directory. Aborting.");
			System.out.println(-1);
		} else if (!directory.exists()) {
			directory.mkdir();
		}
	}

	@Override
	public void keyPressed(KeyEvent paramEvent) {
		int keyCode = paramEvent.getKeyCode();
		if (keyCode == 0x0a) { // Enter
			if (inputBuffer.length() <= 0) {
				worldName = "world";
			} else {
				worldName = inputBuffer.toString();
			}
			worldDirectoryName = worldsDirectoryName + "/" + worldName;
			mkdirp(worldDirectoryName);
			runMC = true;
			frame.setFocusable(false);
			graphics.dispose();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		} else if (keyCode >= 0x30 && (keyCode <= 0x39 || (keyCode >= 0x61 && keyCode <= 0x7a))) { // [0-9a-z]
			if (inputBuffer.length() <= 12) {
				inputBuffer.append((char)keyCode);
			}
		} else if (keyCode >= 0x41 && keyCode <= 0x5a) { // [A-Z]
			if (inputBuffer.length() <= 12) {
				inputBuffer.append((char)(keyCode + 0x20));
			}
		} else if (keyCode == 0x08) { // Back/delete
			if (inputBuffer.length() > 0) {
				inputBuffer.deleteCharAt(inputBuffer.length() - 1);
				graphics.clearRect(frame.getWidth() / 2, 75, frame.getWidth() / 2 + worldNameWidth * 2, 125);
			}
		} else if (keyCode == 0x1b) { // Esc
			graphics.dispose();
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		}
		return;
	}

	@Override
	public void keyReleased(KeyEvent paramEvent) {
		return;
	}

	@Override
	public void keyTyped(KeyEvent paramEvent) {
		return;
	}
}

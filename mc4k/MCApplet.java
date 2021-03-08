/*
 * MCApplet.java - Helper class.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k;

import java.awt.Panel;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;

class MCEvents {
	public int mouseX = 0;
	public int mouseY = 0;
	public int[] wasd = new int[4]; // WASD
	public int[] buttons = new int[2]; // Left, Right
	public int[] arrows = new int[2]; // Left, Right
	public int[] worldKeys = new int[4]; // Esc, C, G, F2
	public int space = 0; // Space
	public int mouseLocked = 1; // 0: Locked, 1: Game not started, 2: Focus lost
}

public class MCApplet extends Panel implements MouseMotionListener, MouseListener, KeyListener {
	public MCEvents events = new MCEvents();
	public static Frame frame = new Frame();

	@Override
	public void mouseDragged(MouseEvent paramEvent) {
		if (events.mouseLocked != 0)
		{
			return;
		}
		events.mouseX = paramEvent.getX();
		events.mouseY = paramEvent.getY();
	}

	@Override
	public void mouseMoved(MouseEvent paramEvent) {
		if (events.mouseLocked != 0)
		{
			return;
		}
		events.mouseX = paramEvent.getX();
		events.mouseY = paramEvent.getY();
	}

	@Override
	public void mousePressed(MouseEvent paramEvent) {
		if (events.mouseLocked != 0)
		{
			return;
		}
		events.mouseX = paramEvent.getX();
		events.mouseY = paramEvent.getY();
		if ((paramEvent.getModifiers() & 4) <= 0)
			events.buttons[0] = 1;
		else
			events.buttons[1] = 1;
	}

	private void setKeyEvent(KeyEvent paramEvent, int val) {
		switch (paramEvent.getKeyCode()) {
			case 27:
				events.worldKeys[0] = val;
			break;
			case 67:
				events.worldKeys[1] = val;
			break;
			case 71:
				events.worldKeys[2] = val;
			break;
			case 113:
				events.worldKeys[3] = val;
			break;
			case 37:
				events.arrows[0] = val;
			break;
			case 39:
				events.arrows[1] = val;
			break;
			case 87:
				events.wasd[0] = val;
			break;
			case 65:
				events.wasd[1] = val;
			break;
			case 83:
				events.wasd[2] = val;
			break;
			case 68:
				events.wasd[3] = val;
			break;
			case 32:
				events.space = val;
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent paramEvent) {
		setKeyEvent(paramEvent, 1);
	}

	@Override
	public void keyReleased(KeyEvent paramEvent) {
		setKeyEvent(paramEvent, 0);
	}

	@Override
	public void mouseExited(MouseEvent paramEvent) {
		return;
	}

	@Override
	public void mouseClicked(MouseEvent paramEvent) {
		if (events.mouseLocked == 1) {
			events.mouseLocked = 0;
		}
		return;
	}

	@Override
	public void mouseEntered(MouseEvent paramEvent) {
		return;
	}

	@Override
	public void mouseReleased(MouseEvent paramEvent) {
		return;
	}

	@Override
	public void keyTyped(KeyEvent paramEvent) {
		return;
	}
}
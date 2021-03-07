/*
 * Minecraft4K.java - Minecraft 4K, but fixed.
 * 
 * Copyright 2009 Notch
 * Copyright 2019 @samsebe at the MCForums
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
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.time.Instant;

public class Minecraft4K extends Panel implements Runnable, MouseMotionListener, MouseListener, KeyListener {
	private int mouseLocked = 0;
	private int[] eventMap = new int[32767];
	private static Frame frame = new Frame();

	int imageWidth = 214 * 2;
	int imageHeight = 120 * 2;
	int screenWidth = 856 * 1;
	int screenHeigth = 480 * 1;

	Random random = new Random();
	BufferedImage screenImage = new BufferedImage(imageWidth, imageHeight, 1);

	int[] screenImagePixels = ((DataBufferInt)screenImage.getRaster().getDataBuffer()).getData();
	int[] worldBlocks = new int[262144];
	int[] texturePixels = new int[12288];

	float posX = 96.5F;
	float posZ = 95.0F;
	float posY = 96.5F;

	int blockInHand = 1;
	int eventIndicator = 0;
	float viewFov = 3F;
	double DRAW_DISTANCE = 20.0D;

	String worldFile = "world.dat";

	public static void main(String[] args) {
		Minecraft4K mc4k = new Minecraft4K();
		mc4k.setSize(840, 480);
		frame.add(mc4k);
		frame.pack();
		mc4k.start();
		frame.setSize(840, 480);
		frame.show();
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				mc4k.fileSave(mc4k.worldFile, mc4k.worldBlocks);
				System.exit(0);
			};
		});

	}

	public void start() {
		(new Thread(this)).start();
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);
		random.setSeed(Instant.now().getEpochSecond());

		try {
			texturePixels = resourceLoad("textures.dat", texturePixels.length);
		} catch (Exception err)
		{
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			err.printStackTrace();
		}

		for(int i = 0; i < 262144; ++i) {
//			worldBlocks[i] = i / 64 % 64 > 32?random.nextInt(8) + 1:0;
			worldBlocks[i] = i / 64 % 64 > 32?4:0;
		}

	}

	public void run() {
		gameLoop();
	}

	public void gameLoop() {
		float moveVelocityX = 0.0F;
		float moveVelocityZ = 0.0F;
		float moveVelocityY = 0.0F;
		long lastTimeStamp = System.currentTimeMillis();
		int blockThatYouLookOn = -1;
		int blockNextToOneThatYouLookOn = 0;
		float viewRotationSpeedHorisontalAxis = 0.0F;
		float viewRotationSpeedVerticalAxis = 0.0F;

		while (true) {
			float smthWithHorisontalAxisSin = (float)Math.sin((double)viewRotationSpeedHorisontalAxis);
			float smthWithHorisontalAxisCos = (float)Math.cos((double)viewRotationSpeedHorisontalAxis);
			float smthWithVerticalAxisSin = (float)Math.sin((double)viewRotationSpeedVerticalAxis);
			float smthWithVerticalAxisCos = (float)Math.cos((double)viewRotationSpeedVerticalAxis);

			float smthWithMouseAndBlockChoice = -1.0F;
			int smthWithCameraX;
			int smthWithCameraZ;
			int smthWithCameraY;

			for (smthWithCameraY = 0; smthWithCameraY < imageWidth; ++smthWithCameraY) {
				float otherVelocityY = (float)(smthWithCameraY - imageWidth / 2) / 90.0F;
				for (smthWithCameraX = 0; smthWithCameraX < imageHeight; ++smthWithCameraX) {
					float viewAngle = (float)(smthWithCameraX - imageHeight / 2) / 90.0F;
					float smthStretchXandY = viewFov * smthWithVerticalAxisCos + viewAngle * smthWithVerticalAxisSin;
					float smthStretchZ = viewAngle * smthWithVerticalAxisCos - viewFov * smthWithVerticalAxisSin;
					float smthStretchX = otherVelocityY * smthWithHorisontalAxisCos + smthStretchXandY * smthWithHorisontalAxisSin;
					float smthStretchY = smthStretchXandY * smthWithHorisontalAxisCos - otherVelocityY * smthWithHorisontalAxisSin;
					int smthWithBlockColor = 0;
					int smthOverallColor = 255;
					double drawDistance = DRAW_DISTANCE;
					float blockReach = 5.0F;

					int smthWithTopAndBottomOfBlockDraw;
					for (smthWithTopAndBottomOfBlockDraw = 0; smthWithTopAndBottomOfBlockDraw < 3; ++smthWithTopAndBottomOfBlockDraw) {
						float smthWithBlockSide = smthStretchX;

						if (smthWithTopAndBottomOfBlockDraw == 1) {
							smthWithBlockSide = smthStretchZ;
						} else if (smthWithTopAndBottomOfBlockDraw == 2) {
							smthWithBlockSide = smthStretchY;
						}

						float smthWithBlockSideDraw = 1.0F / (smthWithBlockSide < 0.0F ? - smthWithBlockSide : smthWithBlockSide);
						float smthWithBlockTextureDrawXAxis = smthStretchX * smthWithBlockSideDraw;
						float smthWithBlockTextureDrawZAxis = smthStretchZ * smthWithBlockSideDraw;
						float smthWithBlockTextureDrawYAxis = smthStretchY * smthWithBlockSideDraw;
						float smthWithBlockTextureMovement = posX - (float)((int)posX);
						if (smthWithTopAndBottomOfBlockDraw == 1) {
							smthWithBlockTextureMovement = posZ - (float)((int)posZ);
						} else if (smthWithTopAndBottomOfBlockDraw == 2) {
							smthWithBlockTextureMovement = posY - (float)((int)posY);
						}

						if (smthWithBlockSide > 0.0F) {
							smthWithBlockTextureMovement = 1.0F - smthWithBlockTextureMovement;
						}

						float smthWithDistanceFade = smthWithBlockSideDraw * smthWithBlockTextureMovement;
						float smthWithBlockXPos = posX + smthWithBlockTextureDrawXAxis * smthWithBlockTextureMovement;
						float smthWithBlockZPos = posZ + smthWithBlockTextureDrawZAxis * smthWithBlockTextureMovement;
						float smthWithBlockYPos = posY + smthWithBlockTextureDrawYAxis * smthWithBlockTextureMovement;
						if (smthWithBlockSide < 0.0F) {
							if (smthWithTopAndBottomOfBlockDraw == 0) {
								--smthWithBlockXPos;
							} else if (smthWithTopAndBottomOfBlockDraw == 1) {
								--smthWithBlockZPos;
							} else if (smthWithTopAndBottomOfBlockDraw == 2) {
								--smthWithBlockYPos;
							}
						}

						while ((double)smthWithDistanceFade < drawDistance) {
							int worldDrawXorY = (int)smthWithBlockXPos - 64;
							int worldDrawZ = (int)smthWithBlockZPos - 64;
							int worldDrawYorX = (int)smthWithBlockYPos - 64;
							if (worldDrawXorY < 0 || worldDrawZ < 0 || worldDrawYorX < 0 || worldDrawXorY >= 64 || worldDrawZ >= 64 || worldDrawYorX >= 64) {
								break;
							}

							int blockIndex = worldDrawXorY + worldDrawZ * 64 + worldDrawYorX * 4096;
							int blockIDToDraw = worldBlocks[blockIndex];
							if (blockIDToDraw > 0) {
								int smthWithXandYDrawAxis = (int)((smthWithBlockXPos + smthWithBlockYPos) * 16.0F) & 15;
								int smthWithZDrawAxis = ((int)(smthWithBlockZPos * 16.0F) & 15) + 16;
								if (smthWithTopAndBottomOfBlockDraw == 1) {
									smthWithXandYDrawAxis = (int)(smthWithBlockXPos * 16.0F) & 15;
									smthWithZDrawAxis = (int)(smthWithBlockYPos * 16.0F) & 15;
									if (smthWithBlockTextureDrawZAxis < 0.0F) {
										smthWithZDrawAxis += 16;
									}
								}

								int smthWithBlockDrawingColor = 0x333333; // Block outline color

								if (blockIndex != blockThatYouLookOn || smthWithXandYDrawAxis > 0 && smthWithZDrawAxis % 16 > 0 && smthWithXandYDrawAxis < 15 && smthWithZDrawAxis % 16 < 15) {
									smthWithBlockDrawingColor = texturePixels[smthWithXandYDrawAxis + smthWithZDrawAxis * 16 + blockIDToDraw * 256 * 3];
								}

								if (smthWithDistanceFade < blockReach && smthWithCameraY == this.eventMap[2] / 2 && smthWithCameraX == this.eventMap[3] / 2) {
									smthWithMouseAndBlockChoice = (float)blockIndex;
									byte var65 = 1;
									if (smthWithBlockSide > 0.0F) {
										var65 = -1;
									}

									blockNextToOneThatYouLookOn = var65 << 6 * smthWithTopAndBottomOfBlockDraw;
									blockReach = smthWithDistanceFade;
								}

								if (smthWithBlockDrawingColor > 0) {
									smthWithBlockColor = smthWithBlockDrawingColor;
									smthOverallColor = 255 - (int)(smthWithDistanceFade / 20.0F * 255.0F);
									smthOverallColor = smthOverallColor * (255 - (smthWithTopAndBottomOfBlockDraw + 2) % 3 * 50) / 255;
									drawDistance = (double)smthWithDistanceFade;
								}
							}
							smthWithBlockXPos += smthWithBlockTextureDrawXAxis;
							smthWithBlockZPos += smthWithBlockTextureDrawZAxis;
							smthWithBlockYPos += smthWithBlockTextureDrawYAxis;
							smthWithDistanceFade += smthWithBlockSideDraw;
						}
					}

					int redChannel = (smthWithBlockColor >> 16 & 255) * smthOverallColor / 255;
					int greenChannel = (smthWithBlockColor >> 8 & 255) * smthOverallColor / 255;
					int blueChannel = (smthWithBlockColor & 255) * smthOverallColor / 255;
					screenImagePixels[smthWithCameraY + smthWithCameraX * imageWidth] = redChannel << 16 | greenChannel << 8 | blueChannel;
				}
			}

			blockThatYouLookOn = (int)smthWithMouseAndBlockChoice;
			try {
				Thread.sleep(2L);
			} catch (InterruptedException err) {
				err.printStackTrace();
			}
			this.getGraphics().drawImage(screenImage, 0, 0, screenWidth, screenHeigth, (ImageObserver)null);

			if (mouseLocked == 0) {
				continue;
			}

			// Save world
			if (this.eventMap[71] == 1) { // 'G'
				fileSave(worldFile, worldBlocks);
			}

			// Load world
			if (this.eventMap[67] == 1) { // 'C'
				try {
					worldBlocks = fileLoad(worldFile, worldBlocks.length);
				} catch (Exception err) {
					err.printStackTrace();
				}
			}

			// Close
			if (this.eventMap[27] == 1) { // Esc
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			}

			// Chose block to place
			if (this.eventMap[39] == 1) {
				if (blockInHand < 15) {
					blockInHand++;
				} else {
					blockInHand = 1;
				}
				this.eventMap[99] = 0;
			}
			if (this.eventMap[37] == 1) {
				if (blockInHand > 1) {
					blockInHand--;
				} else
				{
					blockInHand = 15;
				}
				this.eventMap[97] = 0;
			}

			// Movement
			movement_label:
				while (System.currentTimeMillis() - lastTimeStamp > 10L) {
					float moveX = 0;
					float moveY = 0;
					float velocityX;
					float velocityZ;
					float velocityY;

					// Mouse stuff
					if (this.eventMap[102] == 1 || this.eventMap[100] == 1 || this.eventMap[98] == 1 || this.eventMap[104] == 1) {
						moveX = (this.eventMap[102] - this.eventMap[100]) * 4;
						moveY = (this.eventMap[98] - this.eventMap[104]) * 4;
					} else {
						if (this.eventMap[2] > 0) {
							moveX = (float)(this.eventMap[2] - screenWidth / 2) / (screenWidth / 4) * 2.0F;
							moveY = (float)(this.eventMap[3] - screenHeigth / 2) / (screenHeigth / 4) * 2.0F;
						}
					}

					float viewRotationSpeed = (float)Math.sqrt((double)(moveX * moveX + moveY * moveY)) - 1.2F;

					if (viewRotationSpeed < 0.0F) {
						viewRotationSpeed = 0.0F;
					}
					if (viewRotationSpeed > 0.0F) {
						viewRotationSpeedHorisontalAxis += moveX * viewRotationSpeed / 400.0F;
						viewRotationSpeedVerticalAxis -= moveY * viewRotationSpeed / 400.0F;
						if (viewRotationSpeedVerticalAxis < -1.57F) {
							viewRotationSpeedVerticalAxis = -1.57F;
						}

						if (viewRotationSpeedVerticalAxis > 1.57F) {
							viewRotationSpeedVerticalAxis = 1.57F;
						}
					}

					// Keyboard stuff
					lastTimeStamp += 10L;
					moveX = 0.0F;
					moveY = 0.0F;

					moveY += (float)(this.eventMap[87] - this.eventMap[83]) * 0.02F;
					moveX += (float)(this.eventMap[68] - this.eventMap[65]) * 0.02F;

					moveVelocityX *= 0.5F;
					moveVelocityZ *= 0.99F;
					moveVelocityY *= 0.5F;
					moveVelocityX += smthWithHorisontalAxisSin * moveY + smthWithHorisontalAxisCos * moveX;
					moveVelocityY += smthWithHorisontalAxisCos * moveY - smthWithHorisontalAxisSin * moveX;
					moveVelocityZ += 0.003F;

					for (smthWithCameraX = 0; smthWithCameraX < 3; ++smthWithCameraX) {
						velocityX = posX + moveVelocityX * (float)((smthWithCameraX + 2) % 3 / 2);
						velocityZ = posZ + moveVelocityZ * (float)((smthWithCameraX + 1) % 3 / 2);
						velocityY = posY + moveVelocityY * (float)((smthWithCameraX + 2) % 3 / 2);

						for (int i = 0; i < 12; ++i) {
							int coordinateX = (int)(velocityX + (float)(i >> 0 & 1) * 0.6F - 0.3F) - 64;
							int coordinateZ = (int)(velocityZ + (float)((i >> 2) - 1) * 0.8F + 0.65F) - 64;
							int coordinateY = (int)(velocityY + (float)(i >> 1 & 1) * 0.6F - 0.3F) - 64;

							// World boundaries
							if (coordinateX < 0 || coordinateZ < 0 || coordinateY < 0 || coordinateX >= 64 || coordinateZ >= 64 || coordinateY >= 64 || worldBlocks[coordinateX + coordinateZ * 64 + coordinateY * 4096] > 0) {
								if (smthWithCameraX == 1) {
									// Jumping
									if (this.eventMap[32] > 0 && moveVelocityZ > 0.0F) {
										this.eventMap[32] = 0;
										moveVelocityZ = -0.1F;
									} else {
										moveVelocityZ = 0.0F;
									}
								}
								continue movement_label;
							}
						}
						posX = velocityX;
						posZ = velocityZ;
						posY = velocityY;
					}
				}

			// Remove block
			if ((this.eventMap[0] > 0 || this.eventMap[103] == 1) && blockThatYouLookOn > 0) {
				worldBlocks[blockThatYouLookOn] = 0;
				this.eventMap[0] = 0;
				this.eventMap[103] = 0;
			}

			// Place block
			if ((this.eventMap[1] > 0 || this.eventMap[105] == 1) && blockThatYouLookOn > 0) {
				worldBlocks[blockThatYouLookOn + blockNextToOneThatYouLookOn] = blockInHand;
				this.eventMap[1] = 0;
				this.eventMap[105] = 0;
			}

			for (int i = 0; i < 12; ++i) {
				smthWithCameraY = (int)(posX + (float)(i >> 0 & 1) * 0.6F - 0.3F) - 64;
				smthWithCameraZ = (int)(posZ + (float)((i >> 2) - 1) * 0.8F + 0.65F) - 64;
				smthWithCameraX = (int)(posY + (float)(i >> 1 & 1) * 0.6F - 0.3F) - 64;
				if (smthWithCameraY >= 0 && smthWithCameraZ >= 0 && smthWithCameraX >= 0 && smthWithCameraY < 64 && smthWithCameraZ < 64 && smthWithCameraX < 64) {
					worldBlocks[smthWithCameraY + smthWithCameraZ * 64 + smthWithCameraX * 4096] = 0;
				}
			}
		}
	}

	public void fileSave(String fileName, int[] arrayToSave) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(arrayToSave);
			out.close();
			System.out.println(fileName + " saved");
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public int[] fileLoad(String fileName, int arrayLength) throws Exception {
		int[] loadedFile = new int[arrayLength];
		ObjectInputStream in = null;

		in = new ObjectInputStream(new FileInputStream(fileName));
		loadedFile = (int[])in.readObject();
		in.close();
		System.out.println(fileName + " loaded");
		return loadedFile;
	}

	public int[] resourceLoad(String resourceName, int arrayLength) throws Exception {
		int[] loadedFile = new int[arrayLength];
		ObjectInputStream in = null;

		in = new ObjectInputStream(getClass().getResourceAsStream("/res/" + resourceName));
		loadedFile = (int[])in.readObject();
		in.close();
		System.out.println(resourceName + " loaded");
		return loadedFile;
	}

	public void mouseDragged(MouseEvent paramEvent) {
		eventMap[2] = paramEvent.getX();
		eventMap[3] = paramEvent.getY();
	}

	public void mouseMoved(MouseEvent paramEvent) {
		eventMap[2] = paramEvent.getX();
		eventMap[3] = paramEvent.getY();
	}

	public void mouseExited(MouseEvent e) {
		eventMap[2] = screenWidth / 2;
		eventMap[3] = screenHeigth / 2;
	}

	public void mousePressed(MouseEvent paramEvent) {
		eventIndicator = 1;
		eventMap[2] = paramEvent.getX();
		eventMap[3] = paramEvent.getY();
		if ((paramEvent.getModifiers() & 4) > 0)
			eventMap[1] = eventIndicator;
		else
			eventMap[0] = eventIndicator;
	}

	public void keyPressed(KeyEvent paramEvent) {
		eventIndicator = 1;
		eventMap[paramEvent.getKeyCode()] = eventIndicator;
	}

	public void keyReleased(KeyEvent paramEvent) {
		eventIndicator = 0;
		eventMap[paramEvent.getKeyCode()] = eventIndicator;
	}

	@Override
	public void mouseClicked(MouseEvent paramEvent) {
		if (mouseLocked == 0) {
			mouseLocked = 1;
		}
		return;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
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

/*
 * Minecraft4K.java - Minecraft 4K, but fixed.
 * 
 * Copyright 2009 Notch
 * Copyright 2019 @samsebe at the MCForums
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k;

import java.awt.Frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;

import mc4k.MCApplet;
import mc4k.MCTerrainGenerator;
import mc4k.MCPlayer;

public class Minecraft4K extends MCApplet implements Runnable {
	int imageWidth = 214 * 2;
	int imageHeight = 120 * 2;
	int screenWidth = 856 * 1;
	int screenHeigth = 480 * 1;

	BufferedImage screenImage = new BufferedImage(imageWidth, imageHeight, 1);

	int worldSize = 64 * 64 * 64;
	int textureSize = 12288;
	int[] screenImagePixels = ((DataBufferInt)screenImage.getRaster().getDataBuffer()).getData();
	int[] worldBlocks = new int[worldSize];
	int[] texturePixels = new int[textureSize];

	float viewFov = 3F;
	double DRAW_DISTANCE = 20.0D;

	String playerFile = "player.dat";
	String worldFile = "world.dat";
	String texturesFile = "textures.dat";

	MCPlayer player = new MCPlayer();
	MCTerrainGenerator generator = new MCTerrainGenerator();

	public static void main(String[] args) {
		Minecraft4K mc4k = new Minecraft4K();
		mc4k.setSize(856, 480);
		frame.add(mc4k);
		frame.pack();
		mc4k.start();
		frame.setSize(856, 480);
		frame.setTitle("mc4k - Minecraft 4k");
		frame.setResizable(false);

		URL iconURL = Minecraft4K.class.getResource("/res/icon.png");
		if (iconURL != null) {
			ImageIcon icon = new ImageIcon(iconURL);
			frame.setIconImage(icon.getImage());
		}

		frame.setVisible(true);
		frame.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				mc4k.fileSave(mc4k.worldFile, mc4k.worldBlocks);
				mc4k.playerSave(mc4k.playerFile, mc4k.player);
				System.exit(0);
			};
		});
	}

	public void start() {
		(new Thread(this)).start();
		addMouseMotionListener(this);
		addMouseListener(this);
		addKeyListener(this);

		try {
			texturePixels = resourceLoad(texturesFile, textureSize);
		} catch (Exception err)
		{
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
			err.printStackTrace();
		}

		try {
			worldBlocks = fileLoad(worldFile, worldSize);
		} catch (Exception err) {
			worldBlocks = generator.getBlocks();
		}

		try {
			player = playerLoad(playerFile);
		} catch (Exception err) {
			return;
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

			String diamondsString = "Diamonds: " + player.diamondCounter;
			Graphics graphics = screenImage.getGraphics();

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
						float smthWithBlockTextureMovement = player.posX - (float)((int)player.posX);
						if (smthWithTopAndBottomOfBlockDraw == 1) {
							smthWithBlockTextureMovement = player.posZ - (float)((int)player.posZ);
						} else if (smthWithTopAndBottomOfBlockDraw == 2) {
							smthWithBlockTextureMovement = player.posY - (float)((int)player.posY);
						}

						if (smthWithBlockSide > 0.0F) {
							smthWithBlockTextureMovement = 1.0F - smthWithBlockTextureMovement;
						}

						float smthWithDistanceFade = smthWithBlockSideDraw * smthWithBlockTextureMovement;
						float smthWithBlockXPos = player.posX + smthWithBlockTextureDrawXAxis * smthWithBlockTextureMovement;
						float smthWithBlockZPos = player.posZ + smthWithBlockTextureDrawZAxis * smthWithBlockTextureMovement;
						float smthWithBlockYPos = player.posY + smthWithBlockTextureDrawYAxis * smthWithBlockTextureMovement;
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
									try {
										smthWithBlockDrawingColor = texturePixels[smthWithXandYDrawAxis + smthWithZDrawAxis * 16 + blockIDToDraw * 256 * 3];
									} catch (Exception err) {
										System.out.println("Failed to load textures for block " + blockIDToDraw + ".");
										smthWithBlockDrawingColor = 0x000000;
									}
								}

								if (smthWithDistanceFade < blockReach && smthWithCameraY == events.mouseX / 2 && smthWithCameraX == events.mouseY / 2) {
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

			graphics.setColor(Color.WHITE);
			graphics.setFont(new Font("", Font.BOLD, 10));
			graphics.drawString(diamondsString, 8, 210);
			this.getGraphics().drawImage(screenImage, 0, 0, screenWidth, screenHeigth, (ImageObserver)null);

			if (events.mouseLocked != 0) {
				continue;
			}

			// Close
			if (events.worldKeys[0] == 1) { // Esc
				frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				events.worldKeys[0] = 0;
			}

			// Load world
			if (events.worldKeys[1] == 1) { // 'C'
				try {
					worldBlocks = fileLoad(worldFile, worldSize);
				} catch (Exception err) {
					err.printStackTrace();
				}
				events.worldKeys[1] = 0;
			}

			// Save world
			if (events.worldKeys[2] == 1) { // 'G'
				fileSave(worldFile, worldBlocks);
				events.worldKeys[2] = 0;
			}

			// Screenshot
			if (events.worldKeys[3] == 1) { // F2
				try {
					File screenshotFile = new File("mc4k_screenshot.png");
					ImageIO.write(screenImage, "png", screenshotFile);
					System.out.println("mc4k_scrot.png saved");
				} catch (Exception err) {
					err.printStackTrace();
				}
				events.worldKeys[3] = 0;
			}

			// Chose block to place
			if (events.arrows[0] == 1) {
				if (player.blockInHand > 1) {
					player.blockInHand--;
					if (player.blockInHand == 3 && player.diamondCounter <= 0) {
						player.blockInHand--;
					}
				} else
				{
					player.blockInHand = 15;
				}
				events.arrows[0] = 0;
			}
			if (events.arrows[1] == 1) {
				if (player.blockInHand < 15) {
					player.blockInHand++;
					if (player.blockInHand == 3 && player.diamondCounter <= 0) {
						player.blockInHand++;
					}
				} else {
					player.blockInHand = 1;
				}
				events.arrows[1] = 0;
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
					if (events.mouseX > 0) {
						moveX = (float)(events.mouseX - screenWidth / 2) / (screenWidth / 4) * 2.0F;
						moveY = (float)(events.mouseY - screenHeigth / 2) / (screenHeigth / 4) * 2.0F;
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

					moveY += (float)(events.wasd[0] - events.wasd[2]) * 0.02F;
					moveX += (float)(events.wasd[3] - events.wasd[1]) * 0.02F;

					moveVelocityX *= 0.5F;
					moveVelocityZ *= 0.99F;
					moveVelocityY *= 0.5F;
					moveVelocityX += smthWithHorisontalAxisSin * moveY + smthWithHorisontalAxisCos * moveX;
					moveVelocityY += smthWithHorisontalAxisCos * moveY - smthWithHorisontalAxisSin * moveX;
					moveVelocityZ += 0.003F;

					for (smthWithCameraX = 0; smthWithCameraX < 3; ++smthWithCameraX) {
						velocityX = player.posX + moveVelocityX * (float)((smthWithCameraX + 2) % 3 / 2);
						velocityZ = player.posZ + moveVelocityZ * (float)((smthWithCameraX + 1) % 3 / 2);
						velocityY = player.posY + moveVelocityY * (float)((smthWithCameraX + 2) % 3 / 2);

						for (int i = 0; i < 12; ++i) {
							int coordinateX = (int)(velocityX + (float)(i >> 0 & 1) * 0.6F - 0.3F) - 64;
							int coordinateZ = (int)(velocityZ + (float)((i >> 2) - 1) * 0.8F + 0.65F) - 64;
							int coordinateY = (int)(velocityY + (float)(i >> 1 & 1) * 0.6F - 0.3F) - 64;

							// World boundaries
							if (coordinateX < 0 || coordinateZ < 0 || coordinateY < 0 || coordinateX >= 64 || coordinateZ >= 64 || coordinateY >= 64 || worldBlocks[coordinateX + coordinateZ * 64 + coordinateY * 4096] > 0) {
								if (smthWithCameraX == 1) {
									// Jumping
									if (events.space > 0 && moveVelocityZ > 0.0F) {
										events.space = 0;
										moveVelocityZ = -0.1F;
									} else {
										moveVelocityZ = 0.0F;
									}
								}
								continue movement_label;
							}
						}
						player.posX = velocityX;
						player.posZ = velocityZ;
						player.posY = velocityY;
					}
				}

			// Remove block
			if (events.buttons[0] > 0 && blockThatYouLookOn > 0) {
				if (worldBlocks[blockThatYouLookOn] == 3) {
					player.diamondCounter++;
				}
				worldBlocks[blockThatYouLookOn] = 0;
				events.buttons[0] = 0;
			}

			// Place block
			if (events.buttons[1] > 0 && blockThatYouLookOn > 0) {
				worldBlocks[blockThatYouLookOn + blockNextToOneThatYouLookOn] = player.blockInHand;
				if (player.blockInHand == 3)
				{
					player.diamondCounter--;
					if (player.diamondCounter <= 0)
					{
						player.blockInHand++;
					}
				}
				events.buttons[1] = 0;
			}

			for (int i = 0; i < 12; ++i) {
				smthWithCameraY = (int)(player.posX + (float)(i >> 0 & 1) * 0.6F - 0.3F) - 64;
				smthWithCameraZ = (int)(player.posZ + (float)((i >> 2) - 1) * 0.8F + 0.65F) - 64;
				smthWithCameraX = (int)(player.posY + (float)(i >> 1 & 1) * 0.6F - 0.3F) - 64;
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

	public void playerSave(String fileName, MCPlayer playerToSave) {
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(new FileOutputStream(fileName));
			out.writeObject(playerToSave);
			out.close();
			System.out.println(fileName + " saved");
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	public MCPlayer playerLoad(String fileName) throws Exception {
		MCPlayer loadedPlayer = new MCPlayer();
		ObjectInputStream in = null;

		in = new ObjectInputStream(new FileInputStream(fileName));
		loadedPlayer = (MCPlayer)in.readObject();
		in.close();
		System.out.println(fileName + " loaded");
		return loadedPlayer;
	}

	public int[] resourceLoad(String resourceName, int arrayLength) throws Exception {
		int[] loadedFile = new int[arrayLength];
		ObjectInputStream in = null;

		in = new ObjectInputStream(Minecraft4K.class.getResourceAsStream("/res/" + resourceName));
		loadedFile = (int[])in.readObject();
		in.close();
		System.out.println(resourceName + " loaded");
		return loadedFile;
	}
}

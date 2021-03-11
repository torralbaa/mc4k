/*
 * MCPICommandHandlers.java - MCPI API command handlers.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k.api;

import java.net.*;
import java.io.*;
import java.util.HashMap;

import mc4k.Minecraft4K;
import mc4k.api.MCPICommand;

public class MCPICommandHandlers {
	public int[] mc4k2mcpi;
	public HashMap<Integer, Integer> mcpi2mc4k;

	public MCPICommandHandlers() {
		mc4k2mcpi = new int[]{0, 2, 3, 56, 1, 45, 3, 17, 18, 45, 3, 3, 3, 3, 3};
		mcpi2mc4k = new HashMap<Integer, Integer>();
		int i = 0;
		while (i < 10) {
			mcpi2mc4k.put(mc4k2mcpi[i], i);
			i++;
		}
		mcpi2mc4k.put(45, 5);
		mcpi2mc4k.put(3, 2);
	}

	/* `world` package */
	public int world_getBlock(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			float[] vec3 = command.getVec3();
			vec3[2] = 63 - vec3[2];
			if (!MCPICommand.checkVec3(vec3)) {
				return 0;
			}
			int[] intVec3 = new int[3];
			intVec3[0] = (int)vec3[0];
			intVec3[1] = (int)vec3[1];
			intVec3[2] = (int)vec3[2];
			writer.println(mc4k2mcpi[game.worldBlocks[intVec3[0] + intVec3[2] * 64 + intVec3[1] * 4096]]);
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int world_setBlock(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		if (game.worldImmutable) {
			return 0;
		}
		try {
			float[] vec3 = command.getVec3();
			if (!MCPICommand.checkVec3(vec3)) {
				return 0;
			}
			int mcpiID = command.getInt();
			int mc4kID = 2;
			int[] intVec3 = MCPICommand.mcpi2mc4kVec3(vec3);
			mc4kID = mcpi2mc4k.get(mcpiID);
			if (mc4kID <= 0 || mc4kID > 15) {
				mc4kID = 2;
			}
			game.worldBlocks[intVec3[0] + intVec3[2] * 64 + intVec3[1] * 4096] = mc4kID;
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int world_getHeight(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			int x = command.getInt();
			int y = command.getInt();
			if (x < 0 || x > 63 || y < 0 || y > 63) {
				writer.println(String.valueOf(0));
				return 0;
			}
			int z = 0;
			int block = game.worldBlocks[x + 63 * 64 + y * 4096];
			while (block != 0 && z < 64) {
				block = game.worldBlocks[x + (63 - ++z) * 64 + y * 4096];
			}
			writer.println(String.valueOf(z));
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int world_setting(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			String key = command.getString();
			int value = command.getInt();
			if (key.equals("world_immutable")) {
				game.worldImmutable = value != 0;
			}
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	/* `world.checkpoint` package */
	public int world_checkpoint_save(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			game.fileSave(game.worldFileName + ".old", game.worldBlocks);
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int world_checkpoint_restore(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		if (game.worldImmutable) {
			return 0;
		}
		try {
			game.worldBlocks = game.fileLoad(game.worldFileName + ".old", game.worldSize);
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	/* `player` package */
	public int player_getPos(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			int[] intVec3 = MCPICommand.mc4k2mcpiVec3(new float[]{game.player.posX, game.player.posY, game.player.posZ});
			writer.println(intVec3[0] + "," + intVec3[1] + "," + intVec3[2]);
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int player_getTile(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			int[] intVec3 = MCPICommand.mc4k2mcpiVec3(new float[]{game.player.posX, game.player.posY, game.player.posZ + 1});
			writer.println(intVec3[0] + "," + intVec3[1] + "," + intVec3[2]);
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int player_setPos(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			float[] vec3 = command.getVec3();
			vec3[0] += 64;
			vec3[1] += 64;
			vec3[2] += 64;

			if (MCPICommand.checkVec3(vec3)) {
				game.player.posX = (int)vec3[0];
				game.player.posY = (int)vec3[1];
				game.player.posZ = (int)vec3[2];
			}
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}

	public int player_setTile(MCPICommand command, Minecraft4K game, PrintWriter writer) {
		try {
			float[] vec3 = command.getVec3();
			vec3[0] += 64;
			vec3[1] += 64;
			vec3[2] += 64;

			if (MCPICommand.checkVec3(vec3)) {
				game.player.posX = (int)vec3[0];
				game.player.posY = (int)vec3[1];
				game.player.posZ = (int)vec3[2] + 1;
			}
		} catch (Exception err) {
			err.printStackTrace();
			return -1;
		}
		return 0;
	}
}

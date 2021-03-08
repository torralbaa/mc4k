/*
 * MCTerrainGenerator.java - Terrain generator.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * Copyright 2020 @saca44 at the MCForums
 * 
 */

package mc4k;

import java.util.Random;
import java.time.Instant;
import java.lang.Math;

public class MCTerrainGenerator {
	Random random = new Random();
	int seed = 0;
	int worldSize = 64 * 64 * 64;
	int[] worldBlocks = new int[worldSize];
	double[][] heightMap = new double[64][64];

	public MCTerrainGenerator() {
		int i = 0;
		int tbsi;
		int randomMax = (int)Math.pow(64, 4);

		float fx = 0;
		float fy = 0;

		random.setSeed(Instant.now().getEpochSecond());
		for (int y = 0; y < 64; y++) {
			fx = 0;
			for (int x = 0; x < 64; x++) {
				heightMap[x][y] = 32 + (Math.cos(fx) + Math.sin(fy)) * 2;
				fx += 0.2f;
			}
			fy += 0.2f;
		}

		for (int y = 0; y < 64; y++) {
			for (int x = 0; x < 64; x++) {
				for (int z = 0; z < 64; z++) {
					int lim = i / 64 % 64;
					double mapVal = heightMap[y][z];
					if (heightMap[y][z] > lim) {
						worldBlocks[i] = 0;
					} else if (mapVal > lim - 1) {
						worldBlocks[i] = 1;
					} else if (mapVal > lim - 4 + random.nextFloat()) {
						worldBlocks[i] = 2;
					} else {
						if (random.nextInt(randomMax) >= randomMax - randomMax / 128) {
							worldBlocks[i] = 3;
						} else {
							worldBlocks[i] = 4;
						}
					}
					i++;
				}
			}
		}

		for (int x = 0; x < 64; x++) {
			for (int z = 0; z < 64; z++) {
				tbsi = vecToInt(x, (int)(heightMap[x][z]), z);
				if (isTreeAreaBlank(x, (int)(heightMap[x][z]), z) > 0) {
					if (random.nextInt(96) == 0) {
						tree(x, (int)(heightMap[x][z]), z);
					}
				}
			}
		}
	}

	public int vecToInt(int x, int y, int z) {
		return z + y * 64 + x * 4096;
	}

	public void setBlock(int block, int x, int y, int z) {
		int coords = vecToInt(x, y, z);
		if (coords > 0 && coords < worldSize) {
			worldBlocks[coords] = block;
		}
	}

	public void fillBlocks(int block, int x, int y, int z, int w, int h, int d) {
		w += x;
		h += y;
		d += z;
		for (int xx = x; xx < w; xx++) {
			for (int yy = y; yy < h; yy++) {
				for (int zz = z; zz < d; zz++) {
					setBlock(block, xx, yy, zz);
				}
			}
		}
	}

	public int isTreeAreaBlank(int x, int y, int z) {
		int coords;

		try {
			coords = vecToInt(x, (int)(heightMap[x][z]), z);
			if (worldBlocks[coords] != 0)
			{
				return 0;
			}
			coords = vecToInt(x + 1, (int)(heightMap[x][z]), z);
			if (worldBlocks[coords] != 0)
			{
				return 0;
			}
			coords = vecToInt(x + 1, (int)(heightMap[x][z]), z + 1);
			if (worldBlocks[coords] != 0)
			{
				return 0;
			}
			coords = vecToInt(x, (int)(heightMap[x][z]), z + 1);
			if (worldBlocks[coords] != 0)
			{
				return 0;
			}
		} catch (Exception err) {
			return 0;
		}
		return 1;
	}

	public void tree(int x, int y, int z) {
		fillBlocks(8, x - 2, y - 4, z - 2, 5, 2, 5);
		fillBlocks(8, x - 1, y - 6, z - 1, 3, 2, 3);
		for (int i = 0; i < 5; i++) {
			setBlock(7, x, y - i, z);
		}
	}

	public int[] getBlocks() {
		return worldBlocks;
	}
}

/*
 * MCPlayer.java - Player inventory and data.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k;

import java.util.Random;
import java.time.Instant;
import java.lang.Math;
import java.io.Serializable;

public class MCPlayer implements Serializable {
	private static final long serialVersionUID = 1L;

	public int blockInHand = 1;
	public int diamondCounter = 0;
	public float posX = 96.5F;
	public float posY = 95.0F;
	public float posZ = 96.5F;
}

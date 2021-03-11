/*
 * MCPICommand.java - MCPI API packet parser and type conversion helper.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k.api;

import java.net.*;
import java.io.*;

class Vec3 {
	private int intX = 0;
	private int intY = 0;
	private int intZ = 0;

	public float floatX = 0.0F;
	public float floatY = 0.0F;
	public float floatZ = 0.0F;

	public int getIntX() {
		return (int)floatX;
	}

	public int getIntY() {
		return (int)floatY;
	}

	public int getIntZ() {
		return (int)floatZ;
	}

	@Override
	public String toString() {
		return String.valueOf((int)floatX) + "," + String.valueOf((int)floatY) + "," + String.valueOf((int)floatZ);
	}
}

public class MCPICommand {
	public String pack;
	public String name;
	public String[] args;
	public int argc = 0;
	public int argi = 0;

	public MCPICommand(String packet) {
		String[] tmp = packet.split("\\.", 2);
		pack = tmp[0];
		tmp = tmp[1].split("\\(", 2);
		name = tmp[0];
		args = tmp[1].split("\\)", 2)[0].split(",", 0);
		argc = args.length;
	}

	public float[] getVec3() throws IndexOutOfBoundsException {
		float[] vec3 = new float[3];
		if (argc - argi < 3) {
			throw new IndexOutOfBoundsException("Trying to access unexisting argument number " + argi);
		}
		vec3[0] = Float.parseFloat(args[argi++]);
		vec3[1] = Float.parseFloat(args[argi++]);
		vec3[2] = Float.parseFloat(args[argi++]);
		return vec3;
	}

	public int getInt() throws IndexOutOfBoundsException {
		if (argc - argi < 1) {
			throw new IndexOutOfBoundsException("Trying to access unexisting argument number " + argi);
		}
		return (int)Float.parseFloat(args[argi++]);
	}

	public String getString() throws IndexOutOfBoundsException {
		if (argc - argi < 1) {
			throw new IndexOutOfBoundsException("Trying to access unexisting argument number " + argi);
		}
		return args[argi++];
	}

	public static int[] mcpi2mc4kVec3(float[] vec3) {
		int[] intVec3 = new int[3];
		intVec3[0] = (int)vec3[0];
		intVec3[1] = (int)vec3[1];
		intVec3[2] = (int)(63 - vec3[2]);
		return intVec3;
	}

	public static int[] mc4k2mcpiVec3(float[] vec3) {
		int[] intVec3 = new int[3];
		intVec3[0] = (int)(vec3[0] - 64);
		intVec3[1] = (int)(vec3[1] - 64);
		intVec3[2] = (int)(63 - (vec3[2] - 64));
		return intVec3;
	}

	public static boolean checkVec3(float[] vec3) {
		if (vec3[0] < 0 || vec3[0] >= 63) {
			return false;
		}
		if (vec3[1] < 0 || vec3[1] >= 63) {
			return false;
		}
		if (vec3[2] < 0 || vec3[2] > 63) {
			return false;
		}
		return true;
	}
}

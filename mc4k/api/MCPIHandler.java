/*
 * MCPIHandler.java - MCPI API client handling.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k.api;

import java.net.*;
import java.io.*;
import java.lang.reflect.Method;

import mc4k.Minecraft4K;
import mc4k.api.MCPICommand;
import mc4k.api.MCPICommandHandlers;

public class MCPIHandler implements Runnable {
	public Socket socket;
	Minecraft4K game;
	MCPICommandHandlers handlers;

	public MCPIHandler(Socket tmpSocket, Minecraft4K tmpGame) {
		socket = tmpSocket;
		game = tmpGame;
		handlers = new MCPICommandHandlers();
		(new Thread(this)).start();
	}

	public void run() {
		try {
			String packet;
			Class[] argTypes = new Class[3];
			PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			argTypes[0] = MCPICommand.class;
			argTypes[1] = Minecraft4K.class;
			argTypes[2] = PrintWriter.class;

			packet = reader.readLine();
			while (packet != null) {
				MCPICommand command = new MCPICommand(packet);
				try {
					Method method = MCPICommandHandlers.class.getMethod(command.pack + "_" + command.name.replace('.', '_'), argTypes);
					int rt = (int)method.invoke(handlers, command, game, writer);
					if (rt != 0) {
						writer.println("Fail");
					}
				} catch (Exception err) {
					writer.println("Fail");
				}
				packet = reader.readLine();
			}
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}

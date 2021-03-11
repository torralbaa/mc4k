/*
 * MCPIServer.java - MCPI API implementation.
 * 
 * Copyright 2021 Alvarito050506 <donfrutosgomez@gmail.com>
 * 
 */

package mc4k.api;

import java.net.*;
import java.io.*;

import mc4k.Minecraft4K;
import mc4k.api.MCPIHandler;

public class MCPIServer implements Runnable {
	public ServerSocket socket;
	public SocketAddress addr;
	public boolean runAPI = true;

	Minecraft4K game;

	public MCPIServer(String host, int port, Minecraft4K tmpGame) throws Exception {
		game = tmpGame;
		addr = new InetSocketAddress(InetAddress.getByName(host), port);
		socket = new ServerSocket();
		socket.setReuseAddress(true);
		socket.bind(addr);
		(new Thread(this)).start();
	}

	public void run() {
		while (runAPI) {
			try {
				Socket connection = socket.accept();
				new MCPIHandler(connection, game);
			} catch (Exception err) {
				err.printStackTrace();
			}
		}
		try {
			socket.close();
		} catch (Exception err) {
			err.printStackTrace();
		}
	}
}

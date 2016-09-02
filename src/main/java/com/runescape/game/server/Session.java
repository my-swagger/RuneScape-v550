package com.runescape.game.server;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class Session {
	private NetSocket socket;
	
	public Session(NetSocket socket) {
		this.socket = socket;
	}
	
	public void write(Buffer buffer) {
		socket.write(buffer);
	}
	
	public void disconnect() {
		socket.end();
		socket.close();
	}
	
	public String getRemoteAddress() {
		return socket.remoteAddress().host();
	}
	
	public int getRemotePort() {
		return socket.remoteAddress().port();
	}
}

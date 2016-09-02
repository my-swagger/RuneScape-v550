package com.runescape.game.server.net;

import com.runescape.game.server.Session;

import io.vertx.core.buffer.Buffer;

public interface Request {
	public void handle(Session session, Buffer buffer);
}

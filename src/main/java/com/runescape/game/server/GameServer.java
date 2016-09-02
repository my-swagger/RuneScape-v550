package com.runescape.game.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runescape.game.server.net.GameRequest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

public class GameServer extends AbstractVerticle {
	private Logger logger = LoggerFactory.getLogger(GameServer.class);
	public static final int VERSION = Integer.parseInt(System.getProperty("version", "550"));;
	private static final int GAME_PORT = Integer.parseInt(System.getProperty("game-port", "43594"));
	private NetServer server;

	@Override
	public void start() {
		NetServerOptions options = new NetServerOptions()
				.setPort(GAME_PORT)
				.setIdleTimeout(10);
		server = vertx.createNetServer(options);
		server.connectHandler(socket -> {
			socket.handler(new GameRequest(new Session(socket)));
			logger.info("[GameServer] Connection from:" + socket.remoteAddress());
			socket.closeHandler(s -> {
			//	session.disconnect();
				logger.info("[GameServer][" + socket.remoteAddress() + " disconnected]");
			});
		});
		server.listen();
	}

	@Override
	public String toString() {
		return "[GameServer][version=" + VERSION + "][port=" + GAME_PORT + "]";
	}
}

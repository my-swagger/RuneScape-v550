package com.runescape.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runescape.game.http.HTTPServer;
import com.runescape.game.server.GameServer;

import io.vertx.core.Vertx;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		HTTPServer http = new HTTPServer();
		GameServer game = new GameServer();
		vertx.deployVerticle(http, res -> {
			if (res.succeeded()) {
				logger.info("" + http);
			}
		});
		vertx.deployVerticle(game, res -> {
			if (res.succeeded()) {
				logger.info("" + game);
			}
		});
	}
}
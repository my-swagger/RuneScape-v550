package com.runescape.game.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;

public class HTTPServer extends AbstractVerticle {
	public static final String HOST = System.getProperty("game-host", "localhost");
	public static final int HTTP_PORT = Integer.parseInt(System.getProperty("http-port", "80"));
	private HttpServer server;
	
	@Override
	public void start() {
		server = vertx.createHttpServer();
		server.requestHandler(new HTTPRequestHandler());
		server.listen(HTTP_PORT, HOST);
	}
	
	@Override
	public String toString() {
		return "[HTTP server][port=" + HTTP_PORT + "]";
	}
}
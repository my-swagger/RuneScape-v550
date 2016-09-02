package com.runescape.game.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

public class HTTPRequestHandler implements Handler<HttpServerRequest> {
	private static final Logger logger = LoggerFactory.getLogger(HTTPRequestHandler.class);
	private static final String WWW_ROOT = "www/";

	@Override
	public void handle(HttpServerRequest request) {
		String path = request.path();
		if (path.contains("runescape.jar")) {
			request.response().sendFile(WWW_ROOT + "runescape.jar");
		} else {
			request.response().sendFile(WWW_ROOT + "index.html");
		}
		logger.info("[http request=" + path + "]");
	}
}
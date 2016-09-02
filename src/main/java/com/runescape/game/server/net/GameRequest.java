package com.runescape.game.server.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.runescape.game.server.Session;
import com.runescape.game.server.net.login.LoginRequest;
import com.runescape.game.server.net.update.UpdateRequest;
import com.runescape.game.server.net.world.WorldListRequest;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class GameRequest implements Handler<Buffer> {
	private static final Logger logger = LoggerFactory.getLogger(GameRequest.class);
	private Session session;
	
	public GameRequest(Session session) {
		this.session = session;
	}
	
	@Override
	public void handle(Buffer buffer) {
		Request request = null;
		ByteBuf buf = buffer.getByteBuf();
		int service = buf.readUnsignedByte();
		switch (service) {
		case RequestType.UPDATE_CONNECTION_INITIALIZE:
		case RequestType.LOGGED_OUT:
			break;
		case RequestType.UPDATE_REQUEST:
		case RequestType.UPDATE_FILE_REQUEST:
		case RequestType.UPDATE_FILE_REQUEST_PRIORITY:
			request = new UpdateRequest();
			break;
		case RequestType.LOGIN__REQUEST:
			request = new LoginRequest();
			break;
		case RequestType.WORLD_LIST:
			request = new WorldListRequest();
			break;
			default:
				logger.warn("Unhandled service:" + service);
				break;
		}
		if (request != null) {
			request.handle(session, buffer);
		}
	}
	
}
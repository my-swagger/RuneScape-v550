package com.runescape.game.server.net.login;

import com.runescape.game.server.Session;
import com.runescape.game.server.net.Request;
import com.runescape.game.server.net.RequestType;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public class LoginRequest implements Request {

	@Override
	public void handle(Session session, Buffer buffer) {
		ByteBuf buf = buffer.getByteBuf();
		int request = buf.readUnsignedByte();
		if (request == RequestType.LOGIN__REQUEST) {
			LoginDecoder decoder = new LoginDecoder(session);
			decoder.decode(buffer);
		} else if (request == 16) {
			/* TODO: I don't know what service 16 is, my original client did not send this however Cedia's 550 hd did? */
		}
	}
}

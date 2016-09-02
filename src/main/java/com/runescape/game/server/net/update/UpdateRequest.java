package com.runescape.game.server.net.update;

import com.runescape.game.server.Session;
import com.runescape.game.server.net.Request;
import com.runescape.game.server.net.RequestType;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public class UpdateRequest implements Request {
	
	@Override
	public void handle(Session session, Buffer buffer) {
		ByteBuf buf = buffer.getByteBuf();
		int opcode = buf.readUnsignedByte();
		if (opcode == RequestType.UPDATE_REQUEST) {
			Handshake handshake = new Handshake(session);
			handshake.decode(buffer);
		}
		if (opcode == RequestType.UPDATE_FILE_REQUEST || opcode == RequestType.UPDATE_FILE_REQUEST_PRIORITY) {
			boolean priority = opcode == 1;
			int type = buf.readUnsignedByte();
			int file = buf.readUnsignedShort();
			FileRequest request = new FileRequest(priority, type, file);
			FileDecoder decoder = new FileDecoder();
			FileEncoder encoder = new FileEncoder();
			Buffer decoded = decoder.decode(request);
			Buffer encoded = encoder.encode(request, decoded);
			session.write(encoded);
		}
	}
}
package com.runescape.game.server.net.update;

import com.runescape.game.server.GameServer;
import com.runescape.game.server.Session;
import com.runescape.game.server.net.RequestType;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public class Handshake {
	private static final int VERSION_OK = 0;
	private static final int VERSION_OUT_OF_DATE = 6;
	private Session session;
	
	public Handshake(Session session) {
		this.session = session;
	}
	
	public void decode(Buffer buffer) {
		ByteBuf buf = buffer.getByteBuf();
		int request = buf.readUnsignedByte();
		if (request == RequestType.UPDATE_REQUEST) {
			int version = buf.readInt();
			if (version == GameServer.VERSION) {
				session.write(Buffer.buffer().appendByte((byte) VERSION_OK));
			} else {
				session.write(Buffer.buffer().appendByte((byte) VERSION_OUT_OF_DATE));

			}
		}
	}
}
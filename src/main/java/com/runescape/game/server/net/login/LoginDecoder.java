package com.runescape.game.server.net.login;

import java.security.SecureRandom;

import com.runescape.game.server.Session;
import com.runescape.game.server.net.RequestType;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public class LoginDecoder {
	private Session session;
	
	public LoginDecoder(Session session) {
		this.session = session;
	}
	
	public void decode(Buffer buffer) {
		ByteBuf buf = buffer.getByteBuf();
		int request = buf.readUnsignedByte();
		if (request == RequestType.LOGIN__REQUEST) {
			session.write(Buffer.buffer().appendByte((byte) 0).appendLong(new SecureRandom().nextLong()));
			return;
		}
		
		if (buf.readableBytes() < 4) {
			return;
		}
		
		int hash = buf.readUnsignedByte();
		int type = buf.readUnsignedByte();
		int size = buf.readUnsignedShort();
		
		if (type != 16 && type != 18) {
			System.err.print("Invalid login type: " + type);
			return;
		}
		
		System.out.println("[hash=" + hash + "][type=" + type + "][size=" + size + "]");
		
		if (buf.readableBytes() < size) {
			return;
		}
		
		int version = buf.readInt();
		System.out.println("[login][version=" + version + "]");
		
		buf.readUnsignedByte();
		buf.readUnsignedByte();
		buf.readUnsignedByte();
		
		int displayMode = buf.readUnsignedByte();
		System.out.println("[login][display-mode=" + displayMode + "]");
		
		int width = buf.readUnsignedShort();
		int height = buf.readUnsignedShort();
		System.out.println("[login][width=" + width + "][height=" + height + "]");
		
		buf.readUnsignedByte();
	}
}

package com.runescape.game.server.net.update;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public class FileEncoder {
	private static final int FIRST_CHUNK_LENGTH = 508;
	private static final int MAX_CHUNK_LENGTH = 511;
	
	public Buffer encode(FileRequest request, Buffer buffer) {
		Buffer buf = Buffer.buffer();
		ByteBuf cache = buffer.getByteBuf();
		
		boolean priority = request.isPriority();
		int type = request.getType();
		int file = request.getFile();
		
		buf.appendByte((byte) type);
		buf.appendShort((short) file);

		int compression = cache.readUnsignedByte();
		if (!priority) {
			compression |= 0x80;
		}
		
		buf.appendByte((byte) compression);
		
		int bytes = cache.readableBytes(); 
		
		if (bytes > FIRST_CHUNK_LENGTH) {
			bytes = FIRST_CHUNK_LENGTH;
		}
		
		buf.appendBuffer(Buffer.buffer(cache.readBytes(bytes)));
		
		while(bytes != 0) {
			bytes = cache.readableBytes();
			if (bytes != 0) {
				if (bytes > MAX_CHUNK_LENGTH) {
					bytes = MAX_CHUNK_LENGTH;
				}
				buf.appendByte((byte) 255);
				buf.appendBuffer(Buffer.buffer(cache.readBytes(bytes)));
			}
		}
		
		return buf;
	}
}

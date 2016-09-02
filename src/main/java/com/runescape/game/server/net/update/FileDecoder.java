package com.runescape.game.server.net.update;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.vertx.core.buffer.Buffer;
import net.scapeemulator.cache.Cache;
import net.scapeemulator.cache.ChecksumTable;
import net.scapeemulator.cache.Container;
import net.scapeemulator.cache.FileStore;

public class FileDecoder {
	private static final Logger logger = LoggerFactory.getLogger(FileDecoder.class);
	private Cache cache;
	private ChecksumTable checksumTable;
	
	public FileDecoder() {
		try {
			cache = new Cache(FileStore.open("data/cache"));
			checksumTable = cache.createChecksumTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Buffer decode(FileRequest request) {
		ByteBuf buf = null;
		int type = request.getType();
		int file = request.getFile();
		boolean crcTableRequest = (type == 255) && (file == 255);
		try {
			if (crcTableRequest) {
				Container container = new Container(Container.COMPRESSION_NONE, checksumTable.encode());
				buf = Unpooled.wrappedBuffer(container.encode());
			} else {
				buf = Unpooled.wrappedBuffer(cache.getStore().read(type, file));
				if (type != 255) {
					buf = buf.slice(0, buf.readableBytes() - 2);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return Buffer.buffer(buf);
	}
}

package com.runescape.game.server.net.world;

import java.nio.charset.StandardCharsets;

import com.runescape.game.server.Session;
import com.runescape.game.server.net.Request;
import com.runescape.game.server.net.RequestType;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

/** TODO: Fix, this is not working */
public class WorldListRequest implements Request {

	private static final Country[] COUNTRIES = {
		new Country(Country.FLAG_UK, "UK")
	};
	private static final World[] WORLDS = { 
		new World(1, World.FLAG_MEMBERS | World.FLAG_HIGHLIGHT, 0, "KissMy550", "127.0.0.1"), 
	};
	private int[] players = { 
		1000
	};
	
	@Override
	public void handle(Session session, Buffer buffer) {
		ByteBuf buf = buffer.getByteBuf();
		int request = buf.readUnsignedByte();
		if (request == RequestType.WORLD_LIST) {
			int id = buf.readInt();

			Buffer out = Buffer.buffer();
			WorldListMessage message = new WorldListMessage(0xDEADBEEF, COUNTRIES, WORLDS, players);
			
			out.appendByte((byte) 1).appendByte((byte) 1);
			writeSmart(out, COUNTRIES.length);
			
			for (Country country : COUNTRIES) {
				writeSmart(out, country.getFlag());
				writeWorldListString(out, country.getName());
			}
			
			int minId = WORLDS[0].getId();
			int maxId = WORLDS[0].getId();
			
			for (int i = 1; i < WORLDS.length; i++) {
				World world = WORLDS[i];
				int worldId = world.getId();

				if (worldId > maxId)
					maxId = worldId;
				if (worldId < minId)
					minId = worldId;
			}
			
			writeSmart(out, minId);
			writeSmart(out, maxId);
			writeSmart(out, WORLDS.length);
			
			for (World world : WORLDS) {
				writeSmart(out, (world.getId() - minId));
				out.appendByte((byte) world.getCountry());
				out.appendInt(world.getFlags());
				writeWorldListString(out, world.getActivity());
				writeWorldListString(out, world.getIp());
			}
			
			out.appendInt(message.getSessionId());
			
			int[] players = message.getPlayers();
			for (int i = 0; i < WORLDS.length; i++) {
				World world = WORLDS[i];
				writeSmart(out, (world.getId() - minId));
				out.appendShort((short) players[i]);
			}
			
			out.appendByte((byte) 0).appendShort((short) out.length());
			session.write(out);
		}
	}
	
	public static void writeWorldListString(Buffer buffer, String str) {
		buffer.appendByte((byte) 0);
		buffer.appendBytes(str.getBytes(StandardCharsets.ISO_8859_1));
		buffer.appendByte((byte) 0);
	}
	
	public static void writeSmart(Buffer buffer, int value) {
		if (value < 128)
			buffer.appendByte((byte) value);
		else
			buffer.appendShort((short) (32768 + value));
	}
}

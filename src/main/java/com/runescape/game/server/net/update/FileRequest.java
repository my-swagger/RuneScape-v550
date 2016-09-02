package com.runescape.game.server.net.update;

public class FileRequest {
	private boolean priority;
	private int type, file;
	
	public FileRequest(boolean priority, int type, int file) {
		this.priority = priority;
		this.type = type;
		this.file = file;
	}
	
	public boolean isPriority() {
		return priority;
	}

	public int getType() {
		return type;
	}

	public int getFile() {
		return file;
	}
}

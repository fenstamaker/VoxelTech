package org.voxeltech.game;

import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler implements Runnable {

	private Thread thread;
	private ArrayList<WorldChunk> chunks;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<WorldChunk>();
		thread = new Thread(this);
		thread.start();
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		WorldChunk c = new WorldChunk(x, y, z);
		chunks.add(c);
		return c;
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		WorldChunk c = new WorldChunk(x, y, z);
		chunks.add(c);
		return c;
	}
	
	public WorldChunk loadChunk(int x, int y, int z) {
		for(WorldChunk c : chunks) {
			if(c.coordinates[0] == x && c.coordinates[1] == y && c.coordinates[2] == z)
				return c;
		}
		return generateChunk(x, y, z);
	}

	@Override
	public void run() {
		
	}
	
	
}

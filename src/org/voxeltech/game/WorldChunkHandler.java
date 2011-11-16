package org.voxeltech.game;

import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<WorldChunk> chunks;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<WorldChunk>();
		
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
	
	
	public ArrayList<WorldChunk> loadChunksAroundChunk(int x, int y, int z) {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 5; j++) {
				for(int k = 0; k < 10; k++) {
					chunkHolder.add(loadChunk(x+i, y+j, z+k));
					chunkHolder.add(loadChunk(x-i, y-j, z-k));
				}
			}
		}
		
		/*
		for(int i = 0; i < chunks.size(); i++) {
			if(!chunkHolder.contains(chunks.get(i))) {
				chunks.remove(i);
				i--;
			}
		}*/
		
		return chunkHolder;

	}
	
}

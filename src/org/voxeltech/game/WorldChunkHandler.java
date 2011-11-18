package org.voxeltech.game;

import java.io.*;
import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<WorldChunk> chunks;
	private FileInputStream fileIn;
	private FileOutputStream fileOut;
	private File file;	
	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<WorldChunk>();
		
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk loadChunk(int x, int y, int z) {
		String filename = System.getProperty("user.dir") + "VT_CHUNK_" + x + "_" + y + "_" + z;
		file = new File(filename);
		
		WorldChunk chunk = null;
		
		if(!file.exists()) {
			
			try {
				file.createNewFile();
				fileOut = new FileOutputStream(file);
				objectOut = new ObjectOutputStream(fileOut);

				chunk = generateChunk(x, y, z);
				
				objectOut.writeObject(chunk);
				objectOut.close();
				fileOut.close();
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		} else {
			
			try {
				fileIn = new FileInputStream(file);
				objectIn = new ObjectInputStream(fileIn);
				
				chunk = (WorldChunk)objectIn.readObject();
				
				objectIn.close();
				fileIn.close();
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		}
		
		return chunk;
		/*
		for(WorldChunk c : chunks) {
			if(c.coordinates[0] == x && c.coordinates[1] == y && c.coordinates[2] == z)
				return c;
		}
		return generateChunk(x, y, z);
		*/
	}
	
	
	public ArrayList<WorldChunk> loadChunksAroundChunk(int x, int y, int z) {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 2; k++) {
					chunkHolder.add(loadChunk(x+i, y+j, z+k));
					chunkHolder.add(loadChunk(x+i, y+j, z-k));
					chunkHolder.add(loadChunk(x+i, y-j, z+k));
					chunkHolder.add(loadChunk(x+i, y-j, z-k));
					chunkHolder.add(loadChunk(x-i, y+j, z+k));
					chunkHolder.add(loadChunk(x-i, y+j, z-k));
					chunkHolder.add(loadChunk(x-i, y-j, z+k));
					chunkHolder.add(loadChunk(x-i, y-j, z-k));
				}
			}
		}
		
		return chunkHolder;

	}
	
}

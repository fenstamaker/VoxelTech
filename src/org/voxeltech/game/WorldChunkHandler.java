package org.voxeltech.game;

import java.io.*;
import java.sql.*;
import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<Integer[]> chunks;
	private Connection connection;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private ByteArrayOutputStream bufferOut;
	private ByteArrayInputStream bufferIn;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<Integer[]>();
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		return new WorldChunk(x, y, z);
	}
	
	public ArrayList<WorldChunk> loadChunk() {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		ArrayList<Integer> foundChunks = new ArrayList<Integer>();
		
		
		for(Integer[] i : chunks) {
			chunkHolder.add(generateChunk(i[0], i[1], i[2]));
		}
		
		return chunkHolder;
	}
	
	
	public ArrayList<WorldChunk> loadChunksAroundChunk(int x, int y, int z) {
		chunks.clear();
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 5; k++) {
					chunks.add( new Integer[] { (x+i), (y+j), (z+k) } );
					chunks.add( new Integer[] { (x+i), (y+j), (z-k) } );
					chunks.add( new Integer[] { (x+i), (y-j), (z+k) } );
					chunks.add( new Integer[] { (x+i), (y-j), (z-k) } );
					chunks.add( new Integer[] { (x-i), (y+j), (z+k) } );
					chunks.add( new Integer[] { (x-i), (y+j), (z-k) } );
					chunks.add( new Integer[] { (x-i), (y-j), (z+k) } );
					chunks.add( new Integer[] { (x-i), (y-j), (z-k) } );
				}
			}
		}
		
		return loadChunk();

	}
	
}

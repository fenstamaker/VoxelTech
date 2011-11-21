package org.voxeltech.game;

import java.io.*;
import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<Integer[]> chunks;
	
	private FileInputStream fileIn;
	private FileOutputStream fileOut;
	private File file;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private BufferedOutputStream bufferOut;
	private BufferedInputStream bufferIn;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<Integer[]>();		
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		return new WorldChunk(x, y, z);
	}
	
	public ArrayList<WorldChunk> loadChunks() {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		
		for(Integer[] i : chunks) {
			
			int x = i[0];
			int y = i[1];
			int z = i[2];
			
			String filename = System.getProperty("user.dir") + "VT_CHUNK_" + x + "_" + y + "_" + z;
			file = new File(filename);

			WorldChunk chunk = null;

			if(!file.exists()) {

				try {
					file.createNewFile();
					fileOut = new FileOutputStream(file);
					bufferOut = new BufferedOutputStream(fileOut);
					objectOut = new ObjectOutputStream(bufferOut);

					chunk = generateChunk(x, y, z);

					objectOut.writeObject(chunk);
					objectOut.close();
					bufferOut.close();
					fileOut.close();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}

			} else {

				try {
					fileIn = new FileInputStream(file);
					bufferIn = new BufferedInputStream(fileIn);
					objectIn = new ObjectInputStream(bufferIn);

					chunk = (WorldChunk)objectIn.readObject();

					objectIn.close();
					bufferIn.close();
					fileIn.close();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}

			}
			
			chunkHolder.add(chunk);
			
		}
		
		/*
		for(Integer[] i : chunks) {
			chunkHolder.add(generateChunk(i[0], i[1], i[2]));
		}
		*/
		
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
		
		return loadChunks();

	}
	
}

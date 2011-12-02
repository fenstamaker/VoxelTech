package org.voxeltech.game;

import java.io.*;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Arrays;

public class WorldChunkHandler implements Runnable{

	private final static int turn = 0;
	private final static int opTurn = 1;

	public volatile boolean keepRunning = true;
	
	private ArrayList<Integer[]> chunks;
	private int[] previousChunk;
	
	private Thread thread;
	
	private FileInputStream fileIn;
	private FileOutputStream fileOut;
	private File file;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private BufferedOutputStream bufferOut;
	private BufferedInputStream bufferIn;
	
	public WorldChunkHandler() {
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		return new WorldChunk(x, y, z);
	}
	
	public ArrayList<WorldChunk> loadChunks() {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		ArrayList<Integer[]> regions = new ArrayList<Integer[]>();
		ArrayList<Integer[]> foundChunks = new ArrayList<Integer[]>();
		chunkHolder.clear();
		
		for(Integer[] i : chunks) {
			
			int x = i[0];
			int y = i[1];
			int z = i[2];
			
			int regionX = x/2;
			int regionY = y/2;
			int regionZ = z/2;
			
			regions.add(new Integer[] { regionX, regionY, regionZ } );
			
		}
		
		for(Integer[] region : regions) {
			
			String filename = System.getProperty("user.dir") + "/../world/VT_REGION_" + region[0] + "_" + region[1] + "_" + region[2];
			file = new File(filename);
			
			if( file.exists() ) {

				try {
					fileIn = new FileInputStream(file);
					bufferIn = new BufferedInputStream(fileIn);
					
					WorldChunk chunk;
					while(true) {
						try {
							objectIn = new ObjectInputStream(bufferIn);
							
							chunk = (WorldChunk)objectIn.readObject();
							if( chunks.contains(chunk) ) {
								chunkHolder.add( chunk );
								foundChunks.add( new Integer[] { chunk.coordinates[0], chunk.coordinates[1], chunk.coordinates[2] } );
							}
							
							objectIn.close();
						} catch(Exception e) {
							break;
						}
					}
					
					bufferIn.close();
					fileIn.close();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}				
				
			} else {
				try {
					file.createNewFile();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}

		for(Integer[] i : chunks) {
			
			if( !foundChunks.contains(i) ) {
				int x = i[0];
				int y = i[1];
				int z = i[2];
				
				int regionX = x/2;
				int regionY = y/2;
				int regionZ = z/2;
				
				String filename = System.getProperty("user.dir") + "/../world/VT_REGION_" + regionX + "_" + regionY + "_" + regionZ;
				file = new File(filename);
				
				try {	
					fileOut = new FileOutputStream(file);
					bufferOut = new BufferedOutputStream(fileOut);
					objectOut = new ObjectOutputStream(bufferOut);
	
					WorldChunk chunk = new WorldChunk(x, y, z);
					objectOut.writeObject(chunk);
					chunkHolder.add(chunk);
					
					objectOut.close();
					bufferOut.close();
					fileOut.close();
				} catch(Exception e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			
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
		
		return loadChunks();

	}
	
	public void run() {
		chunks = new ArrayList<Integer[]>();
		previousChunk = ThreadHandler.getPosition();
		
		ThreadHandler.setFlag(turn, true);
		ThreadHandler.setTurn(opTurn);
		
		while( ThreadHandler.flag[opTurn] && ThreadHandler.turn == opTurn ) {
			System.out.println("WorldChunkHandler: HALT");
		}
		System.out.println("WorldChunkHandler: PROCEED");
		previousChunk = ThreadHandler.getPosition();
		ThreadHandler.setChunks( loadChunksAroundChunk(previousChunk[0], previousChunk[1], previousChunk[2]) );
		ThreadHandler.setUpdate(true);
		
		ThreadHandler.setFlag(turn, false);
		
		
		while(keepRunning) {
			int[] tempPos = ThreadHandler.getPosition();
			if( !Arrays.equals(previousChunk, tempPos) ) {
				ThreadHandler.setFlag(turn, true);
				ThreadHandler.setTurn(opTurn);
				
				if( !ThreadHandler.flag[opTurn] || ThreadHandler.turn != opTurn ) {
					
					System.out.println("WorldChunkHandler: PROCEED");
					
					previousChunk = ThreadHandler.getPosition();
					ThreadHandler.setChunks( loadChunksAroundChunk(previousChunk[0], previousChunk[1], previousChunk[2]) );
					ThreadHandler.setUpdate(true);
					
					ThreadHandler.setFlag(turn, false);
					
				}
			}
		}
		
		System.out.println("Stopping WorldChunkHandler...");
	}
	
}
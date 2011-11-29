package org.voxeltech.game;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import org.voxeltech.graphics.*;
import org.voxeltech.noise.*;
import org.voxeltech.utils.*;

public class World {

	private final static int turn = 1;
	private final static int opTurn = 0;
	
	public final static int h_NUMBER_LOAD_CHUNKS = 3;
	public final static int h_NUMBER_RENDER_CHUNKS = 2;
	public final static int v_NUMBER_LOAD_CHUNKS = 3;
	public final static int v_NUMBER_RENDER_CHUNKS = 1;
	public final static int[] h_CHUNKS_TO_LOAD = {};
	public final static int[] v_CHUNKS_TO_LOAD = {};
	
	// x, y, z modifiers
	public final static int[][] CHUNKS_TO_RENDER = { { 0,  0,  0}, 

													 { 0, -1,  0},
		
													 { 1,  0,  0},
													 {-1,  0,  0}, 
													 
													 { 1, -1,  0},
													 
													 {-1, -1,  0},
													 
													 { 0,  0,  1}, 
													 { 0,  0, -1},
													  
													 { 0, -1,  1}, 
													  
													 { 0, -1, -1},
													 
													 { 0, -2,  0},
		
													 { 2,  0,  0},
													 {-2,  0,  0}, 
													 
													 { 2, -2,  0},
													 
													 {-2, -2,  0},
													 
													 { 0,  0,  2}, 
													 { 0,  0, -2},
													 
													 { 0, -2,  2}, 
													 
													 { 0, -2, -2}    };
	
	
	public final static int VERTICAL_LIMIT = 2;

	private ProgramClock clock = ProgramClock.getInstance();
	private Renderer renderer = Renderer.getInstance();
	private ArrayList<WorldChunk> renderedChunks;
	private ArrayList<WorldChunk> loadedChunks;
	private Vector3f playerLocation;
	private int[] chunkPlayerIsIn;
	private WorldChunkHandler chunkHandler;
	private ThreadHandler threadHandler;

	public World() {
		threadHandler = ThreadHandler.INSTANCE;
		renderedChunks = new ArrayList<WorldChunk>();
		loadedChunks = new ArrayList<WorldChunk>();
		chunkHandler = new WorldChunkHandler();

		setPlayerLocation(0, 0, 0);
		
	}
	
	public void setPlayerLocation(float x, float y, float z) {
		playerLocation = new Vector3f(x, y, z);
		int[] newChunkLocation = new int[] { ((int)(x / (WorldChunk.SIZE*Voxel.SIZE))), 
									   ((int)(y / (WorldChunk.SIZE*Voxel.SIZE))), 
									   ((int)(z / (WorldChunk.SIZE*Voxel.SIZE))) };
		
		if(!Arrays.equals(chunkPlayerIsIn, newChunkLocation)) {
			chunkPlayerIsIn = newChunkLocation;
			threadHandler.setPosition(chunkPlayerIsIn);
		}
		
	}
	
	public void loadChunksAroundPlayer() {
		
		if( threadHandler.shouldUpdate  ) {
			System.out.println("World: Chunk Update Available");
			
			threadHandler.setFlag(turn, true);
			threadHandler.setTurn(opTurn);
			
			if( !threadHandler.flag[opTurn] && threadHandler.turn != opTurn ) {
				System.out.println("World: PROCEED");
				renderedChunks.clear();
				renderer.clearChunks(); 
				System.out.println("World.java");
				
				renderedChunks.addAll( threadHandler.getChunks() );
				
				renderer.addChunks(renderedChunks);
				
				threadHandler.releaseLock();
				threadHandler.setUpdate(false);

				System.out.println("World: FINISHED");
			} else {
				System.out.println(Boolean.toString(threadHandler.getFlag(opTurn)) + threadHandler.getTurn());
			}
			
		}
		
	}
	
	public void addChunksToRenderer() {
		renderer.addChunks(renderedChunks);
	}

	public void destroy() {
		chunkHandler.stop();
	}
	
}
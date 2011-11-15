package org.voxeltech.game;

import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import org.voxeltech.graphics.*;
import org.voxeltech.noise.*;
import org.voxeltech.utils.*;

public class World {
	
	public final static int h_NUMBER_LOAD_CHUNKS = 3;
	public final static int h_NUMBER_RENDER_CHUNKS = 2;
	public final static int v_NUMBER_LOAD_CHUNKS = 3;
	public final static int v_NUMBER_RENDER_CHUNKS = 1;
	public final static int[] h_CHUNKS_TO_LOAD = {};
	public final static int[] v_CHUNKS_TO_LOAD = {};
	
	// x, y, z modifiers
	public final static int[][] CHUNKS_TO_RENDER = { { 0,  0,  0}, 

													 { 0,  1,  0},
													 { 0, -1,  0},
		
													 { 1,  0,  0},
													 {-1,  0,  0}, 
													 
													 { 1,  1,  0},
													 { 1, -1,  0},
													 
													 {-1,  1,  0},
													 {-1, -1,  0},
													 
													 { 0,  0,  1}, 
													 { 0,  0, -1},
													 
													 { 0,  1,  1}, 
													 { 0, -1,  1}, 
													 
													 { 0,  1, -1}, 
													 { 0, -1, -1}  };
	
	
	public final static int VERTICAL_LIMIT = 2;

	private ProgramClock clock = ProgramClock.getInstance();
	private Renderer renderer = Renderer.getInstance();
	private ArrayList<WorldChunk> renderedChunks;
	private ArrayList<WorldChunk> loadedChunks;
	private Vector3f playerLocation;
	private int[] chunkPlayerIsIn;
	private WorldChunkHandler chunkHandler;

	public World() {
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
			loadChunksAroundPlayer();
		}
		
	}
	
	public void loadChunksAroundPlayer() {
		renderedChunks.clear();
		renderer.clearChunks();
		for(int[] modifier : CHUNKS_TO_RENDER) {
			renderedChunks.add(chunkHandler.loadChunk(chunkPlayerIsIn[0]+modifier[0], 
													  chunkPlayerIsIn[1]+modifier[1], 
													  chunkPlayerIsIn[2]+modifier[2]) );
		}
		
		renderer.addChunks(renderedChunks);

	}
	
	public void addChunksToRenderer() {
		renderer.addChunks(renderedChunks);
	}
	
}
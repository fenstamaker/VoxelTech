package org.voxeltech.game;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import org.voxeltech.graphics.*;
import org.voxeltech.noise.*;
import org.voxeltech.utils.*;

public class World {
	
	public final static int h_NUMBER_LOAD_CHUNKS = 3;
	public final static int h_NUMBER_RENDER_CHUNKS = 2;
	public final static int v_NUMBER_LOAD_CHUNKS = 3;
	public final static int v_NUMBER_RENDER_CHUNKS = 1;
	
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
		chunkPlayerIsIn = new int[] { ((int)(x / (WorldChunk.SIZE*Voxel.SIZE))), 
									  ((int)(y / (WorldChunk.SIZE*Voxel.SIZE))), 
									  ((int)(z / (WorldChunk.SIZE*Voxel.SIZE))) };
	}
	
	public void loadChunksAroundPlayer() {
		for(int x = 0; x < h_NUMBER_LOAD_CHUNKS; x++) {
			for(int y = 0; y < v_NUMBER_LOAD_CHUNKS; y++) {
				for(int z = 0; z < h_NUMBER_LOAD_CHUNKS; z++) {
					WorldChunk c = chunkHandler.loadChunk(chunkPlayerIsIn[0]+x, chunkPlayerIsIn[1]+y, chunkPlayerIsIn[2]+z);
					
					loadedChunks.add(c);
					if(x <= h_NUMBER_RENDER_CHUNKS && y <= v_NUMBER_RENDER_CHUNKS 
							&& z <= h_NUMBER_RENDER_CHUNKS) {
						renderedChunks.add(c);
						renderer.addChunk(c);
					}
				}
			}
		}
	}
	
	public void addChunksToRenderer() {
		renderer.addChunks(renderedChunks);
	}
	
}
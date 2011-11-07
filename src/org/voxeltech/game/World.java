package org.voxeltech.game;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import org.voxeltech.graphics.*;
import org.voxeltech.noise.*;
import org.voxeltech.utils.*;

public class World {
	
	public final static int h_NUMBER_LOAD_CHUNKS = 1;
	public final static int h_NUMBER_RENDER_CHUNKS = 1;
	public final static int v_NUMBER_LOAD_CHUNKS = 1;
	public final static int v_NUMBER_RENDER_CHUNKS = 1;
	
	public final static int VERTICAL_LIMIT = 2;

	private ProgramClock clock = ProgramClock.getInstance();
	private ArrayList< ArrayList< ArrayList<WorldChunk> > > renderedChunks;
	private ArrayList< ArrayList< ArrayList<WorldChunk> > > loadedChunks;
	private Vector3f playerLocation;

	public World() {
		
	}
	
	public void setPlayerLocation(float x, float y, float z) {
		playerLocation = new Vector3f(x, y, z);
	}
	
	public void loadChunksAroundPlayer() {
		
	}
	
}
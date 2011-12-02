package org.voxeltech.game;

import java.lang.*;
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
	
	public final static int VERTICAL_LIMIT = 2;

	private ProgramClock clock = ProgramClock.getInstance();
	private ArrayList<WorldChunk> renderedChunks;
	private ArrayList<WorldChunk> loadedChunks;
	private Vector3f playerLocation;
	private int[] chunkPlayerIsIn;
	private WorldChunkHandler chunkHandler;
	private ThreadHandler threadHandler;

	public World() {
		renderedChunks = new ArrayList<WorldChunk>();
		loadedChunks = new ArrayList<WorldChunk>();	
	}
	
}
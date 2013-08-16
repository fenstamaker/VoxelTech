package org.voxeltech.game;

import java.lang.*;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.util.vector.Vector3f;

import org.voxeltech.graphics.*;
import org.voxeltech.noise.*;
import org.voxeltech.utils.*;

public enum World {
	INSTANCE;
	
	public final static int h_NUMBER_LOAD_CHUNKS = 3;
	public final static int h_NUMBER_RENDER_CHUNKS = 2;
	public final static int v_NUMBER_LOAD_CHUNKS = 3;
	public final static int v_NUMBER_RENDER_CHUNKS = 1;
	public final static int[] h_CHUNKS_TO_LOAD = {};
	public final static int[] v_CHUNKS_TO_LOAD = {};	
	
	public final static int VERTICAL_LIMIT = 2;

	private ProgramClock clock = ProgramClock.getInstance();
	private ArrayList<WorldChunk> chunks = new ArrayList<WorldChunk>();
	private ArrayList<WorldChunk> loadedChunks = new ArrayList<WorldChunk>();

	public boolean checkPlayerCollision(Vector3f location) {
		int[] chunk = new int[] {   ((int)(location.x / (WorldChunk.SIZE*Voxel.SIZE))), 
				   					((int)(location.y / (WorldChunk.SIZE*Voxel.SIZE))), 
				   					((int)(location.z / (WorldChunk.SIZE*Voxel.SIZE))) };
		
		//System.out.println(chunk[0] + " " + chunk[1] + " " + chunk[2]);
		
		Voxel[] player = new Voxel[Frustum.height];
		
		for(int i = 0; i < Frustum.height; i++) {
			player[i] = new Voxel(i,i,i);
			player[i].setActualPosition(location.x, location.y, location.z);
		}
		
		WorldChunk foundChunk = null;
		for(WorldChunk c : chunks) {
			if( Arrays.equals(c.coordinates, chunk) ) {
				foundChunk = c;
				break;
			}
		}
		
		if( foundChunk == null ) {
			return false;
		}
		
		for(Voxel voxel : foundChunk.terrian) {
			if( voxel.shouldRender ) {
				for(Voxel p : player) {
					if(voxel.hasCollidedWith(p)) {
						return true;
					}
				}	
			}
		}
		
		return false;
	}
	
	public void addChunks(ArrayList<WorldChunk> _chunks) {
    	chunks.addAll(_chunks);
    }
    
    public void clearChunks() {
    	chunks.clear();
    }
	
}
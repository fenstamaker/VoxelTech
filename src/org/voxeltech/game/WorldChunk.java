package org.voxeltech.game;

import java.util.ArrayList;
import java.io.Serializable;

import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;
import org.voxeltech.noise.*;

public class WorldChunk implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public final static int SIZE = 20;

	private transient ProgramClock clock = ProgramClock.getInstance();	
	private ArrayList< ArrayList< ArrayList<Voxel> > > terrian;
	/** 
	 * <em>boundaries</em> holds the min and max values of the chunk. 
	 * <em>Boundaries</em>[0] is the x-axis, <em>boundaries</em>[1]
	 * is the y-axis, and <em>boundaries</em>[2] is the z-axis.
	 */
	public transient float[][] boundaries = new float[3][2]; 
	
	/**
	 * <em>coordinates</em> refer to the chunk coordinates. (does not correspond
	 * to actual world coordinates)
	 */
	public transient int[] coordinates = new int[3];
	
	/**
	 * <em>origin</em> refers to the top-left actual coordinates. (this corresponds
	 * to actual world coordinates, i.e. where the chunk is drawn) 
	 */
	public float[] origin = new float[3];

	public WorldChunk() {
		terrian = new ArrayList<ArrayList<ArrayList<Voxel>>>();
		for(int i = 0; i < SIZE; i++) {
			terrian.add(new ArrayList<ArrayList<Voxel>>());
			for(int j = 0; j < SIZE; j++) {
				terrian.get(i).add(new ArrayList<Voxel>());
			}
		}
	}
	
	public WorldChunk(float x, float y, float z) {
		this();
		
		origin[0] = x;
		origin[1] = y;
		origin[2] = z;
		
		coordinates[0] = (int)(x / (SIZE*Voxel.SIZE));
		coordinates[1] = (int)(y / (SIZE*Voxel.SIZE));
		coordinates[2] = (int)(z / (SIZE*Voxel.SIZE));
		
		boundaries[0][0] = x;
		boundaries[0][1] = x+(SIZE*Voxel.SIZE);

		boundaries[1][0] = y;
		boundaries[1][1] = y+(SIZE*Voxel.SIZE);
		
		boundaries[2][0] = z;
		boundaries[2][1] = z+(SIZE*Voxel.SIZE);
		
		generateChunkTerrian();
	}
	
	public WorldChunk(int x, int y, int z) {
		this();
		
		origin[0] = x*(SIZE*Voxel.SIZE);
		origin[1] = y*(SIZE*Voxel.SIZE);
		origin[2] = z*(SIZE*Voxel.SIZE);
		
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		
		boundaries[0][0] = x;
		boundaries[0][1] = x+(SIZE*Voxel.SIZE);

		boundaries[1][0] = y;
		boundaries[1][1] = y+(SIZE*Voxel.SIZE);
		
		boundaries[2][0] = z;
		boundaries[2][1] = z+(SIZE*Voxel.SIZE);
		
		generateChunkTerrian();
	}
	
	private void generateChunkTerrian() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					Voxel v = new Voxel(i*Voxel.SIZE+origin[0], j*Voxel.SIZE+origin[1], k*Voxel.SIZE+origin[2]);
					
					double sn = SimplexNoise.noise(v.position.x/10.0, v.position.y/10.0, v.position.z/10.0);
			        if(sn <= 0.3)
			            v.turnOff();
			        if(sn > 0.3)
			            v.turnOn();
					
					terrian.get(i).get(j).add(v);
				}
			}
		}
	}
	
	public void render() {
		for(int i = 0; i < SIZE; i++) {
			for(int j = 0; j < SIZE; j++) {
				for(int k = 0; k < SIZE; k++) {
					terrian.get(i).get(j).get(k).render();
				}
			}
		}
	}
	
}

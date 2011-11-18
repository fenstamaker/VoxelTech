package org.voxeltech.game;

import java.io.Serializable;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import org.apache.commons.lang3.ArrayUtils;

import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;
import org.voxeltech.noise.*;

public class WorldChunk implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public final static int SIZE = 20;

	private transient ProgramClock clock = ProgramClock.getInstance();	
	private TerrianList<Voxel> terrian;
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

	public ArrayList<ArrayList<Float>> normals = new ArrayList<ArrayList<Float>>();
	public ArrayList<ArrayList<Float>> texCoords = new ArrayList<ArrayList<Float>>();
	public ArrayList<ArrayList<Float>> vertices = new ArrayList<ArrayList<Float>>();
	public ArrayList<Float> color = new ArrayList<Float>();
	public FloatBuffer normalBuffer = null;
    public FloatBuffer texBuffer = null;
    public FloatBuffer vertexBuffer = null;
	
	public WorldChunk() {
		terrian = new TerrianList<Voxel>(3*SIZE);
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
		for(int k = 0; k < SIZE; k++) {
			for(int j = 0; j < SIZE; j++) {
				for(int i = 0; i < SIZE; i++) {
					Voxel v = new Voxel(i*Voxel.SIZE+origin[0], j*Voxel.SIZE+origin[1], k*Voxel.SIZE+origin[2]);
					v.setCoordinates(i, j, k);
					
					double sn = SimplexNoise.noise(v.position.x/10.0, v.position.y/10.0, v.position.z/10.0);//, clock.getTime());
			        if(sn < 0.3)
			            v.turnOff();
			        if(sn >= 0.3)
			            v.turnOn();
			        
			        v.setColor((float)Math.abs(sn)*.8f, (float)Math.abs(sn)*.2f, (float)Math.abs(sn)*.3f);
					
					terrian.add(v);
				}
			}
		}
	}
	
	public void generateMesh() {
				
	}
	
	public void applyAnimation() {
		for(Voxel v : terrian) {
			double sn = SimplexNoise.noise(v.position.x/10.0, v.position.y/10.0, v.position.z/10.0);//, clock.getTime());
			
	        if(sn < 0.3)
	            v.turnOff();
	        if(sn >= 0.3)
	            v.turnOn();
	        
	        v.setColor((float)Math.abs(sn)*.8f, (float)Math.abs(sn)*.2f, (float)Math.abs(sn)*.3f);
		}
	}
	
	public void render() {
		generateMesh();
		
		GL11.glPushMatrix();
        
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, Voxel.image.getTextureID());
    	GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
    	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		vertexBuffer = BufferUtils.createFloatBuffer(72);
        
        for(Voxel vox : terrian) {
        	
        	if(vox.shouldRender()) {
        		GL11.glColor4f(vox.color.r, vox.color.g, vox.color.b, 1.0f);
        		vertexBuffer.clear();
        		vertexBuffer.put( vox.mesh.vertices );
                
                vertexBuffer.rewind();
                vox.mesh.normalBuffer.rewind();
                vox.mesh.texBuffer.rewind();
                GL11.glNormalPointer(0, vox.mesh.normalBuffer);
                GL11.glTexCoordPointer(2, 0, vox.mesh.texBuffer);
                GL11.glVertexPointer(3, 0, vertexBuffer);
                GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
        	}
        	
        }
        
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            
    	GL11.glPopMatrix();
	}
	
}

package org.voxeltech.game;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import org.apache.commons.lang3.ArrayUtils;

import org.voxeltech.audio.AudioController;
import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;
import org.voxeltech.noise.*;

public class WorldChunk implements Externalizable{

	public final static int SIZE = 10;

	private ProgramClock clock = ProgramClock.getInstance();	
    private Frustum frustum = Frustum.INSTANCE;
	public TerrianList<Voxel> terrian;
	
	/**
	 * <em>coordinates</em> refer to the chunk coordinates. (does not correspond
	 * to actual world coordinates)
	 */
	public int[] coordinates = new int[3];
	
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
		
		coordinates[0] = (int)(x / (SIZE*Voxel.SIZE));
		coordinates[1] = (int)(y / (SIZE*Voxel.SIZE));
		coordinates[2] = (int)(z / (SIZE*Voxel.SIZE));
		
		calculateChunkInfo(coordinates[0], coordinates[1], coordinates[2]);
		
		generateChunkTerrian();
	}
	
	public WorldChunk(int x, int y, int z) {
		this();
		
		coordinates[0] = x;
		coordinates[1] = y;
		coordinates[2] = z;
		
		calculateChunkInfo(x, y, z);
		
		generateChunkTerrian();
	}
	
	private void calculateChunkInfo(int x, int y, int z) {
		origin[0] = x*(SIZE*Voxel.SIZE);
		origin[1] = y*(SIZE*Voxel.SIZE);
		origin[2] = z*(SIZE*Voxel.SIZE);
	}
	
	private void generateChunkTerrian() {
		for(int k = 0; k < SIZE; k++) {
			for(int j = 0; j < SIZE; j++) {
				for(int i = 0; i < SIZE; i++) {
					
					Voxel vox = new Voxel(i, j, k);
					vox.setActualPosition((i*Voxel.SIZE+origin[0]), (j*Voxel.SIZE+origin[1]), (k*Voxel.SIZE+origin[2]));
					
					terrian.add(vox);
				}
			}
		}
		
		applyAnimation();
	}
	
	public void applyAnimation() {
		for(Voxel vox : terrian) {
			double noise2d = SimplexNoise.noise( vox.actualPosition[0]/10.0, vox.actualPosition[2]/10.0 );
			double noise3d = SimplexNoise.noise( vox.actualPosition[0]/15.0, vox.actualPosition[1]/15.0, vox.actualPosition[2]/15.0);//, clock.getTime());
	        
			float seaLevel = 3.0f;
			
			if(noise3d >= 0.3) {
				vox.turnOn();
				if( noise2d*WorldChunk.SIZE < vox.actualPosition[1] ) {
					vox.turnOff();
				}
			} else {
				vox.turnOff();
				if( noise2d*WorldChunk.SIZE < vox.actualPosition[1] ) {
					vox.turnOn();
				}
			}
			
			if( vox.actualPosition[1] > seaLevel ) {
				vox.turnOff();
				if( vox.actualPosition[1] < noise2d*WorldChunk.SIZE ) {
					vox.turnOn();
				}
			}
			
			if( vox.actualPosition[1] > seaLevel - 5.0f ) {
				float colorMultiplier = ( (float)Math.abs(noise3d) + .3f);
		        vox.setColor( colorMultiplier*.2f, colorMultiplier*.5f, 0f);
			}
			
	        
		}
	}
	
	public void render() {
		
		GL11.glPushMatrix();
        
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, Voxel.image.getTextureID());
    	GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
    	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		vertexBuffer = BufferUtils.createFloatBuffer(72);
        
        for(Voxel vox : terrian) {
        	
        	if( vox.shouldRender && frustum.isInFrustum(vox) ) {
        		Vector3f voxPosition = new Vector3f( vox.actualPosition[0], vox.actualPosition[1], vox.actualPosition[2] );
        		GL11.glColor4f(vox.color[0], vox.color[1], vox.color[2], 1.0f);
        		vertexBuffer.clear();
        		vertexBuffer.put( Mesh.getVertices(voxPosition) );
                
                vertexBuffer.rewind();
                GL11.glNormalPointer(0, Mesh.getNormalBuffer());
                GL11.glTexCoordPointer(2, 0, Mesh.getTexBuffer());
                GL11.glVertexPointer(3, 0, vertexBuffer);
                GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
        	}
        	
        }
        
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
            
    	GL11.glPopMatrix();
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		coordinates = new int[] { in.readInt(), in.readInt(), in.readInt() };
		
		calculateChunkInfo(coordinates[0], coordinates[1], coordinates[2]);
		
		terrian = new TerrianList<Voxel>();
		
		while(true) {
			try {
				terrian.add((Voxel)in.readObject());
			} catch(Exception e) {
				break;
			}
		}
		
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeInt(coordinates[0]);
		out.writeInt(coordinates[1]);
		out.writeInt(coordinates[2]);
		
		for(Voxel vox : terrian) {
			out.writeObject(vox);
		}
	}
	
}

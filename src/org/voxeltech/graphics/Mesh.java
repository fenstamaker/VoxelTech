package org.voxeltech.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {
	
	public ArrayList<Float> normals = new ArrayList<Float>();
    public ArrayList<Float> texCoords = new ArrayList<Float>();
    public ArrayList<ArrayList<ArrayList<Float>>> vertices = new ArrayList<ArrayList<ArrayList<Float>>>();
    
    public FloatBuffer normalBuffer = null;
    public FloatBuffer texBuffer = null;
    public FloatBuffer vertexBuffer = null;

    public int getNormalLength() { return normals.size(); }

    /**
     * Faces - ordered in such a way that:
     *  - [0] are the normals
     *  - [1] are the texCoords
     */
    
    public static Float[][] frontFace = { 
								    		{0.0f, 0.0f, 1.0f},
								    		{0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f}
								    	};
    
    public static Float[][] backFace = 	{ 
											{0.0f, 0.0f, -1.0f},
											{1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f}
										};
    
    public static Float[][] leftFace = 	{ 
											{-1.0f, 0.0f, 0.0f},
											{0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f}
										};
    
    public static Float[][] rightFace = { 
											{1.0f, 0.0f, 0.0f},
											{1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f}
										};
    
    public static Float[][] topFace =	{ 
											{0.0f, 1.0f, 0.0f},
											{0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f}
										};
    
    public static Float[][] bottomFace = { 
											{0.0f, -1.0f, 0.0f},
											{1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f}
										};
    
    public static Float[] getTopVertices(Vector3f position) {
    	Float[] v = {
				// Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
				position.x, position.y+Voxel.SIZE, position.z-Voxel.SIZE, 
				position.x, position.y+Voxel.SIZE, position.z,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z-Voxel.SIZE
			};
    	return v;
    }
    
    public static Float[] getBottomVertices(Vector3f position) {
    	Float[] v = {
    			// Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
    			position.x, position.y, position.z-Voxel.SIZE,
				position.x+Voxel.SIZE, position.y, position.z-Voxel.SIZE,
				position.x+Voxel.SIZE, position.y, position.z, 
				position.x, position.y, position.z
			};
    	return v;
    }
    
    public static Float[] getFrontVertices(Vector3f position) {
    	Float[] v = {
    			// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
				position.x, position.y, position.z,
				position.x+Voxel.SIZE, position.y, position.z,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z,  
				position.x, position.y+Voxel.SIZE, position.z
			};
    	return v;
    }
    
    public static Float[] getBackVertices(Vector3f position) {
    	Float[] v = {
    			// Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
				position.x, position.y, position.z-Voxel.SIZE,
				position.x, position.y+Voxel.SIZE, position.z-Voxel.SIZE,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z-Voxel.SIZE,  
				position.x+Voxel.SIZE, position.y, position.z-Voxel.SIZE
			};
    	return v;
    }
    
    public static Float[] getLeftVertices(Vector3f position) {
    	Float[] v = {
    			// Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
				position.x, position.y, position.z-Voxel.SIZE,
				position.x, position.y, position.z,
				position.x, position.y+Voxel.SIZE, position.z,
				position.x, position.y+Voxel.SIZE, position.z-Voxel.SIZE
			};
    	return v;
    }
    
    public static Float[] getRightVertices(Vector3f position) {
    	Float[] v = {
    			// Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bototm Left}
				position.x+Voxel.SIZE, position.y, position.z-Voxel.SIZE,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z-Voxel.SIZE,
				position.x+Voxel.SIZE, position.y+Voxel.SIZE, position.z,
				position.x+Voxel.SIZE, position.y, position.z
			};
    	return v;
    }
    
    
    
	public Mesh(Vector3f position, float size) {
		
			float[] n = {
						0.0f, 0.0f,  1.0f, // Front Face
						0.0f, 0.0f, -1.0f, //Back Face
						0.0f, 1.0f, 0.0f, // Top Face
						0.0f, -1.0f, 0.0f, // Bottom Face
						1.0f, 0.0f, 0.0f, // Right Face
						-1.0f, 0.0f, 0.0f // Left Face
					};
		
			float[] t = {   // { {0,0} , {0,0}, {0, 0}, {0,0} },  // Clears face
						0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f, // Front Face
						1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Back Face
						0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f, // Top Face
						1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f, // Bottom Face
						1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Right Face
						0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f  // Left Face
					};

			float[] v = {
						// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						position.x, position.y, position.z,
						position.x+size, position.y, position.z,
						position.x+size, position.y+size, position.z,  
						position.x, position.y+size, position.z,

						// Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
						position.x, position.y, position.z-size,
						position.x, position.y+size, position.z-size,
						position.x+size, position.y+size, position.z-size,  
						position.x+size, position.y, position.z-size,

						// Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
						position.x, position.y+size, position.z-size, 
						position.x, position.y+size, position.z,
						position.x+size, position.y+size, position.z,
						position.x+size, position.y+size, position.z-size,

						// Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
						position.x, position.y, position.z-size,
						position.x+size, position.y, position.z-size,
						position.x+size, position.y, position.z, 
						position.x, position.y+size, position.z,

						// Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bototm Left}
						position.x+size, position.y, position.z-size,
						position.x+size, position.y+size, position.z-size,
						position.x+size, position.y+size, position.z,
						position.x+size, position.y, position.z,

						// Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						position.x, position.y, position.z-size,
						position.x, position.y, position.z,
						position.x, position.y+size, position.z,
						position.x, position.y+size, position.z-size
					};

			float[][][] v2 = {
							// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						  { {position.x, position.y, position.z},
						    {position.x+size, position.y, position.z},
						    {position.x+size, position.y+size, position.z},
						    {position.x, position.y+size, position.z} },
		
			                // Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
						  { {position.x, position.y, position.z-size},
						    {position.x, position.y+size, position.z-size},
						    {position.x+size, position.y+size, position.z-size},
						    {position.x+size, position.y, position.z-size} },
		
						    // Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
						  { {position.x, position.y+size, position.z-size},
						    {position.x, position.y+size, position.z},
						    {position.x+size, position.y+size, position.z},
						    {position.x+size, position.y+size, position.z-size} },
		
						    // Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
						  { {position.x, position.y, position.z-size},
						    {position.x+size, position.y, position.z-size},
						    {position.x+size, position.y, position.z},
						    {position.x, position.y+size, position.z} },
		
						    // Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bototm Left}
						  { {position.x+size, position.y, position.z-size},
						    {position.x+size, position.y+size, position.z-size},
						    {position.x+size, position.y+size, position.z},
						    {position.x+size, position.y, position.z} },
		
						    // Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						  { {position.x, position.y, position.z-size},
						    {position.x, position.y, position.z},
						    {position.x, position.y+size, position.z},
						    {position.x, position.y+size, position.z-size} }
	                    };

			normals = new ArrayList(Arrays.asList(n));
			texCoords = new ArrayList(Arrays.asList(t));
			vertices = new ArrayList(Arrays.asList(v2));
	        
			normalBuffer = BufferUtils.createFloatBuffer(n.length);
			texBuffer = BufferUtils.createFloatBuffer(t.length);
			vertexBuffer = BufferUtils.createFloatBuffer(v.length);
			
			normalBuffer.put(n);
			texBuffer.put(t);
			vertexBuffer.put(v);

	    }

	    public void changePosition(Vector3f position, float size) {
			float[] v = {
					// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					position.x, position.y, position.z,
					position.x+size, position.y, position.z,
					position.x+size, position.y+size, position.z,  
					position.x, position.y+size, position.z,

					// Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
					position.x, position.y, position.z-size,
					position.x, position.y+size, position.z-size,
					position.x+size, position.y+size, position.z-size,  
					position.x+size, position.y, position.z-size,

					// Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
					position.x, position.y+size, position.z-size, 
					position.x, position.y+size, position.z,
					position.x+size, position.y+size, position.z,
					position.x+size, position.y+size, position.z-size,

					// Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
					position.x, position.y, position.z-size,
					position.x+size, position.y, position.z-size,
					position.x+size, position.y, position.z, 
					position.x, position.y+size, position.z,

					// Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bototm Left}
					position.x+size, position.y, position.z-size,
					position.x+size, position.y+size, position.z-size,
					position.x+size, position.y+size, position.z,
					position.x+size, position.y, position.z,

					// Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					position.x, position.y, position.z-size,
					position.x, position.y, position.z,
					position.x, position.y+size, position.z,
					position.x, position.y+size, position.z-size
				};

			float[][][] v2 = {
						// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					  { {position.x, position.y, position.z},
					    {position.x+size, position.y, position.z},
					    {position.x+size, position.y+size, position.z},
					    {position.x, position.y+size, position.z} },
	
		                // Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
					  { {position.x, position.y, position.z-size},
					    {position.x, position.y+size, position.z-size},
					    {position.x+size, position.y+size, position.z-size},
					    {position.x+size, position.y, position.z-size} },
	
					    // Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
					  { {position.x, position.y+size, position.z-size},
					    {position.x, position.y+size, position.z},
					    {position.x+size, position.y+size, position.z},
					    {position.x+size, position.y+size, position.z-size} },
	
					    // Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
					  { {position.x, position.y, position.z-size},
					    {position.x+size, position.y, position.z-size},
					    {position.x+size, position.y, position.z},
					    {position.x, position.y+size, position.z} },
	
					    // Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bototm Left}
					  { {position.x+size, position.y, position.z-size},
					    {position.x+size, position.y+size, position.z-size},
					    {position.x+size, position.y+size, position.z},
					    {position.x+size, position.y, position.z} },
	
					    // Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					  { {position.x, position.y, position.z-size},
					    {position.x, position.y, position.z},
					    {position.x, position.y+size, position.z},
					    {position.x, position.y+size, position.z-size} }
                    };
			vertices = new ArrayList(Arrays.asList(v2));
			    
		    vertexBuffer = BufferUtils.createFloatBuffer(v.length);
		    vertexBuffer.put(v);
	    }
	
}

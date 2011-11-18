package org.voxeltech.graphics;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Mesh implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public ArrayList<Float> normals = new ArrayList<Float>();
    public ArrayList<Float> texCoords = new ArrayList<Float>();
    public float[] vertices;
    public ArrayList<ArrayList<ArrayList<Float>>> vertices3f = new ArrayList<ArrayList<ArrayList<Float>>>();

    public transient static FloatBuffer normalBuffer = null;
    public transient static FloatBuffer texBuffer = null;
    
    public int getNormalLength() { return normals.size(); }    
    
    public Mesh() {
    	if(normalBuffer == null) {
        	
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
	    		
        	normalBuffer = BufferUtils.createFloatBuffer(n.length);
    		texBuffer = BufferUtils.createFloatBuffer(t.length);

    		normalBuffer.put(n);
    		texBuffer.put(t);
    	}

    }
    
	public Mesh(Vector3f position, float size) {
			this();
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
						position.x, position.y, position.z,

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

			vertices = v;
			vertices3f = new ArrayList(Arrays.asList(v2));

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
			vertices = v;
			vertices3f = new ArrayList(Arrays.asList(v2));
	    }
	
}

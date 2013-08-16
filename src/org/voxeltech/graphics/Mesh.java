package org.voxeltech.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Mesh {
	
	public static FloatBuffer normalBuffer = null;
	public static FloatBuffer texBuffer = null;
	
	public static FloatBuffer getNormalBuffer() {
		if(normalBuffer == null) {
			float[] n = {
					0.0f, 0.0f, 1.0f, // Front Face
					0.0f, 0.0f, -1.0f, //Back Face
					0.0f, 1.0f, 0.0f, // Top Face
					0.0f, -1.0f, 0.0f, // Bottom Face
					1.0f, 0.0f, 0.0f, // Right Face
					-1.0f, 0.0f, 0.0f // Left Face
				};

			normalBuffer = BufferUtils.createFloatBuffer(n.length);
			normalBuffer.put(n);
		}
		
		normalBuffer.rewind();
		return normalBuffer;
	}
	
	public static FloatBuffer getTexBuffer() {
		if(texBuffer == null) {
			float[] t = {   // { {0,0} , {0,0}, {0, 0}, {0,0} },  // Clears face
							0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f, // Front Face
							1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Back Face
							0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f, // Top Face
							1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f, // Bottom Face
							1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Right Face
							0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f  // Left Face
						};
				
			texBuffer = BufferUtils.createFloatBuffer(t.length);
			texBuffer.put(t);
		}
		
		texBuffer.rewind();
		return texBuffer;
	}
	    
	public static float[] getVertices(Vector3f position) {
			float[] v = {
						// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,

						// Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,

						// Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,

						// Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,

						// Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,
						position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,
						position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,

						// Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF,
						position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF,
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF,
						position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF,
					};

			return v;
		}

	public static ArrayList<ArrayList<ArrayList<Float>>> getVertices3f(Vector3f position) {
		float[][][] v2 = {
					
						// Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					  { { position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF },
						{ position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF },
						{ position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF, } },

						// Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
					  { { position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF },
						{ position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF },
						{ position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF, } },

						// Top Face - {Top Left}, {Bottom Left}, {Bottom Right}, {Top Right}
					  { { position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF },
						{ position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF, } },

						// Bottom Face - {Top Right}, {Top Left}, {Bottom Left}, {Bottom Right}
					  { { position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF },
						{ position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF },
						{ position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF },
						{ position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF, } },

						// Right face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
					  { { position.x + Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF },
						{ position.x + Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF },
						{ position.x + Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF, } },

						// Left Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
					  { { position.x - Voxel.HALF, position.y - Voxel.HALF, position.z - Voxel.HALF },
						{ position.x - Voxel.HALF, position.y - Voxel.HALF, position.z + Voxel.HALF },
						{ position.x - Voxel.HALF, position.y + Voxel.HALF, position.z + Voxel.HALF },
						{ position.x - Voxel.HALF, position.y + Voxel.HALF, position.z - Voxel.HALF, } }
					  
					};
		
		return new ArrayList(Arrays.asList(v2));
    }
	
}

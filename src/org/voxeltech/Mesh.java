package org.voxeltech;

/**
 *
 * @author Gary
 */
public class Mesh {
    
    public static short[] getIndices() {
        short[] i = { 0, 1, 2, 0, 2, 3, 4, 0, 3, 4, 3, 5, 6, 4, 5, 6, 5, 7, 1, 6, 7, 1, 7, 2, 3, 2, 7, 3, 7, 5, 0, 1, 6, 0, 6, 4 }; 
        return i;
    }
    
    public static float[] getNormals() {
        float[] n = {
            0.0f, 0.0f, 1.0f, // Front Face
            -1.0f, 0.0f, 0.0f, // Left Face
            0.0f, 0.0f, -1.0f, //Back Face
            1.0f, 0.0f, 0.0f, // Right Face
            0.0f, 1.0f, 0.0f, // Top Face
            0.0f, -1.0f, 0.0f, // Bottom Face
        };
        return n;
    }
    
    public static float[] getTexCoords() {
        float[] t = {
            0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f, // Front Face
            1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Back Face
            0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f, // Top Face
            1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f,  1.0f, 0.0f, // Bottom Face
            1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f,  0.0f, 0.0f, // Right Face
            0.0f, 0.0f,  1.0f, 0.0f,  1.0f, 1.0f,  0.0f, 1.0f  // Left Face
        };
        return t;
    }
    
    public static float[] getVertices(float[] position, float half) {
        
        float[] v = {
            // Front Face - {Bottom Left}, {Bottom Right}, {Top Right}, {Top Left}
            position[0] - half, position[1] - half, position[2] + half, // 0
            position[0] + half, position[1] - half, position[2] + half, // 1
            position[0] + half, position[1] + half, position[2] + half, // 2
            position[0] - half, position[1] + half, position[2] + half, // 3

            // Left Face - {Bottom Left}, {Top Left}
            position[0] - half, position[1] - half, position[2] - half, // 4
            position[0] - half, position[1] + half, position[2] - half, // 5

            // Back Face - {Bottom Right}, {Top Right}, {Top Left}, {Bottom Left}
            position[0] + half, position[1] - half, position[2] - half, // 6
            position[0] + half, position[1] + half, position[2] - half, // 7
        };

        return v;
    }
}

package org.voxeltech;

import org.voxeltech.noise.SimplexNoise;

/**
 *
 * @author Gary
 */
public class WorldChunk implements Chunk {
    
    public static final float SIZE = 1.0f;
    public static final float RADIUS = 1.5f;
    public static final int CX = 16;
    public static final int CZ = 32;
    public static final int CY = 32;
    public static final int MAXHEIGHT = 20;
    public static final int MINHEIGHT = 15;
    
    public static final float MINRED = .08f;
    public static final float MAXRED = .48f;
    
    public static final float MINGREEN = .38f;
    public static final float MAXGREEN = .56f;
    
    public static final float MINBLUE = .125f;
    public static final float MAXBLUE = .3f;
    
    public Position3f realPosition;

    public byte[][][] blocks = new byte[CX][CZ][CY];
    public float[][][][] colors = new float[CX][CZ][CY][4];
    
    public WorldChunk(Position3f position) {
        realPosition = position;
        for(int i = 0; i < CX; i++) {
            for(int j = 0; j < CZ; j++) {
                double noise = SimplexNoise.noise((realPosition.x+i)/30, 0);
                int height = (int)(MINHEIGHT*(1-noise) + MAXHEIGHT*noise);
                
                for(int k = 0; k < height; k++) {
                    blocks[i][j][k] = 1;
                    colors[i][j][k][0] = (float)(MINRED*(1-noise) + MAXRED*noise);
                    colors[i][j][k][1] = (float)(MINGREEN*(1-noise) + MAXGREEN*noise);
                    colors[i][j][k][2] = (float)(MINBLUE*(1-noise) + MAXBLUE*noise);
                    colors[i][j][k][3] = (float)(.5*(1-noise) + 1.0*noise);
                }
            }
        }
    }
    
    public byte getBlock(Position3d position) {
        return blocks[position.x][position.z][position.y];
    }
    
    public float[] getColor(Position3d position) {
        return colors[position.x][position.z][position.y];
    }

    public void setColor(Position3d position, float r, float g, float b) {
        float[] color = colors[position.x][position.z][position.y];
        color[0] = r;
        color[1] = g;
        color[2] = b;
    }
    
}

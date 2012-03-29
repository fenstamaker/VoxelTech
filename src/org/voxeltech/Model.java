package org.voxeltech;


public class Model {
    
    public static final float SIZE = 1.0f;
    public static final float RADIUS = 2f;
    public static final int CX = 5;
    public static final int CZ = 5;
    public static final int CY = 5;
    
    public float center[] = new float[]{CX/2.0f, CY/2.0f, CZ/2.0f};
    
    public byte[][][] blocks = new byte[CX][CZ][CY];
    public float[][][][] colors = new float[CX][CZ][CY][4];
    
    public Model() {
        for(int i = 0; i < CX; i++) {
            for(int j = 0; j < CZ; j++) {
                for(int k = 0; k < CY; k++) {
                    float r = (float)Math.sqrt( ((i-center[0])*(i-center[0])) + ((k-center[1])*(k-center[1])) + ((j-center[2])*(j-center[2])) );
                    if (r <= RADIUS) {
                        blocks[i][j][k] = 1;
                    }
                    colors[i][j][k][0] = 1.0f;
                    colors[i][j][k][1] = 0.0f;
                    colors[i][j][k][2] = 0.0f;
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
    
    
}

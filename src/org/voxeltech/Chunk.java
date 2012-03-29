package org.voxeltech;

/**
 *
 * @author Gary
 */
public interface Chunk {
    
    public float[] getColor(Position3d position);
    public void setColor(Position3d position, float r, float g, float b);
}

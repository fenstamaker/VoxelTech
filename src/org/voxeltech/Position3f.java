package org.voxeltech;

/**
 *
 * @author Gary
 */
public class Position3f {
    
    public float x,y,z;
    
    public Position3f(float _x, float _y, float _z) {
       this.x = _x;
       this.y = _y;
       this.z = _z;
    }
    
    public void set(float _x, float _y, float _z) {
       this.x = _x;
       this.y = _y;
       this.z = _z;
    }
    
    public float[] toArray() {
        return new float[] { x, y, z };
    }
    
    @Override
    public String toString() {
        return "" + x + ", " + y + ", " + z;
    }
}

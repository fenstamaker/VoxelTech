package org.voxeltech;

/**
 *
 * @author Static
 */
public class Vector3f extends Position3f {
    
    public Vector3f(float x, float y, float z) {
        super(x, y, z);
    }
    
    public Vector3f(Vector3f vector) {
        super(vector.x, vector.y, vector.z);
    }
    
    public Vector3f(Position3f position) {
        super(position.x, position.y, position.z);
    }
    
    public Vector3f() {
        super(0,0,0);
    }
    
    public void scale(float distance) {
        this.x *= distance;
        this.y *= distance;
        this.z *= distance;
    }
    
    public void negate() {
        this.x = -1*x;
        this.y = -1*y;
        this.z = -1*z;
    }
    
    public void normalize() {
        float l = length();
        this.set(x/l, y/l, z/l);
    }
    
    public void normalise() {
        this.normalize();
    }
    
    public float length() {
        return (float)Math.sqrt( x * x + y * y + z * z );
    }
    
    public static Vector3f cross(Vector3f left, Vector3f right) {
        Vector3f dest = new Vector3f();
        dest.set(   
                    left.y * right.z - left.z * right.y,
                    right.x * left.z - right.z * left.x,
                    left.x * right.y - left.y * right.x 
                );
        return dest;
    }
    
    public static float dot(Vector3f left, Vector3f right) {
        return left.x * right.x + left.y * right.y + left.z * right.z;
    }
    
    public static Vector3f add(Vector3f left, Vector3f right) {
        Vector3f dest = new Vector3f();
        dest.set(   
                    left.x + right.x,
                    left.y + right.y,
                    left.z + right.z
                );
        return dest;
    }
    
    public static Vector3f sub(Vector3f left, Vector3f right) {
        Vector3f dest = new Vector3f();
        dest.set(   
                    left.x - right.x,
                    left.y - right.y,
                    left.z - right.z
                );
        return dest;
    }
}

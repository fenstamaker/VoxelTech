package org.voxeltech;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.lwjgl.opengl.GL11;
import java.nio.FloatBuffer;
import org.lwjgl.util.glu.GLU;

public enum Frustum {
    INSTANCE;

    public static final int NEAR = 5;
    public static final int FAR = 4;
    public static final int RIGHT = 3;
    public static final int LEFT = 2;
    public static final int TOP = 1;
    public static final int BOTTOM = 0;
    public static final int height = 1;

    public static float nearDistance = 0.1f;
    public static float farDistance = 200.0f;
    public static float fov = 65.0f;

    protected static float mouseSensitivity = 0.08f;
    public static Vector3f xAxis = new Vector3f(1.0f, 0, 0);
    public static Vector3f yAxis = new Vector3f(0, 1.0f, 0);
    public static Vector3f zAxis = new Vector3f(0, 0, 1.0f);

    public float horizontalAngle = 0.0f;
    public float verticalAngle = 0.0f;
    public Vector3f up = new Vector3f(0, 1, 0);
    public Vector3f direction = new Vector3f(0.0f, -1.0f, 1.0f);
    public Vector3f position = new Vector3f(0.0f, 25.0f, -20.0f);
    public Vector3f right;

    private Matrix4f projection;
    private Matrix4f modelview;
    private Matrix4f identity;

    private FloatBuffer buffer;

    private Plane[] planes = new Plane[6];

    private Frustum() {
        for(int i = 0; i < 6; i++) {
                planes[i] = new Plane();
        }
        ByteBuffer bb = ByteBuffer.allocateDirect(16*4);
        bb.order(ByteOrder.nativeOrder());
        buffer = bb.asFloatBuffer();
        projection = new Matrix4f();
        modelview = new Matrix4f();
        identity = new Matrix4f();
        identity.setIdentity();
    }

    /*
    * Frustum calculation algorithm, based on 
    * http://www.racer.nl/reference/vfc_markmorley.htm
    * 
    */
    public void calculateFrustum() {
        buffer.clear();

        calculateRight();

        buffer.rewind();
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, buffer);
        projection.load(buffer);

        buffer.clear();

        buffer.rewind();
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
        modelview.load(buffer);


        Matrix4f clip = Matrix4f.mul(projection, modelview);

        planes[RIGHT] = new Plane(  clip.m03-clip.m00, clip.m13-clip.m10, clip.m23-clip.m20, clip.m33-clip.m30 );
        planes[LEFT] = new Plane(   clip.m03+clip.m00, clip.m13+clip.m10, clip.m23+clip.m20, clip.m33+clip.m30 );
        planes[TOP] = new Plane(    clip.m03-clip.m01, clip.m13-clip.m11, clip.m23-clip.m21, clip.m33-clip.m31 );
        planes[BOTTOM] = new Plane( clip.m03+clip.m01, clip.m13+clip.m11, clip.m23+clip.m21, clip.m33+clip.m31 );
        planes[FAR] = new Plane(    clip.m03-clip.m02, clip.m13-clip.m12, clip.m23-clip.m22, clip.m33-clip.m32 );
        planes[NEAR] = new Plane(   clip.m03+clip.m02, clip.m13+clip.m12, clip.m23+clip.m22, clip.m33+clip.m32 );

        for(Plane plane : planes) {
                plane.normalize();
        }
    }

    public boolean isInFrustum(Position3f position) {
        Vector3f center = new Vector3f(position);

        float distance;
        for(int i = 0; i < 6; i++) {
                distance = planes[i].getDistance1(center);
                if( distance <= -WorldChunk.RADIUS ) {
                        return false;
                }
        }

        return true;
    }

    private void calculateRight() {
        right = Vector3f.cross(zAxis, up);
    }
    
    public void lookAt(Vector3f look) {
        direction.set(look.x, look.y, look.z);
    }

    public void rotate(float dx, float dy) {
        reset();

        horizontalAngle += dx * mouseSensitivity;
        verticalAngle -= dy * mouseSensitivity;

        direction.x = -1 * (float)Math.sin( Math.toRadians(horizontalAngle) );
        direction.z = (float)Math.cos( Math.toRadians(horizontalAngle) );
        direction.y = (float)Math.sin( Math.toRadians(verticalAngle) );
    }

    private void updatePosition(Vector3f movement) {
        Vector3f temp = Vector3f.add(position, movement);
        position = temp;
    }

    public void forward(float distance) {
        Vector3f movement = new Vector3f(direction);
        movement.scale(distance);

        updatePosition(movement);
}

    public void backwards(float distance) {
        Vector3f movement = new Vector3f(direction);
        movement.scale(distance);
        movement.negate();

        updatePosition(movement);
    }

    public void left(float distance) {
        Vector3f movement = new Vector3f(1, 0, 0);
        movement.scale(distance);
        movement.negate();

        updatePosition(movement);
    }

    public void right(float distance) {
        Vector3f movement = new Vector3f(1, 0, 0);
        movement.scale(distance);

        updatePosition(movement);
    }
    
    public void up(float distance) {
        Vector3f movement = new Vector3f(up);
        movement.scale(distance);

        updatePosition(movement);
    }

    public void down(float distance) {
        Vector3f movement = new Vector3f(up);
        movement.scale(distance);
        movement.negate();

        updatePosition(movement);
    }
    
    public void reset() {
    	//rotation = new Quaternion(1, 0, 0, 0);
    }
    
    public void update() {
        
        direction.normalise();
        calculateRight();
        right.normalise();
        up.normalise();
        
        float[] view = { 
            right.x, up.x, -direction.x, 0.0f,
            right.y, up.y, -direction.y, 0.0f,
            right.z, up.z, -direction.z, 0.0f,
            -Vector3f.dot(right, position), -Vector3f.dot(up, position), Vector3f.dot(direction, position), 1.0f   
        };
        buffer.clear();
        buffer.position(0);
        buffer.put(view);
        buffer.position(0);
        
        GL11.glLoadIdentity();
        GL11.glMultMatrix(buffer);
        
//        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
//        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
//        GL11.glTranslatef(position.x, position.y, position.z);
    }


}
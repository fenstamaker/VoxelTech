package org.voxeltech.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

import org.voxeltech.game.*;

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
	
	protected static float mouseSensitivity = 0.04f;
	public static Vector3f xAxis = new Vector3f(1.0f, 0, 0);
	public static Vector3f yAxis = new Vector3f(0, 1.0f, 0);
	public static Vector3f zAxis = new Vector3f(0, 0, 1.0f);
	
	public float horizontalAngle = 0.0f;
	public float verticalAngle = 0.0f;
	public Vector3f up = new Vector3f(0, 1, 0);
	public Vector3f direction = new Vector3f(0, 0, -1);
	public Vector3f position = new Vector3f(8, -7, 0);
	public Vector3f right;
	
	private Matrix4f projection;
	private Matrix4f modelview;
	private Matrix4f identity;
	
	private World world = World.INSTANCE;
	
	private FloatBuffer buffer;
	
	private Plane[] planes = new Plane[6];
	
	private Frustum() {
		for(int i = 0; i < 6; i++) {
			planes[i] = new Plane();
		}
		buffer = BufferUtils.createFloatBuffer(16);
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
		
		
		Matrix4f clip = new Matrix4f();
		Matrix4f.mul( projection, modelview,clip);
		
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
	
	public boolean isInFrustum(Voxel voxel) {
		Vector3f center = new Vector3f(voxel.actualPosition[0], voxel.actualPosition[1], voxel.actualPosition[2]);
		
		float distance;
		for(int i = 0; i < 6; i++) {
			distance = planes[i].getDistance1(center);
			if( distance <= -Voxel.RADIUS ) {
				return false;
			}
		}
		
		return true;
	}
	
	private void calculateRight() {
		right = Vector3f.cross(direction, up, null);
	}
	
	public void rotate(float dx, float dy) {
		reset();
		
		horizontalAngle += dx * mouseSensitivity;
		verticalAngle -= dy * mouseSensitivity;
		
		direction.x = -1 * (float)Math.sin( Math.toRadians(horizontalAngle) );
		direction.z = (float)Math.cos( Math.toRadians(horizontalAngle) );
		direction.y = (float)Math.sin( Math.toRadians(verticalAngle) );
		/*
		Quaternion horizontalRotation, verticalRotation;
		
		horizontalRotation = new Quaternion();
		horizontalRotation.setFromAxisAngle( new Vector4f(0f, 1f, 0f, (float)Math.toRadians(-horizontalAngle) ) );
		horizontalRotation.normalise();
		verticalRotation = new Quaternion();
		verticalRotation.setFromAxisAngle( new Vector4f(1f, 0f, 0f, (float)Math.toRadians(verticalAngle) ) );
		verticalRotation.normalise();
		
		rotation = Quaternion.mul( horizontalRotation, verticalRotation, null);
		
		Matrix4f matrix = multiplyMatrixQuaternion(identity, verticalRotation);
		Matrix4f temp = multiplyMatrixQuaternion(identity, rotation);
		direction.x = -temp.m20;
		direction.y = -matrix.m21;
		direction.z = temp.m22;
		*/
	}
	
	private void updatePosition(Vector3f movement) {
		Vector3f temp = Vector3f.add(position, movement, null);
		if( true || !world.checkPlayerCollision(temp) ) {
			position = temp;
		}
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
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);
		movement.negate();

		updatePosition(movement);
    }

    public void right(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
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
		GL11.glLoadIdentity();

		//modelview = multiplyMatrixQuaternion(identity, rotation);
		//modelview.translate(position);
		
		//buffer.clear();
		//modelview.store(buffer);
		//buffer.rewind();
		//GL11.glLoadMatrix(buffer);
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
		GL11.glTranslatef(position.x, position.y, position.z);
	}
    
    public static Matrix4f multiplyMatrixQuaternion(Matrix4f m, Quaternion q) {
    	
    	q.normalise();

    	float x2 = 2.0f * q.x * q.x;
    	float y2 = 2.0f * q.y * q.y;
    	float z2 = 2.0f * q.z * q.z;
    	float xy = 2.0f * q.x * q.y;
    	float xz = 2.0f * q.x * q.z;
    	float yz = 2.0f * q.y * q.z;
    	float wx = 2.0f * q.w * q.x;
    	float wy = 2.0f * q.w * q.y;
    	float wz = 2.0f * q.w * q.z;
    	
    	m.m00 = 1-y2-z2;
    	m.m01 = xy-wz;
    	m.m02 = xz+wy;
    	m.m03 = 0;
    	m.m10 = xy+wz;
    	m.m11 = 1-x2-z2;
    	m.m12 = yz-wx;
    	m.m13 = 0;
    	m.m20 = xz-wy;
    	m.m21 = yz-wx;
    	m.m22 = 1-x2-y2;
    	m.m23 = 0;
    	m.m30 = 0;
    	m.m31 = 0;
    	m.m32 = 0;
    	m.m33 = 1;
    	
    	return m;
    }

}

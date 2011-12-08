package org.voxeltech.graphics;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

public enum Frustum {
	INSTANCE;
	
	public static final int NEAR = 5;
	public static final int FAR = 4;
	public static final int RIGHT = 3;
	public static final int LEFT = 2;
	public static final int TOP = 1;
	public static final int BOTTOM = 0;
	
	public static float nearDistance = 0.1f;
	public static float farDistance = 200.0f;
	public static float fov = 65.0f;
	
	protected static float mouseSensitivity = 0.004f;
	public static Vector3f xAxis = new Vector3f(1.0f, 0, 0);
	public static Vector3f yAxis = new Vector3f(0, 1.0f, 0);
	public static Vector3f zAxis = new Vector3f(0, 0, 1.0f);
	
	public float horizontalAngle = 0.0f;
	public float verticalAngle = 0.0f;
	public Vector3f up = new Vector3f(0, 1, 0);
	public Vector3f direction = new Vector3f(0, 0, -1);
	public Vector3f position = new Vector3f(0, 0, 0);
	public Vector3f right;
	
	private float displayRatio;
	private float nearHeight;
	private float nearWidth;
	private float farHeight;
	private float farWidth;
	
	private Matrix4f projection;
	private Matrix4f modelview;
	
	private Quaternion rotation;
	
	private FloatBuffer buffer;
	
	private Plane[] planes = new Plane[6];
	
	private Frustum() {
		for(int i = 0; i < 6; i++) {
			planes[i] = new Plane();
		}
		buffer = BufferUtils.createFloatBuffer(16);
		projection = new Matrix4f();
		modelview = new Matrix4f();
	}
	
	/*
	 * Frustum calculation algorithm, based on 
	 * http://www.racer.nl/reference/vfc_markmorley.htm
	 * 
	 */
	public void calculateFrustum() {
		buffer.clear();
		
		displayRatio = (float)( Display.getWidth() / Display.getHeight() );
		
		nearHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * nearDistance);
		nearWidth = nearHeight * displayRatio;
		
		farHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * farDistance);
		farWidth = farHeight * displayRatio;
		
		calculateRight();
		
		Vector3f planeNormal;
		Vector3f planePoint;

		Vector3f farCenter, nearCenter, X, Y, Z;

		Z = Vector3f.sub(position, direction, null);
		Z.normalise();

		X = Vector3f.cross(up, Z, null);
		X.normalise();

		Y = Vector3f.cross(Z, X, null);

		buffer.rewind();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, buffer);
		projection.load(buffer);
		//projection.rotate(horizontalAngle, new Vector3f(0, 1.0f, 0) );
		//projection.rotate(verticalAngle, new Vector3f(1.0f, 0, 0) );
		//projection.translate(position.negate(null));
		
		buffer.clear();
		
		buffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
		modelview.load(buffer);
		//modelview.rotate(horizontalAngle, new Vector3f(0, 1.0f, 0) );
		//modelview.rotate(verticalAngle, new Vector3f(1.0f, 0, 0) );
		//modelview.translate(position.negate(null));
		
		
		Matrix4f clip = new Matrix4f();
		Matrix4f.mul(modelview, projection, clip);
		
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
			if( distance <= -Voxel.RAIDUS ) {
				return false;
			}
		}
		
		return true;
	}
	
	private void calculateRight() {
		right = Vector3f.cross(direction, yAxis, null);
	}
	
	private Vector3f getVerticalAxis() {
		Vector3f temp = new Vector3f(position.x, 1.0f, position.z);
		temp.normalise();
		return temp;
	}
	
	private Vector3f getHorizontalAxis() {
		Vector3f temp = new Vector3f(1.0f, position.y, position.z);
		temp.normalise();
		return temp;
	}
	
	public void rotate(float dx, float dy) {
		reset();
		
		horizontalAngle += dx * mouseSensitivity;
		verticalAngle -= dy * mouseSensitivity;
		
		direction.x = -1 * (float)Math.sin( Math.toRadians(horizontalAngle) );
		direction.z = (float)Math.cos( Math.toRadians(horizontalAngle) );
		direction.y = (float)Math.sin( Math.toRadians(verticalAngle) );
		calculateRight();
		
		Quaternion horizontalRotation, verticalRotation;
		
		horizontalRotation = new Quaternion(yAxis.x, yAxis.y, yAxis.z, horizontalAngle);
		verticalRotation = new Quaternion(xAxis.x, xAxis.y, xAxis.z, verticalAngle);
		
		rotation = Quaternion.mul(rotation, horizontalRotation, null);
		rotation = Quaternion.mul(rotation, verticalRotation, null);
	}
	
	public void rotateHorizontal(float dx) {
		modelview.translate(position.negate(null));
		modelview.rotate( (float)Math.toRadians( dx * mouseSensitivity ), yAxis );
		modelview.translate(position);
		direction.x = -1 * (float)Math.sin( Math.toRadians(horizontalAngle) );
		direction.z = (float)Math.cos( Math.toRadians(horizontalAngle) );
	}
	
	public void rotateVertical(float dy) {
		modelview.translate(position.negate(null));
		modelview.rotate( (float)Math.toRadians( -dy * mouseSensitivity ), xAxis );
		modelview.translate(position);
		direction.y = (float)Math.sin( Math.toRadians(verticalAngle) );
	}
	
	public void forward(float distance) {
		Vector3f movement = new Vector3f(direction);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }

    public void backwards(float distance) {
		Vector3f movement = new Vector3f(direction);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }

    public void left(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }

    public void right(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }
    
    public void up(float distance) {
		Vector3f movement = new Vector3f(up);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }

    public void down(float distance) {
		Vector3f movement = new Vector3f(up);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		//modelview.translate(movement);
    }
    
    public void reset() {
    	rotation = new Quaternion(1, 0, 0, 0);
    	modelview.setIdentity();
    	/*
    	buffer.clear();
		buffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
		modelview.load(buffer);
		*/
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
	
	public void update() {
		
		modelview = multiplyMatrixQuaternion(modelview, rotation);
		modelview.translate(position);
		
		buffer.clear();
		modelview.store(buffer);
		buffer.rewind();
		GL11.glLoadMatrix(buffer);
		
		/*
		//GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glLoadIdentity();
		
		//Rotate around the X axis
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        //Rotate around the Y axis
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
        //Translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
        
        //calculateFrustum();
        
        */
		
	}

}

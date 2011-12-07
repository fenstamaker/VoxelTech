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
	
	protected static float mouseSensitivity = 0.04f;
	
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

		nearCenter = new Vector3f(direction);
		nearCenter.scale(nearDistance);
		nearCenter = Vector3f.add(nearCenter, position, null);

		farCenter = new Vector3f(direction);
		farCenter.scale(farDistance);
		farCenter = Vector3f.add(farCenter, position, null);

		planes[NEAR] = new Plane( (Vector3f)Z.negate(), nearCenter);
		planes[FAR] = new Plane(Z, farCenter);

		// Creating the top plane
		planeNormal = new Vector3f(Y);
		planeNormal.scale(nearHeight);
		planeNormal = Vector3f.add(nearCenter, planeNormal, null);
		planeNormal = Vector3f.sub(planeNormal, position, null);
		planeNormal.normalise();
		planeNormal = Vector3f.cross(planeNormal, X, null);

		planePoint = new Vector3f(Y);
		planePoint.scale(nearHeight);
		planePoint = Vector3f.add(nearCenter, planePoint, null);
		planes[TOP] = new Plane(planeNormal, planePoint);

		// Creating the bottom plane
		planeNormal = new Vector3f(Y);
		planeNormal.scale(nearHeight);
		planeNormal = Vector3f.sub(nearCenter, planeNormal, null);
		planeNormal = Vector3f.sub(planeNormal, position, null);
		planeNormal.normalise();
		planeNormal = Vector3f.cross(X, planeNormal, null);

		planePoint = new Vector3f(Y);
		planePoint.scale(nearHeight);
		planePoint = Vector3f.sub(nearCenter, planePoint, null);
		planes[BOTTOM] = new Plane(planeNormal, planePoint);

		// Creating the left plane
		planeNormal = new Vector3f(X);
		planeNormal.scale(nearWidth);
		planeNormal = Vector3f.sub(nearCenter, planeNormal, null);
		planeNormal = Vector3f.sub(planeNormal, position, null);
		planeNormal.normalise();
		planeNormal = Vector3f.cross(planeNormal, Y, null);

		planePoint = new Vector3f(X);
		planePoint.scale(nearWidth);
		planePoint = Vector3f.sub(nearCenter, planePoint, null);
		planes[LEFT] = new Plane(planeNormal, planePoint);

		// Creating the right plane
		planeNormal = new Vector3f(X);
		planeNormal.scale(nearWidth);
		planeNormal = Vector3f.add(nearCenter, planeNormal, null);
		planeNormal = Vector3f.sub(planeNormal, position, null);
		planeNormal.normalise();
		planeNormal = Vector3f.cross(Y, planeNormal, null);

		planePoint = new Vector3f(X);
		planePoint.scale(nearWidth);
		planePoint = Vector3f.add(nearCenter, planePoint, null);
		planes[RIGHT] = new Plane(planeNormal, planePoint);
		
		/*
		buffer.rewind();
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, buffer);
		projection.load(buffer);
		
		buffer.clear();
		
		buffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
		modelview.load(buffer);
		
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
		*/
	}
	
	public boolean isInFrustum(Voxel voxel) {
		Vector3f center = new Vector3f(voxel.actualPosition[0], voxel.actualPosition[1], voxel.actualPosition[2]);
		
		float distance;
		for(int i = 0; i < 6; i++) {
			distance = planes[i].getDistance(center);
			if( distance <= -Voxel.RAIDUS ) {
				return false;
			}
		}
		
		return true;
	}
	
	private void calculateRight() {
		direction.normalise();
		right = Vector3f.cross(direction, up, null);
	}
	
	public void rotateHorizontal(float dx) {
		horizontalAngle += dx * mouseSensitivity;
		direction.x = -1 * (float)Math.sin( Math.toRadians(horizontalAngle) );
		direction.z = (float)Math.cos( Math.toRadians(horizontalAngle) );
	}
	
	public void rotateVertical(float dy) {
		verticalAngle -= dy * mouseSensitivity;
		direction.y = (float)Math.sin( Math.toRadians(verticalAngle) );
	}
	
	public void forward(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(direction);
		movement.scale(distance);
		
		position = Vector3f.add(position, movement, null);
    }

    public void backwards(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(direction);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
    }

    public void left(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
    }

    public void right(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
    }
    
    public void up(float distance) {
    	position.y -= distance;
    }

    public void down(float distance) {
    	position.y += distance;
    }
    
    public void reset() {
    	horizontalAngle = 0;
    	verticalAngle = 0;
    	position = new Vector3f(0,0,0);
    }
	
	public void update() {
		
		//GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		GL11.glLoadIdentity();
		
		//Rotate around the X axis
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        //Rotate around the Y axis
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
        //Translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
        
        calculateFrustum();
        
        //
		
	}

}

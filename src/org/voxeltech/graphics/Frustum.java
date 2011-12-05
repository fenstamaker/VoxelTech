package org.voxeltech.graphics;

import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

public enum Frustum {
	INSTANCE;
	
	public static final int NEAR = 0;
	public static final int FAR = 1;
	public static final int RIGHT = 2;
	public static final int LEFT = 3;
	public static final int TOP = 4;
	public static final int BOTTOM = 5;
	
	public static float nearDistance = 1.0f;
	public static float farDistance = 100.0f;
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
	
	private Plane[] planes = new Plane[6];
	
	private Frustum() {
		
	}
	
	public void calculateFrustum() {
		displayRatio = (float)( Display.getWidth() / Display.getHeight() );
		
		nearHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * nearDistance);
		nearWidth = nearHeight * displayRatio;
		
		farHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * farDistance);
		farWidth = farHeight * displayRatio;
		
		calculateRight();
		
		Vector3f planeNormal;
		Vector3f planePoint;
	
		// Creating the nearPlane
		planeNormal = new Vector3f(direction);
		planePoint = new Vector3f(direction);
		planePoint.scale(nearDistance);
		planePoint = Vector3f.add(position, planePoint, null);
		planes[NEAR] = new Plane(planeNormal, planePoint);
	
		// Creating the farPlane
		planeNormal = new Vector3f(direction);
		planeNormal.negate();
		planePoint = new Vector3f(direction);
		planePoint.scale(farDistance);
		planePoint = Vector3f.add(position, planePoint, null);
		planes[FAR] = new Plane(planeNormal, planePoint);
		
		// Creating the rightPlane
		planeNormal = new Vector3f(right);
		planeNormal.negate();
		planePoint = new Vector3f(position);
		planes[RIGHT] = new Plane(planeNormal, planePoint);
	
		// Creating the leftPlane
		planeNormal = new Vector3f(right);
		planePoint = new Vector3f(position);
		planes[LEFT] = new Plane(planeNormal, planePoint);
		
		// Creating the topPlane
		planeNormal = new Vector3f(up);
		planeNormal.negate();
		planePoint = new Vector3f(position);
		planes[TOP] = new Plane(planeNormal, planePoint);
	
		// Creating the bottomPlane
		planeNormal = new Vector3f(up);
		planePoint = new Vector3f(position);
		planes[BOTTOM] = new Plane(planeNormal, planePoint);
	}
	
	public boolean isInFrustum(Voxel voxel) {
		float radius = (float)( Math.sqrt(2.0) * Voxel.SIZE );
		Vector3f center = new Vector3f(voxel.actualPosition[0], voxel.actualPosition[1], voxel.actualPosition[2]);
		center = Vector3f.add( center, new Vector3f(radius, radius, radius), null );
		
		float distance;
		for(int i = 0; i < planes.length; i++) {
			distance = planes[i].getDistance(center);
			if( distance < -radius ) {
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
	
	public void update() {
		calculateFrustum();
		
		GL11.glLoadIdentity();

		//Rotate around the X axis
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        //Rotate around the Y axis
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
        //Translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
		
	}

}

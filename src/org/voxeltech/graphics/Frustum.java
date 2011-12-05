package org.voxeltech.graphics;

import java.util.HashMap;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

public class Frustum {
	
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
	
	private Vector3f farPlaneCenter;
	private Vector3f farPlaneTopLeft;
	private Vector3f farPlaneTopRight;
	private Vector3f farPlaneBottomLeft;
	private Vector3f farPlaneBottomRight;
	
	private Vector3f nearPlaneCenter;
	private Vector3f nearPlaneTopLeft;
	private Vector3f nearPlaneTopRight;
	private Vector3f nearPlaneBottomLeft;
	private Vector3f nearPlaneBottomRight;
	
	
	public Frustum() {
		calculateFrustum();
	}
	
	private void calculateFrustum() {
		displayRatio = (float)( Display.getWidth() / Display.getHeight() );
		
		nearHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * nearDistance);
		nearWidth = nearHeight * displayRatio;
		
		farHeight = (float)(2.0 * Math.tan( Math.toRadians(fov/2.0) ) * farDistance);
		farWidth = farHeight * displayRatio;
		
		calculateRight();
		
		Vector3f temp1;
		Vector3f temp2;
		
		temp1 = new Vector3f(up);
		temp1.scale(farHeight/2.0f);
		temp2 = new Vector3f(right);
		temp2.scale(farWidth/2.0f);
		
		farPlaneCenter = Vector3f.add(position, direction, null);
		farPlaneCenter.scale(farDistance);
		
		farPlaneTopLeft = Vector3f.add(farPlaneCenter, temp1, null);
		farPlaneTopLeft = Vector3f.sub(farPlaneTopLeft, temp2, null);
		
		farPlaneTopRight = Vector3f.add(farPlaneCenter, temp1, null);
		farPlaneTopRight = Vector3f.add(farPlaneTopRight, temp2, null);
		
		farPlaneBottomLeft = Vector3f.sub(farPlaneCenter, temp1, null);
		farPlaneBottomLeft = Vector3f.sub(farPlaneBottomLeft, temp2, null);

		farPlaneBottomRight = Vector3f.sub(farPlaneCenter, temp1, null);
		farPlaneBottomRight = Vector3f.add(farPlaneBottomRight, temp2, null);
		
		temp1 = new Vector3f(up);
		temp1.scale(farHeight/2.0f);
		temp2 = new Vector3f(right);
		temp2.scale(farWidth/2.0f);
		
		nearPlaneCenter = Vector3f.add(position, direction, null);
		nearPlaneCenter.scale(nearDistance);
		
		nearPlaneTopLeft = Vector3f.add(nearPlaneCenter, temp1, null);
		nearPlaneTopLeft = Vector3f.sub(nearPlaneTopLeft, temp2, null);
		
		nearPlaneTopRight = Vector3f.add(nearPlaneCenter, temp1, null);
		nearPlaneTopRight = Vector3f.add(nearPlaneTopRight, temp2, null);
		
		nearPlaneBottomLeft = Vector3f.sub(nearPlaneCenter, temp1, null);
		nearPlaneBottomLeft = Vector3f.sub(nearPlaneBottomLeft, temp2, null);

		nearPlaneBottomRight = Vector3f.sub(nearPlaneCenter, temp1, null);
		nearPlaneBottomRight = Vector3f.add(nearPlaneBottomRight, temp2, null);
	
		
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
		direction.normalise();
		right = Vector3f.cross(direction, up, null);
		
		GL11.glLoadIdentity();

		//Rotate around the X axis
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        //Rotate around the Y axis
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
        //Translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
		
	}

}

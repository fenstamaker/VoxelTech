package org.voxeltech.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Gary Fenstamaker
 *
 * <em>CameraController</em> provides several methods to 
 * move the camera angle and position, abstracting the 
 * OpenGL away.
 * 
 */
public class CameraController {
	
	/**
	 * Holds the position of the camera
	 */
	public Vector3f position;
	public Vector3f lookAtLocation;
	
	/** <em>yaw</em> is the left and right angle movement of the camera */
	private float yaw;
	/** <em>pitch</em> is the up and down angle movement of the camera */
	private float pitch;
	
	/** Constructor for <em>CameraController</em> */
	public CameraController() {
		
	}
	
	/** 
	 * Constructor for <em>CameraController</em>
	 * 
	 * Sets initial x, y, and z position of camera
	 */
	public CameraController(float x, float y, float z) {
		position = new Vector3f(x, y, z);
    }
	
	public void setPosition(float x, float y, float z) {
		position = new Vector3f(x, y, z);
    }

    public void yaw(float amount) {
    	yaw += amount * Frustum.mouseSensitivity;
    }

    public void pitch(float amount) {
    	pitch += amount * Frustum.mouseSensitivity;
    }

    public void forward(float distance) {
    	position.x -= distance * (float)Math.sin(Math.toRadians(yaw));
		position.z += distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void backwards(float distance) {
    	position.x += distance * (float)Math.sin(Math.toRadians(yaw));
    	position.z -= distance * (float)Math.cos(Math.toRadians(yaw));
    }

    public void left(float distance) {
    	position.x -= distance * (float)Math.sin(Math.toRadians(yaw-90));
    	position.z += distance * (float)Math.cos(Math.toRadians(yaw-90));
    }

    public void right(float distance) {
    	position.x -= distance * (float)Math.sin(Math.toRadians(yaw+90));
    	position.z += distance * (float)Math.cos(Math.toRadians(yaw+90));
    }

    public void up(float distance) {
    	position.y -= distance;
    }

    public void down(float distance) {
    	position.y += distance;
    }

    public void update() {
    	
        //Rotate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Rotate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
    }
	
}

package org.voxeltech.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

public class CameraController {
	
	public Vector3f position;
	
	private float yaw;
	private float pitch;
	
	public CameraController(float x, float y, float z) {
		position = new Vector3f(x, y, z);
    }

    public void yaw(float amount) {
    	yaw += amount;
    }

    public void pitch(float amount) {
    	pitch += amount;
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
    	position.z += distance * (float)Math.sin(Math.toRadians(pitch));
    	position.y -= distance * (float)Math.cos(Math.toRadians(pitch));
    }

    public void down(float distance) {
    	position.z -= distance * (float)Math.sin(Math.toRadians(pitch));
    	position.y += distance * (float)Math.cos(Math.toRadians(pitch));
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

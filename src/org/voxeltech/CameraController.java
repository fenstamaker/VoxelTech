package org.voxeltech;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Gary Fenstamaker
 */
public class CameraController {
    private Vector3f position = null;
    private float yaw = 0.0f;
    private float pitch = 0.0f;

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
        //roatate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
    }

    public float[] getPosition() {
	return new float[] { position.x, position.y, position.z };
    }
}

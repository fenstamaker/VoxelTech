package org.voxeltech.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.*;

public class Frustrum {
	
	protected static float mouseSensitivity = 0.04f;
	
	public float horizontalAngle = 0.0f;
	public float verticalAngle = 0.0f;
	public Vector3f up = new Vector3f(0, 1, 0);
	public Vector3f direction = new Vector3f(0, 0, -1);
	public Vector3f position = new Vector3f(0, 0, 0);
	public Vector3f right;
	
	
	public Frustrum() {
		Vector3f.cross(direction, up, right);		
	}
	
	public void rotateHorizontal(float dx) {
		horizontalAngle += dx * mouseSensitivity;
		direction.x = (float)Math.sin( Math.toRadians(horizontalAngle) );
		direction.z = -1 * (float)Math.cos( Math.toRadians(horizontalAngle) );
	}
	
	public void rotateVertical(float dy) {
		verticalAngle -= dy * mouseSensitivity;
		direction.y = -1 * (float)Math.sin( Math.toRadians(verticalAngle) );
	}
	
	public void forward(float distance) {
		
		direction.normalise();
		Vector3f tempVector = new Vector3f(direction);
		tempVector.negate();
		tempVector.scale(distance);
		
		position = Vector3f.add(position, tempVector, null);
		
    }

    public void backwards(float distance) {
    	
    	direction.normalise();
		Vector3f tempVector = new Vector3f(direction);
		tempVector.scale(distance);

		position = Vector3f.add(position, tempVector, null);

    }

    public void left(float distance) {
    	
    	direction.normalise();
		Vector3f tempVector = new Vector3f(direction);
		tempVector.negate();
		tempVector = Vector3f.cross(tempVector, up, null);
		tempVector.scale(distance);
		tempVector.negate();

		position = Vector3f.add(position, tempVector, null);
    	
    }

    public void right(float distance) {

    	direction.normalise();
		Vector3f tempVector = new Vector3f(direction);
		tempVector.negate();
		tempVector = Vector3f.cross(tempVector, up, null);
		tempVector.scale(distance);

		position = Vector3f.add(position, tempVector, null);
    	
    }
    
    public void up(float distance) {
    	position.y -= distance;
    }

    public void down(float distance) {
    	position.y += distance;
    }	
	
	public void update() {
		direction.normalise();
		
		GL11.glLoadIdentity();

		//Rotate around the X axis
        GL11.glRotatef(verticalAngle, 1.0f, 0.0f, 0.0f);
        //Rotate around the Y axis
        GL11.glRotatef(horizontalAngle, 0.0f, 1.0f, 0.0f);
        //Translate to the position vector's location
        GL11.glTranslatef(position.x, position.y, position.z);
		
	}

}

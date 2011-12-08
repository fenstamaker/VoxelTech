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
		
		Matrix4f identity0 = new Matrix4f();
		identity0.setIdentity();
		Matrix4f identity1 = new Matrix4f(identity0);
		
		identity0.rotate( (float)Math.toRadians( horizontalAngle ), yAxis );
		identity1.rotate( (float)Math.toRadians( verticalAngle ), xAxis );
		
		identity0 = Matrix4f.mul(identity0, identity1, null);
		modelview.translate(position.negate(null));
		modelview = Matrix4f.mul(modelview, identity0, null);
		
		Vector3f frustumUp = Vector3f.cross(direction, right, null);
		float fixAngle = Vector3f.angle(up, frustumUp);
		//modelview.rotate(fixAngle, direction);
		
		modelview.translate(position);
		
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
		modelview.translate(movement);
    }

    public void backwards(float distance) {
		Vector3f movement = new Vector3f(direction);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		modelview.translate(movement);
    }

    public void left(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		modelview.translate(movement);
    }

    public void right(float distance) {
		calculateRight();
		Vector3f movement = new Vector3f(right);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
		modelview.translate(movement);
    }
    
    public void up(float distance) {
		Vector3f movement = new Vector3f(up);
		movement.scale(distance);

		position = Vector3f.add(position, movement, null);
		modelview.translate(movement);
    }

    public void down(float distance) {
		Vector3f movement = new Vector3f(up);
		movement.scale(distance);
		movement.negate();

		position = Vector3f.add(position, movement, null);
		modelview.translate(movement);
    }
    
    public void reset() {
    	horizontalAngle = 0;
    	verticalAngle = 0;
		buffer.clear();
			
		buffer.rewind();
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, buffer);
		modelview.load(buffer);
    }
	
	public void update() {
		
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

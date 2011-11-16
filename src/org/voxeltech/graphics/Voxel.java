package org.voxeltech.graphics;

import java.io.*;
import java.util.ArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.Renderable;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Voxel implements Renderable, Serializable {
	
	private static final long serialVersionUID = 1L;

	public final transient static float SIZE = 0.5f;
	
	//public transient Mesh mesh;
	
	public Vector3f position;
    protected transient Vector3f lastPosition;
    public int[] coordinates;
	
	protected Vector3f velocity;
	protected float acceleration;

    protected Color color;
    protected boolean shouldRender = true;
    protected transient static Texture image;
    
    public static void setTexture(String texturePath) {
    	// Load Texture
    	try {
    	    image = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(texturePath));
    	} catch(IOException e) {
    	    e.printStackTrace();
    	}
    }
    
    public Voxel() {
    	
    }
    
    public Voxel(float x, float y, float z) {
		position = new Vector3f(x, y, z);
		
		//mesh = new Mesh(position, SIZE);
	
		color = new Color(0f, .8f, .1f);
    }
    
    public void setCoordinates(int x, int y, int z) {
    	coordinates = new int[] { x, y, z };
    }
	
	public void setColor(float r, float g, float b) {
        color = new Color(r, g, b);
    }
    
    public void turnOn() {
    	shouldRender = true;
    }

    public void turnOff() {
    	shouldRender = false;
    }

    public void toggleOnOff() {
		if(shouldRender) shouldRender = false;
		else shouldRender = true;
    }

    public boolean shouldRender() { return shouldRender; }
    
    public void render() {}
    
    /*
    public void render() {
    	if(shouldRender) {
	    	GL11.glPushMatrix();
	            
	    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getTextureID());
	    	GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);
	    	GL11.glEnable(GL11.GL_COLOR_MATERIAL);
	        GL11.glColor4f(color.r, color.g, color.b, 1.0f);
		
	        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
	        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
	        mesh.normalBuffer.rewind();
	        mesh.texBuffer.rewind();
	        mesh.vertexBuffer.rewind();
	        GL11.glNormalPointer(0, mesh.normalBuffer);
	        GL11.glTexCoordPointer(2, 0, mesh.texBuffer);
	        GL11.glVertexPointer(3, 0, mesh.vertexBuffer);
	        GL11.glDrawArrays(GL11.GL_QUADS, 0, 24);
	        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
	        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
	            
	    	GL11.glPopMatrix();
    	}
    }*/

}

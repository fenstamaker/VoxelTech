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

public class Voxel implements Renderable {
	
	public Mesh mesh;
	
	protected Vector3f position;
    protected Vector3f lastPosition;
    protected float size;
	
	protected Vector3f velocity;
	protected float acceleration;

    protected Color color;
    protected boolean shouldRender = true;
    protected static Texture image;
    
    public boolean hasCollidedWith(Voxel obj) {

		for( ArrayList<Float> pt : mesh.vertices.get(2) ) {
		    if( pt.get(0) > obj.mesh.vertices.get(0).get(0).get(0) && pt.get(0) < obj.mesh.vertices.get(0).get(1).get(0) ) {
				if(pt.get(1) > obj.mesh.vertices.get(4).get(0).get(1) && pt.get(1) < obj.mesh.vertices.get(4).get(1).get(1) ) {
					if ( pt.get(2) > obj.mesh.vertices.get(2).get(0).get(2) && pt.get(2) < obj.mesh.vertices.get(2).get(1).get(2) ) {
						return true;
				    }
				}
		    }
		}
	
		for( ArrayList<Float> pt : mesh.vertices.get(3) ) {
		    if( pt.get(0) > obj.mesh.vertices.get(0).get(0).get(0) && pt.get(0) < obj.mesh.vertices.get(0).get(1).get(0) ) {
				if( pt.get(1) > obj.mesh.vertices.get(4).get(0).get(1) && pt.get(1) < obj.mesh.vertices.get(4).get(1).get(1) ) {
				    if ( pt.get(2) > obj.mesh.vertices.get(2).get(0).get(2) && pt.get(2) < obj.mesh.vertices.get(2).get(1).get(2) ) {
				    	return true;
				    }
				}
		    }
		}
		
		return false;
		
    }
    
	public void move(float distance) {
		
		Vector3f m = new Vector3f(velocity);
		m.scale(distance*acceleration);
		lastPosition = position;
		position = Vector3f.add(position, m, null);
		mesh.changePosition(position, size);
		
	}
	
	public void setColor(float r, float g, float b) {
        color = new Color(r, g, b);
    }
    
    public static void setTexture(String texturePath) {
    	// Load Texture
    	try {
    	    image = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(texturePath));
    	} catch(IOException e) {
    	    e.printStackTrace();
    	}
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
    
    public void render() {
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

}

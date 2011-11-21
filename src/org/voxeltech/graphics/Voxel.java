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

public class Voxel implements Serializable {

	private static final long serialVersionUID = 1L;
	public final transient static float SIZE = 0.5f;
	public transient static Texture image;
	
	public int[] position;
	public float[] actualPosition;
	public float[] color;
    public boolean shouldRender = true;
    
    public Voxel(int x, int y, int z) {
		position = new int[] {x, y, z};
	
		color = new float[] {0f, .8f, .1f};
    }
    
    public void setActualPosition(float x, float y, float z) {
    	actualPosition = new float[] {x, y, z};
    }
	
	public void setColor(float r, float g, float b) {
        color[0] = r;
        color[1] = g;
        color[2] = b;
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
    
    public static void setTexture(String texturePath) {
    	// Load Texture
    	try {
    	    image = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(texturePath));
    	} catch(IOException e) {
    	    e.printStackTrace();
    	}
    }

}

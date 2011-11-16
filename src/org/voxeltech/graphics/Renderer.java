package org.voxeltech.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import org.voxeltech.game.*;

public class Renderer {

	private ArrayList<Voxel> objects = new ArrayList<Voxel>();
	private ArrayList<WorldChunk> chunks = new ArrayList<WorldChunk>();
	
	private Renderer() {
		
	}
	
    public void setupRenderer(int displayWidth, int displayHeight) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glViewport(0, 0, displayWidth, displayHeight);
		
		// Makes blocks "solid" looking
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(65.0f, ((float)displayWidth/(float)displayHeight), 1.0f, 100.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Make background white
		GL11.glLoadIdentity();
	    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	    
		 // Set up overall ambient light
		FloatBuffer lightOverallAmbient = BufferUtils.createFloatBuffer(4);
		lightOverallAmbient.put(new float[] {5.5f, 5.5f, 5.5f, 1.0f} );
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)lightOverallAmbient.flip());
    }

    public void addObject(Voxel obj) {
    	objects.add(obj);
    }

    public void addAllObjects(Voxel[] objs) {
    	objects.addAll(Arrays.asList(objs));
    }
    
    public void addChunk(WorldChunk _chunk) {
    	chunks.add(_chunk);
    }
    
    public void addChunks(ArrayList<WorldChunk> _chunks) {
    	chunks.addAll(_chunks);
    }
    
    public void clearChunks() {
    	chunks.clear();
    }

    public void render() {
		for(int i = 0; i < objects.size(); i++) {
		    if(objects.get(i).shouldRender()) {
		    	objects.get(i).render();
		    }
		}
		for(int i = 0; i < chunks.size(); i++) {
			chunks.get(i).applyAnimation();
			chunks.get(i).render();
		}
    }
    
    public static Renderer getInstance() {
		return RendererHolder.INSTANCE;
	}
	
	private static class RendererHolder {
		private static final Renderer INSTANCE = new Renderer();
	}

}

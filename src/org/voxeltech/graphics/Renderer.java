package org.voxeltech.graphics;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

import org.voxeltech.game.*;

public class Renderer {

	private static ArrayList<Voxel> objects = new ArrayList<Voxel>();
	private static ArrayList<WorldChunk> chunks = new ArrayList<WorldChunk>();
	private static GLContext context;
	
	public Renderer() {
		
	}
	
	public static void setup() {
		context = new GLContext();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		// Makes blocks "solid" looking
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
		
		//GL11.glCullFace(GL11.GL_BACK);
		//GL11.glEnable(GL11.GL_CULL_FACE);
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(Frustum.fov, (float)( Display.getWidth() / Display.getHeight() ), Frustum.nearDistance, Frustum.farDistance);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Make background white
		GL11.glLoadIdentity();
	    GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
	    
		 // Set up overall ambient light
		FloatBuffer lightOverallAmbient = BufferUtils.createFloatBuffer(4);
		lightOverallAmbient.put(new float[] {5.5f, 5.5f, 5.5f, 1.0f} );
		GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, (FloatBuffer)lightOverallAmbient.flip());

	    Voxel.setTexture("resources/image.png");
	}

    public static void addObject(Voxel obj) {
    	objects.add(obj);
    }

    public static void addAllObjects(Voxel[] objs) {
    	objects.addAll(Arrays.asList(objs));
    }
    
    public static void addChunk(WorldChunk _chunk) {
    	chunks.add(_chunk);
    }
    
    public static void addChunks(ArrayList<WorldChunk> _chunks) {
    	chunks.addAll(_chunks);
    }
    
    public static void clearChunks() {
    	chunks.clear();
    }

    public static void render() {
		for(int i = 0; i < chunks.size(); i++) {
			//chunks.get(i).applyAnimation();
			chunks.get(i).render();
		}
    }
    
    public static void loadIdentity() {
    	try {
			Display.makeCurrent();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	    GL11.glLoadIdentity();
    }

}

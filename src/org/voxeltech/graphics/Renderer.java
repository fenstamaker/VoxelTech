package org.voxeltech.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Renderer {

	private ArrayList<Voxel> objects = new ArrayList<Voxel>();

    public Renderer(int displayWidth, int displayHeight) {
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
    }

    public void addObject(Voxel obj) {
    	objects.add(obj);
    }

    public void addAllObjects(Voxel[] objs) {
    	objects.addAll(Arrays.asList(objs));
    }

    public void render() {
		for(int i = 0; i < objects.size(); i++) {
		    if(objects.get(i).shouldRender()) {
		    	objects.get(i).render();
		    }
		}
    }

}

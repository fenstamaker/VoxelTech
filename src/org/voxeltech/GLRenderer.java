package org.voxeltech;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 *
 * @author Gary Fenstamaker
 */
public class GLRenderer {

    public GLRenderer(int displayWidth, int displayHeight) {
	// init OpenGL stuff
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
    
}

package org.voxeltech.graphics;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;

import org.voxeltech.audio.*;
import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;

public abstract class AbstractWindow {

	public int displayHeight;
	public int displayWidth;
	
	public AbstractWindow(int _displayHeight, int _displayWidth) {
		displayHeight = _displayHeight;
		displayWidth = _displayWidth;
		
		try {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}	
	}
	
	public void destroy() {
		Display.destroy();
	}
	
}

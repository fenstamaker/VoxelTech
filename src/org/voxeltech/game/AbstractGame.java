package org.voxeltech.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;

import org.voxeltech.audio.*;
import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;

public abstract class AbstractGame {
	
	protected float dx = 0f;
	protected float dy = 0f;
	
	protected float movementSpeed = 10.0f;
	
	protected ProgramClock clock;
	protected AudioController audio;
	protected Frustum camera;
	
	public AbstractGame() {	
		clock = ProgramClock.getInstance();
		audio = new AudioController();
		camera = Frustum.INSTANCE;
		camera.calculateFrustum();
	}
	
	public void start() {
		setup();
		clock.startClock();
	}

	public void destroy() {
		
	}
	
	public abstract void setup();
	public abstract void loop();

}

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
	
	protected float mouseSensitivity = 0.04f;
	protected float movementSpeed = 10.0f;
	
	protected ProgramClock clock;
	protected AudioController audio;
	protected CameraController camera;
	
	public AbstractGame() {	
		clock = ProgramClock.getInstance();
		audio = new AudioController();
		camera = new CameraController(0, 0, 0);
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

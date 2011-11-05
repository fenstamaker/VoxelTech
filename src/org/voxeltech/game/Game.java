package org.voxeltech.game;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.LWJGLException;

import org.voxeltech.audio.*;
import org.voxeltech.graphics.*;
import org.voxeltech.utils.*;

public abstract class Game {
	
	public int displayHeight;
	public int displayWidth;
	
	protected float dx = 0f;
	protected float dy = 0f;
	
	protected float mouseSensitivity = 0.04f;
	protected float movementSpeed = 10.0f;
	
	protected ProgramClock clock;
	protected AudioController audio;
	protected Renderer renderer;
	protected CameraController camera;
	
	public Game(int _displayHeight, int _displayWidth) {
		displayHeight = _displayHeight;
		displayWidth = _displayWidth;
		
		audio = new AudioController();
		renderer = new Renderer();
		camera = new CameraController();
	}

	
	protected void setupDisplay() {
		
		try {
			Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public void start() {
		setupGame();
		clock.startClock();
		gameLoop();
	}

	public void destroy() {
		Display.destroy();
	}
	
	public abstract void setupGame();
	public abstract void gameLoop();

}

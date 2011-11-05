package org.voxeltech.utils;

import org.lwjgl.Sys;
import org.lwjgl.util.Timer;

public class ProgramClock {

	private float dt = 0f;
	private float lastTime = 0f;
	private float time = 0f;
	private float startTime = 0f;
	private Timer timer = new Timer();
	
	private ProgramClock() {
		
	}
	
	public void startClock() {
		startTime = timer.getTime();
	}
	
	public void tick() {
		Timer.tick();
		if(startTime == 0f) {
			startTime = timer.getTime();
		}
		
		time = timer.getTime() - startTime;
		dt = (time - lastTime);
		lastTime = time;
	}
	
	public float getDt() {
		return dt;
	}
	
	public float getRandomBasedOnClock() {
		long r = Sys.getTime();
		float q = r / Long.MAX_VALUE;
		float n = q - ( r % Long.MAX_VALUE );
		return n;
	}
	
	public static ProgramClock getInstance() {
		return ProgramClockHolder.INSTANCE;
	}
	
	private static class ProgramClockHolder {
		private static final ProgramClock INSTANCE = new ProgramClock();
	}
	
}

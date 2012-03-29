package org.voxeltech;

import org.lwjgl.Sys;
import org.lwjgl.util.Timer;

/**
 *
 * @author Gary Fenstamaker
 */
public class AppTime {

    private float dt = 0f; // length of frame
    private float lastTime = 0f; // when last frame was
    private float time = 0f;
    private float startTime = 0f;
    private Timer timer = new Timer();
    
    private AppTime() {
	//startTime = Sys.getTime();
    }
    
    public void startOfApp() {
        startTime = timer.getTime();
    }

    public void startOfFrame() {
        Timer.tick();
        if(startTime == 0f) {
            startTime = timer.getTime();
        }
	time = timer.getTime()-startTime;
	dt = (time - lastTime);
	lastTime = time;

    }

    public float getDT() {
	return dt;
    }
    
    public float getRandomBasedOnTime() {
        long r = Sys.getTime();
        float q = r / Long.MAX_VALUE;
        float n = q - ( r % Long.MAX_VALUE );
        return n;
    }
    
    public float getTime() {
        return time;
    }

    public static AppTime getInstance() {
        return AppTimeHolder.INSTANCE;
    }

    private static class AppTimeHolder {
        private static final AppTime INSTANCE = new AppTime();
    }
 }

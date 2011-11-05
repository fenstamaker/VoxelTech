package org.voxeltech.audio;

import java.lang.Thread;

import org.voxeltech.utils.*;

public class AudioController implements Runnable {

    Thread thread;
    private ProgramClock clock = ProgramClock.getInstance();
    private int count = 0;

    public AudioController() {
    	
    }

    public void start() {
		thread = new Thread(this);
		thread.start();
    }

    public void run() {
		while(true) {
		    
		}
    }

    public void stop() {
    	thread.stop();
    }

}
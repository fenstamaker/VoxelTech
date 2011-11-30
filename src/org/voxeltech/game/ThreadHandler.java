package org.voxeltech.game;
import java.util.ArrayList;

import org.lwjgl.util.vector.*;
import org.voxeltech.game.*;

public enum ThreadHandler {
	INSTANCE;
	
	public volatile int[] position;
	public volatile ArrayList<WorldChunk> chunksToLoad = new ArrayList<WorldChunk>();
	public volatile boolean lock = false;
	public volatile boolean shouldUpdate = false;
	
	public volatile boolean[] flag = new boolean[] { false, false };
	public volatile int turn = 0;
	
	private ThreadHandler() {
		
	}
	
	public synchronized void setTurn(int i) {
		turn = i;
	}
	
	public synchronized void setFlag(int i, boolean b) {
		flag[i] = b;
	}
	
	public synchronized int getTurn() {
		return turn;
	}
	
	public synchronized boolean getFlag(int i) {
		return flag[i];
	}
	
	public synchronized boolean lock() {
		boolean rv = lock;
		lock = true;
		return rv;
	}
	
	public synchronized void releaseLock() {
		lock = false;
	}
	
	public synchronized boolean shouldUpdate() {
		return shouldUpdate;
	}
	
	public synchronized void setUpdate(boolean update) {
		shouldUpdate = update;
	}
	
	public synchronized ArrayList<WorldChunk> getChunks() {
		System.out.println("ThreadHandler: Loading Chunks into World");
		return chunksToLoad;
	}
	
	public synchronized void setChunks(ArrayList<WorldChunk> chunks) {
		System.out.println("ThreadHandler: Loading Chunks...");
		chunksToLoad = chunks;
		System.out.println("ThreadHandler: Finished Loading");
	}
	
	public synchronized int[] getPosition() {
		return position;
	}
	
	public synchronized void setPosition(int[] _position) {
		position = _position;
	}
	
}

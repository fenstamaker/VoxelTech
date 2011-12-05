package org.voxeltech.game;
import java.util.ArrayList;

import org.lwjgl.util.vector.*;
import org.voxeltech.game.*;

public class ThreadHandler {
	
	public static int[] position = new int[] { 0, 0, 0 };
	public static ArrayList<WorldChunk> chunksToLoad = new ArrayList<WorldChunk>();
	public static boolean shouldUpdate = false;
	
	public static boolean[] flag = new boolean[] { false, false };
	public static int turn = 0;
	
	private ThreadHandler() {
		
	}
	
	public synchronized static void setTurn(int i) {
		turn = i;
	}
	
	public synchronized static void setFlag(int i, boolean b) {
		flag[i] = b;
	}
	
	public synchronized static int getTurn() {
		return turn;
	}
	
	public synchronized static boolean getFlag(int i) {
		return flag[i];
	}
	
	public synchronized static boolean shouldUpdate() {
		return shouldUpdate;
	}
	
	public synchronized static void setUpdate(boolean update) {
		shouldUpdate = update;
	}
	
	public synchronized static ArrayList<WorldChunk> getChunks() {
		//System.out.println("ThreadHandler: Loading Chunks into World");
		return chunksToLoad;
	}
	
	public synchronized static void setChunks(ArrayList<WorldChunk> chunks) {
		//System.out.println("ThreadHandler: Loading Chunks...");
		chunksToLoad = chunks;
		//System.out.println("ThreadHandler: Finished Loading");
	}
	
	public synchronized static int[] getPosition() {
		return position;
	}
	
	public synchronized static void setPosition(int[] _position) {
		position = _position;
	}
	
}

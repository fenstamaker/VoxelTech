package org.voxeltech.audio;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.voxeltech.utils.*;
import org.voxeltech.noise.*;
import org.jfugue.*;

public class AudioController implements Runnable {

	public volatile boolean keepRunning = true;
    protected ProgramClock clock = ProgramClock.getInstance();
    protected int count = 0;
    public Player player = new Player();
    protected String bass;
    protected String lead;
    public static Integer bassInt = 0;
    public static Integer leadInt = 0;
    public static int duration = 1000;
    public static int noteNumber = 0;
 
    public AudioController() {
    	
    }
    
    public Integer getBass() {
    	return bassInt;
    }
    
    public Integer getLead() {
    	return leadInt;
    }
    
    protected ArrayList<String> composePattern(int minNote, int maxNote, int iterations) {
    	ArrayList<String> notes = new ArrayList<String>();
    	
    	for(int i = 0; i < 8; i++) {
    		double noise = SimplexNoise.noise(clock.getTime(), i, Math.random());
    		noise = ( ( maxNote - minNote ) * Math.abs(noise) ) + minNote;
    		int duration = (int)(4*Math.random()+1);
    		notes.add( "[" + Integer.toString( (int)noise ) + "]/" + Float.toString(1.0f/duration) );
    	}
    	
    	Collections.sort(notes);
    	
    	HashMap<String,String[]> map = new HashMap<String,String[]>();
    	
    	for(int i = 0; i < notes.size(); i++) {
    		
    		ArrayList<String> tempList = new ArrayList<String>();
        	int maxNumber = (int)(5 * Math.random() + 1);
        	for(int j = 0; j < maxNumber; j++) {
        		int randomIndex = (int)(notes.size() * Math.random());
        		tempList.add(notes.get(randomIndex));
        	}
    		map.put(notes.get(i), tempList.toArray(new String[] {}) );
    	}
    	
    	for(int i = 0; i < iterations; i++) {

        	ArrayList<String> patternList = new ArrayList<String>();
        	
    		for(int j = 0; j < notes.size(); j++) {
    			String[] mapping = map.get(notes.get(j));
    			if(mapping != null) {
	    			for(int k = 0; k < mapping.length; k++) {
	    				patternList.add(mapping[k]);
	    			}
    			} else {
    				patternList.add(notes.get(j));
    			}
    		}
    		
    		notes = patternList;
    		
    	}
    	
    	
    	
    	return notes;
    	
    }

    public void run() {
    	
    	String instrument1 = "V0 I54 R ";
    	String instrument2 = "V8 I81";
    	ArrayList<String> lead = composePattern(72, 95, 4);
    	ArrayList<String> bass = composePattern(24, 47, 4);
    	
    	String pattern1 = " ";
    	String pattern2 = " ";
    	
    	for(String note : bass) {
    		pattern1 += " [" + note + "] ";
    	}
    	for(String note : lead) {
    		pattern2 += " [" + note + "] ";
    	}
    	
    	String finalPattern = instrument1 + pattern1 + " | " + instrument2 + pattern2;
    	
    	/*
    	map.put( notes.get(0), new String[] { notes.get(0), notes.get(4), notes.get(2) } );  
    	map.put( notes.get(1), new String[] { notes.get(2), notes.get(3) } );
    	map.put( notes.get(2), new String[] { notes.get(1), notes.get(0) } );
    	map.put( notes.get(4), new String[] { notes.get(4), notes.get(1), notes.get(4) } );
    	  */ 
    	
    	player = new Player();
    	player.play(finalPattern);
    	
    	stop();
    	
    	/*
		while(true) {
		    player = new Player();
		    double sn = SimplexNoise.noise(clock.getDt(), count);
		    int note = (int)(70 * Math.abs(sn)) + 24;
		    
		    player.play("I0 [" + Integer.toString(note) + "]");
		    count++;
		    try {
				thread.sleep(2);
			} catch(InterruptedException e) { }
		}
		*/
    }

    public void stop() {
    	player.close();
    }

}
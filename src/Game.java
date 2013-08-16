import java.util.Arrays;
import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import org.voxeltech.game.*;
import org.voxeltech.graphics.*;


public class Game extends AbstractGame {
	
	public final static int turn = 1;
	public final static int opTurn = 0;
	
	public World world = World.INSTANCE;
	private Thread thread;

	private Vector3f playerLocation;
	private int[] chunkPlayerIsIn;
	
	
	public Game() {
		super();
		setPlayerLocation(0, 0, 0);
	}
	
	@Override
    public void setup() {
	    Mouse.setGrabbed(true);
    }

    @Override
    public void loop() {
	
	    clock.tick();
	    //camera.reset();
	    dx = Mouse.getDX();
	    dy = Mouse.getDY();
	
	    camera.rotate(dx, dy);
	    
	    //camera.rotateHorizontal(dx);
	    //camera.rotateVertical(dy);
	
	    float dt = clock.getDt();
	
	    
	    if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
	    	camera.forward(movementSpeed * dt);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
	    	camera.backwards(movementSpeed * dt);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
	    	camera.left(movementSpeed * dt);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
	    	camera.right(movementSpeed * dt);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
	    	camera.down(movementSpeed * dt);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_E) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
	    	camera.up(movementSpeed * dt);
	    }
	    
	    camera.update();
	    camera.calculateFrustum();
	
	    setPlayerLocation(-camera.position.x, -camera.position.y, -camera.position.z);
	    loadChunksAroundPlayer();
	    Renderer.render();
	
	    // Clear screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    }
    
    public void setPlayerLocation(float x, float y, float z) {
		playerLocation = new Vector3f(x, y, z);
    	//System.out.println(playerLocation.toString());
		int[] newChunkLocation = new int[] { ((int)(x / (WorldChunk.SIZE*Voxel.SIZE))), 
									   0, 
									   ((int)(z / (WorldChunk.SIZE*Voxel.SIZE))) };
		
		if(!Arrays.equals(chunkPlayerIsIn, newChunkLocation)) {
			chunkPlayerIsIn = newChunkLocation;
			ThreadHandler.setPosition(chunkPlayerIsIn);
		}
		
	}
    
	public void loadChunksAroundPlayer() {
		
		if( ThreadHandler.shouldUpdate  ) {
			//System.out.println("World: Chunk Update Available");
			
			ThreadHandler.setFlag(turn, true);
			ThreadHandler.setTurn(opTurn);
			
			if( !ThreadHandler.flag[opTurn] || ThreadHandler.turn != opTurn ) {
				//System.out.println("World: PROCEED");
				Renderer.clearChunks(); 
				world.clearChunks();
				
				ArrayList<WorldChunk> chunks = ThreadHandler.getChunks();
				Renderer.addChunks( chunks );
				world.addChunks( chunks );
				
				ThreadHandler.setUpdate(false);
				ThreadHandler.setFlag(turn, false);
				ThreadHandler.setTurn(opTurn);

				System.out.println("World: FINISHED");
			}
			
		}
		
	}
    
    public void destory() {
    	
    }

	public void run() {
    	super.start();
    	
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {	
			loop();
			
			Renderer.render();
		    Display.update();
		}
		
		destroy();
		Display.destroy();
	}
	    
}

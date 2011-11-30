import java.util.Arrays;

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
	
	public World world = new World();
	private ThreadHandler threadHandler;
	public Renderer renderer;
	private Thread thread;

	private Vector3f playerLocation;
	private int[] chunkPlayerIsIn;
	
	
	public Game(ThreadHandler _threadHandler) {
		super();
		threadHandler = _threadHandler;
		setPlayerLocation(0, 0, 0);
		renderer = new Renderer();
	}
	
	@Override
    public void setup() {
	    Voxel.setTexture("resources/image.png");
	    Mouse.setGrabbed(true);
    }

    @Override
    public void loop() {
	
	    clock.tick();
	    dx = Mouse.getDX();
	    dy = Mouse.getDY();
	
	    camera.yaw(dx * mouseSensitivity);
	    camera.pitch(-dy * mouseSensitivity);
	
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
	    if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
	    	camera.pitch(-dt * movementSpeed * 2.0f);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
	    	camera.pitch(dt * movementSpeed * 2.0f);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
	    	camera.yaw(dt * movementSpeed * 2.0f);
	    }
	    if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
	    	camera.yaw(-dt * movementSpeed * 2.0f);
	    }
	
	    GL11.glLoadIdentity();
	    camera.update();
	
	    setPlayerLocation(-1.0f*camera.position.x, -1.0f*camera.position.y, -1.0f*camera.position.z);
	    loadChunksAroundPlayer();
	
	    // Clear screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    }
    
    public void setPlayerLocation(float x, float y, float z) {
		playerLocation = new Vector3f(x, y, z);
		int[] newChunkLocation = new int[] { ((int)(x / (WorldChunk.SIZE*Voxel.SIZE))), 
									   ((int)(y / (WorldChunk.SIZE*Voxel.SIZE))), 
									   ((int)(z / (WorldChunk.SIZE*Voxel.SIZE))) };
		
		if(!Arrays.equals(chunkPlayerIsIn, newChunkLocation)) {
			chunkPlayerIsIn = newChunkLocation;
			threadHandler.setPosition(chunkPlayerIsIn);
		}
		
	}
    
	public void loadChunksAroundPlayer() {
		
		if( threadHandler.shouldUpdate  ) {
			System.out.println("World: Chunk Update Available");
			
			threadHandler.setFlag(turn, true);
			threadHandler.setTurn(opTurn);
			
			if( !threadHandler.flag[opTurn] && threadHandler.turn != opTurn ) {
				System.out.println("World: PROCEED");
				renderer.clearChunks(); 
				System.out.println("World.java");
				
				renderer.addChunks( threadHandler.getChunks() );
				
				threadHandler.releaseLock();
				threadHandler.setUpdate(false);

				System.out.println("World: FINISHED");
			} else {
				System.out.println(Boolean.toString(threadHandler.getFlag(opTurn)) + threadHandler.getTurn());
			}
			
		}
		
	}
    
    public void destory() {
    	world.destroy();
    }

	public void run() {
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {	
			loop();
			
			renderer.render();
		    Display.update();
		}
		
		destroy();
		Display.destroy();
	}
	    
}

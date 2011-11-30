import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.voxeltech.game.AbstractGame;
import org.voxeltech.game.World;
import org.voxeltech.graphics.Voxel;


public class Game extends AbstractGame {
	
	World world = new World();

	public Game() {
		super();
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
	
	    world.setPlayerLocation(-1.0f*camera.position.x, -1.0f*camera.position.y, -1.0f*camera.position.z);
	    world.loadChunksAroundPlayer();
	
	    // Clear screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

    }
    
    public void destory() {
    	world.destroy();
    }
	    
}

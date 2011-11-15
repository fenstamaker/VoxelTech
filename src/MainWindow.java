
import java.nio.FloatBuffer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;

import org.voxeltech.visualizer.*;
import org.voxeltech.game.*;
import org.voxeltech.graphics.*;

/**
 *
 * @author Gary Fenstamaker
 */
public class MainWindow extends Game {

	World world = new World();
	
    public MainWindow(int _displayHeight, int _displayWidth) {
		super(_displayHeight, _displayWidth);
	}

	public static void main(String[] args) {
    	Game game = new MainWindow(600, 800);
    	game.start();
    }

	@Override
	public void setupGame() {
		
		
		Voxel.setTexture("resources/image.png");
		Mouse.setGrabbed(true);
	}

	@Override
	public void gameLoop() {
		setupGame();

		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
		    
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
		     
		    world.setPlayerLocation(camera.position.x, camera.position.y, camera.position.z);
		    
		    // Clear screen and depth buffer
		    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		    renderer.render();
		    
		    Display.update();
		}
		Display.destroy();

	}

}

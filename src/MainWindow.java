
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import org.voxeltech.game.*;
import org.voxeltech.graphics.*;

/**
 *
 * @author Gary Fenstamaker
 */
public class MainWindow extends AbstractWindow {

    WorldChunkHandler chunkHandler = new WorldChunkHandler();
    ThreadHandler threadHandler = ThreadHandler.INSTANCE;
    Game game = new Game();
    
    public MainWindow(int _displayHeight, int _displayWidth) {
    	super(_displayHeight, _displayWidth);
    }

    public static void main(String[] args) {
    	MainWindow window = new MainWindow(800, 600);
    	window.startDisplayLoop();
    }
    
    public void startDisplayLoop() {
		
    	game.start();
	
		while(!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			
			game.loop();
			
			renderer.render();
		    Display.update();
		}
		
		game.destroy();
		Display.destroy();
		
    }

}

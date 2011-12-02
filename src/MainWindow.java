
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import org.voxeltech.game.*;
import org.voxeltech.graphics.*;

/**
 *
 * @author Gary Fenstamaker
 */
public class MainWindow extends AbstractWindow {
	
    public Game game;
    public WorldChunkHandler chunkHandler;
    
    public MainWindow(int _displayHeight, int _displayWidth) {
    	super(_displayHeight, _displayWidth);
    }

    public static void main(String[] args) {
    	MainWindow window = new MainWindow(600, 800);
    	window.startDisplayLoop();
    }
    
    public void startDisplayLoop() {
    	Renderer.setup();
    	game = new Game();
        chunkHandler = new WorldChunkHandler();
    	
    	Thread thread1 = new Thread(game);
    	Thread thread2 = new Thread(chunkHandler);
    	thread1.start();
    	thread2.start();
    }

}

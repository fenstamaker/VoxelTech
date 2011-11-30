
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import org.voxeltech.game.*;
import org.voxeltech.graphics.*;

/**
 *
 * @author Gary Fenstamaker
 */
public class MainWindow extends AbstractWindow {

    public ThreadHandler threadHandler = ThreadHandler.INSTANCE;
    public Game game = new Game(threadHandler);
    public WorldChunkHandler chunkHandler = new WorldChunkHandler(threadHandler);
    
    public MainWindow(int _displayHeight, int _displayWidth) {
    	super(_displayHeight, _displayWidth);
    }

    public static void main(String[] args) {
    	MainWindow window = new MainWindow(600, 800);
    	window.startDisplayLoop();
    }
    
    public void startDisplayLoop() {
    	game.start();
    	game.run();
    }

}

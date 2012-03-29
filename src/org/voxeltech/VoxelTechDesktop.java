/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.voxeltech;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * @author Gary
 */
public class VoxelTechDesktop {

    public int displayHeight = 600;
    public int displayWidth = 800;

    private float dx = 0f; // Mouse X Movement
    private float dy = 0f; // Mouse Y Movement
    
    private int oldX = 100;
    private int oldZ = 100;
    
    private final float movementSpeed = 10.0f; //move 10 units per second
    
    private ArrayList<WorldChunk> chunks = new ArrayList<WorldChunk>();
    private Frustum camera = null;
    private Player player = null;
    
    private FloatBuffer vertexBuffer;
    private ShortBuffer indexBuffer;
    private FloatBuffer texBuffer;
    private FloatBuffer normalBuffer;
    private float[] vertices;
    private short[] indices;
    private float[] textures;
    private float[] normals;
    
    public void init() {
	// init OpenGL stuff
	GL11.glViewport(0, 0, displayWidth, displayHeight);


	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GLU.gluPerspective(65.0f, ((float)displayWidth/(float)displayHeight), 1.0f, 100.0f);
	GL11.glMatrixMode(GL11.GL_MODELVIEW);
	GL11.glShadeModel(GL11.GL_FLAT);
        
	// Makes blocks "solid" looking
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//        GL11.glEnable(GL11.GL_BLEND);
	GL11.glClearDepth(1.0f); // Depth Buffer Setup
        GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
        GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Testing To Do
        
	GL11.glClearColor(1.0f, 1.0f, 1.0f, 0.0f); // Make background white
	GL11.glLoadIdentity();
        GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
        
	camera = Frustum.INSTANCE;
        camera.calculateFrustum();
        player = Player.INSTANCE;
        
        ByteBuffer vbb = ByteBuffer.allocateDirect(96);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        
        // Set up and initializes the indices buffer
        indices = Mesh.getIndices();                
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        indexBuffer = ibb.asShortBuffer();
        indexBuffer.put(indices);
        indexBuffer.position(0);
        
        textures = Mesh.getTexCoords();
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length * 4);
        tbb.order(ByteOrder.nativeOrder());
        texBuffer = tbb.asFloatBuffer();
        texBuffer.put(textures);
        texBuffer.position(0);
        
        normals = Mesh.getNormals();
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
        nbb.order(ByteOrder.nativeOrder());
        normalBuffer = nbb.asFloatBuffer();
        normalBuffer.put(normals);
        normalBuffer.position(0);
    }
    
    public void setupDisplay() {
        // Setup window and display
	try {
	    Display.setDisplayMode(new DisplayMode(displayWidth, displayHeight));
	    Display.create();
	} catch (LWJGLException e) {
	    e.printStackTrace();
	    System.exit(0);
	}
    }

    public void start() {
        setupDisplay();
	init();	
        
        Mouse.setGrabbed(true); //hide the mouse
	while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            
            dx = Mouse.getDX();
	    dy = Mouse.getDY();

	    camera.rotate(dx, dy);

	    float dt = 0.03f;

//	    if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
//		camera.forward(movementSpeed * dt);
//	    }
//	    if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
//		camera.backwards(movementSpeed * dt);
//	    }
	    if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
		camera.left(movementSpeed * dt);
                player.left(movementSpeed * dt);
	    }
	    if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
		camera.right(movementSpeed * dt);
                player.right(movementSpeed * dt);
	    }
	    if(Keyboard.isKeyDown(Keyboard.KEY_Q) || Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
		camera.down(movementSpeed * dt);
                player.down(movementSpeed * dt);
	    }
	    if(Keyboard.isKeyDown(Keyboard.KEY_E) || Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
		camera.up(movementSpeed * dt);
                player.up(movementSpeed * dt);
	    }
	    if(Keyboard.isKeyDown(Keyboard.KEY_M)) {
		if(Mouse.isGrabbed())
		    Mouse.setGrabbed(false);
		else
		    Mouse.setGrabbed(true);
	    }
	    
	    GL11.glLoadIdentity();
//            camera.lookAt(new Vector3f(0, 0, 1));
            camera.update();
	    camera.calculateFrustum();
            System.out.println("Player: " + player.position.toString());
            System.out.println("Camera: " + camera.position.toString());
	   
	    // Clear screen and depth buffer
	    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
            GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
            GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
            
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glCullFace(GL11.GL_BACK);
	    
            
            float[] position = camera.position.toArray();
            int x = (int)(position[0]/WorldChunk.CX);
            int z = (int)(position[2]/WorldChunk.CZ);
            if(x != oldX) {
                chunks.clear();
                for(int i = 0; i < 5; i++) {
                    WorldChunk c1 = new WorldChunk(new Position3f(WorldChunk.SIZE+(x+i)*WorldChunk.CX, 0, 0));
                    chunks.add(c1);          
                    c1 = new WorldChunk(new Position3f(WorldChunk.SIZE+(x+i)*WorldChunk.CX, 0, 0));
                    chunks.add(c1);
                    c1 = new WorldChunk(new Position3f(WorldChunk.SIZE+(x-i)*WorldChunk.CX, 0, 0));
                    chunks.add(c1);
                    c1 = new WorldChunk(new Position3f(WorldChunk.SIZE+(x-i)*WorldChunk.CX, 0, 0));
                    chunks.add(c1);
                }   
                oldX = x;
            }
            
            for(int i = 0; i < Model.CX; i++) {
                for(int j = 0; j < Model.CZ; j++) {
                    for(int k = 0; k < Model.CY; k++) {
                         byte block = player.model.getBlock(new Position3d(i,k,j));
                            if ( block == 1 ) {
                                float[] color = player.model.getColor(new Position3d(i,k,j));
                                setVertices(new float[] {player.position.x+i, player.position.y+k, player.position.z+j}, Model.SIZE/2.0f);
                                GL11.glColor4f(color[0], color[1], color[2], color[3]);
                                GL11.glVertexPointer(3, 0, vertexBuffer);
                                GL11.glTexCoordPointer(2, 0, texBuffer);
                                GL11.glNormalPointer(3, normalBuffer);
                                GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer);
                                vertexBuffer.clear();
                                vertexBuffer.position(0);
                            }
                    }
                }
            }
            
            for( WorldChunk chunk : chunks ) {
                for(int i = 0; i < WorldChunk.CX; i++) {
                    for(int j = 0; j < WorldChunk.CZ; j++) {
                        for(int k = 0; k < WorldChunk.CY; k++) {
                            byte block = chunk.getBlock(new Position3d(i,k,j));
                            if (
                                    block == 1 && 
                                    camera.isInFrustum(new Position3f(chunk.realPosition.x+i, chunk.realPosition.y+k, chunk.realPosition.z+j)) &&
                                    checkAround(chunk, new Position3d(i,k,j))
                                ) {
                                float[] color = chunk.getColor(new Position3d(i,k,j));
                                setVertices(new float[] {chunk.realPosition.x+i,chunk.realPosition.y+k,chunk.realPosition.z+j}, WorldChunk.SIZE/2.0f);
                                GL11.glColor4f(color[0], color[1], color[2], color[3]);
                                GL11.glVertexPointer(3, 0, vertexBuffer);
                                GL11.glTexCoordPointer(2, 0, texBuffer);
                                GL11.glNormalPointer(3, normalBuffer);
                                GL11.glDrawElements(GL11.GL_TRIANGLES, indexBuffer);
                                vertexBuffer.clear();
                                vertexBuffer.position(0);
                            }
                        }
                    }
                }
            }
            
            
	    Display.update();
	}
	Display.destroy();
    }
    
    private boolean checkAround(WorldChunk chunk, Position3d position) {
        if ( 
                (position.y+1 < WorldChunk.CY && chunk.getBlock(new Position3d(position.x+0, position.y+1, position.z+0)) == 1) && 
                (position.z-1 > 0 && chunk.getBlock(new Position3d(position.x+0, position.y+0, position.z-1)) == 1) 
           )  {
            return false;
        }
        return true;
    }
    
    private void setVertices(float[] v, float size) {
        vertices = Mesh.getVertices( v, size );
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
    }
    
    public static void main(String args[]) {
	VoxelTechDesktop window = new VoxelTechDesktop();
	window.start();
    }
}

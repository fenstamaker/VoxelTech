package org.voxeltech.game;

import java.io.*;
import java.sql.*;
import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<WorldChunk> chunks;
	private Connection connection;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private ByteArrayOutputStream bufferOut;
	private ByteArrayInputStream bufferIn;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<WorldChunk>();
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
	
	public WorldChunk generateChunk(float x, float y, float z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk generateChunk(int x, int y, int z) {
		return new WorldChunk(x, y, z);
	}
	
	public WorldChunk loadChunk(int x, int y, int z) {
		
		WorldChunk chunk = null;
		int id = x+y+z;
		
		try {
			
			connection = DriverManager.getConnection("jdbc:sqlite:world.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS chunks (id int, data BLOB)");
			
			PreparedStatement prepStatement;
			ResultSet rs = statement.executeQuery("SELECT * FROM chunks WHERE id=" + id + " LIMIT 1");
			
			if(!rs.next()) {
				chunk = generateChunk(x, y, z);
				bufferOut = new ByteArrayOutputStream();
				objectOut= new ObjectOutputStream(bufferOut);
				objectOut.writeObject(chunk);
				objectOut.close();
				
				prepStatement = connection.prepareStatement("INSERT INTO chunks VALUES (?, ?)");
				prepStatement.setInt(1, id);
				prepStatement.setBytes(2, bufferOut.toByteArray());
				prepStatement.execute();
				bufferOut.close();
			} else {
				byte[] byteBuffer = rs.getBytes("data");
				bufferIn = new ByteArrayInputStream(byteBuffer);
				objectIn = new ObjectInputStream(bufferIn);
				chunk = (WorldChunk)objectIn.readObject();
				objectIn.close();
				bufferIn.close();
			}
			
			rs.close();
			statement.close();
			connection.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chunk;
		/*
		for(WorldChunk c : chunks) {
			if(c.coordinates[0] == x && c.coordinates[1] == y && c.coordinates[2] == z)
				return c;
		}
		return generateChunk(x, y, z);
		*/
	}
	
	
	public ArrayList<WorldChunk> loadChunksAroundChunk(int x, int y, int z) {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 2; k++) {
					chunkHolder.add(loadChunk(x+i, y+j, z+k));
					chunkHolder.add(loadChunk(x+i, y+j, z-k));
					chunkHolder.add(loadChunk(x+i, y-j, z+k));
					chunkHolder.add(loadChunk(x+i, y-j, z-k));
					chunkHolder.add(loadChunk(x-i, y+j, z+k));
					chunkHolder.add(loadChunk(x-i, y+j, z-k));
					chunkHolder.add(loadChunk(x-i, y-j, z+k));
					chunkHolder.add(loadChunk(x-i, y-j, z-k));
				}
			}
		}
		
		return chunkHolder;

	}
	
}

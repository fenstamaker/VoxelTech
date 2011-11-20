package org.voxeltech.game;

import java.io.*;
import java.sql.*;
import java.lang.Thread;
import java.util.ArrayList;

public class WorldChunkHandler {

	private ArrayList<Integer[]> chunks;
	private Connection connection;

	private ObjectOutputStream objectOut;
	private ObjectInputStream objectIn;
	private ByteArrayOutputStream bufferOut;
	private ByteArrayInputStream bufferIn;
	
	public WorldChunkHandler() {
		chunks = new ArrayList<Integer[]>();
		
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
	
	public ArrayList<WorldChunk> loadChunk() {
		ArrayList<WorldChunk> chunkHolder = new ArrayList<WorldChunk>();
		ArrayList<Integer> foundChunks = new ArrayList<Integer>();
		
		try {
			
			connection = DriverManager.getConnection("jdbc:sqlite:world.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			statement.executeUpdate("DROP TABLE IF EXISTS chunks");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS chunks (name VARCHAR(15), data BLOB)");
			
			PreparedStatement prepStatement;
			
			String sql = "SELECT * FROM chunks WHERE name IN (";
			
			for(int i = 0; i < chunks.size(); i++) {
				if(i!=0)
					sql += ",";
				String nameString = chunks.get(i)[0].toString() + chunks.get(i)[1].toString() + chunks.get(i)[2].toString();
				sql += nameString;
			}
			
			sql += ")";
			
			ResultSet rs = statement.executeQuery(sql);
			
			while(rs.next()) {
				foundChunks.add(rs.getInt("id"));
				
				byte[] byteBuffer = rs.getBytes("data");
				
				bufferIn = new ByteArrayInputStream(byteBuffer);
				objectIn = new ObjectInputStream(bufferIn);
				
				chunkHolder.add( (WorldChunk)objectIn.readObject() );
				
				objectIn.close();
				bufferIn.close();
			}
			
			WorldChunk tempChunk;
			
			for(Integer[] i : chunks) {
				
				if(!foundChunks.contains(i)) {
					
					tempChunk = generateChunk(i[0], i[1], i[2]);
					chunkHolder.add(tempChunk);
					
					bufferOut = new ByteArrayOutputStream();
					objectOut= new ObjectOutputStream(bufferOut);
					objectOut.writeObject(tempChunk);
					objectOut.close();
					
					prepStatement = connection.prepareStatement("INSERT INTO chunks VALUES (?, ?)");
					String nameString = i[0].toString() + i[1].toString() + i[2].toString();
					prepStatement.setString(1, nameString);
					prepStatement.setBytes(2, bufferOut.toByteArray());
					prepStatement.execute();
					bufferOut.close();
					
				}
				
			}
			
			rs.close();
			statement.close();
			connection.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return chunkHolder;
		/*
		for(WorldChunk c : chunks) {
			if(c.coordinates[0] == x && c.coordinates[1] == y && c.coordinates[2] == z)
				return c;
		}
		return generateChunk(x, y, z);
		*/
	}
	
	
	public ArrayList<WorldChunk> loadChunksAroundChunk(int x, int y, int z) {
		
		
		for(int i = 0; i < 2; i++) {
			for(int j = 0; j < 2; j++) {
				for(int k = 0; k < 2; k++) {
					chunks.add( new Integer[] { (x+i), (y+j), (z+k) } );
					chunks.add( new Integer[] { (x+i), (y+j), (z-k) } );
					chunks.add( new Integer[] { (x+i), (y-j), (z+k) } );
					chunks.add( new Integer[] { (x+i), (y-j), (z-k) } );
					chunks.add( new Integer[] { (x-i), (y+j), (z+k) } );
					chunks.add( new Integer[] { (x-i), (y+j), (z-k) } );
					chunks.add( new Integer[] { (x-i), (y-j), (z+k) } );
					chunks.add( new Integer[] { (x-i), (y-j), (z-k) } );
				}
			}
		}
		
		return loadChunk();

	}
	
}

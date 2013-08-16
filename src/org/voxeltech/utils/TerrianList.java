package org.voxeltech.utils;

import java.util.ArrayList;

import org.voxeltech.game.WorldChunk;

public class TerrianList<E> extends ArrayList<E> {
	
	private static final long serialVersionUID = 1L;
	
	public TerrianList() {
		super();
	}
	
	public TerrianList(int size) {
		super(size);
	}

	public void add(int x, int y, int z, E element) {
		super.add(x*WorldChunk.SIZE+y*WorldChunk.SIZE+z*WorldChunk.SIZE, element);
	}
	
	public E get(int x, int y, int z) {
		return super.get(x*WorldChunk.SIZE+y*WorldChunk.SIZE+z*WorldChunk.SIZE);
	}
	
}

package org.voxeltech.utils;

public class ChunkID {
	
	public int x;
	public int y;
	public int z;
	
	public ChunkID(int _x, int _y, int _z) {
		x = _x;
		y = _y;
		z = _z;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if( obj == null ) return false;
		if( this == obj ) return true;
		if( this.getClass() != obj.getClass() ) return false;
		ChunkID other = (ChunkID)obj;
		
		if( this.x == other.x && this.y == other.y && this.z == other.z ) return true;
		
		return false;
	}

}

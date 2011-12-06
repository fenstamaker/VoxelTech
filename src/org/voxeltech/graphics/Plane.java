package org.voxeltech.graphics;

import org.lwjgl.util.vector.*;

public class Plane {

	private Vector3f normal;
	private Vector3f point;
	private float D;
	
	public Plane(Vector3f p0, Vector3f p1, Vector3f p2) {
		point = p0;
		
		Vector3f v = Vector3f.sub(p1, p0, null);
		Vector3f u = Vector3f.sub(p2, p0, null);
		
		normal = Vector3f.cross(v, u, null);
		normal.normalise();
		D = Vector3f.dot(normal, point);
	}
	
	public Plane(Vector3f _normal, Vector3f _point) {
		normal = _normal;
		point = _point;
		normal.normalise();

		D = Vector3f.dot(normal, point);
	}
	
	public float getDistance(Vector3f p0) {
		normal.normalise();
		return Vector3f.dot(normal, p0) - D;
	}
	
}

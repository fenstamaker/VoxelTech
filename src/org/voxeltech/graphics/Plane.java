package org.voxeltech.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.*;

public class Plane {

	private Vector3f normal;
	private Vector3f point;
	public float A;
	public float B;
	public float C;
	public float D;
	
	public Plane() {}
	
	public Plane(float _A, float _B, float _C, float _D) {
		A = _A;
		B = _B;
		C = _C;
		D = _D;
	}
	
	public void setPlane(float _A, float _B, float _C, float _D) {
		A = _A;
		B = _B;
		C = _C;
		D = _D;
	}
	
	public void normalize() {
		float t = (float)Math.sqrt(A*A+B*B+C*C);
		A /= t;
		B /= t;
		C /= t;
		D /= t;
	}
	
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
		return -1 * ( Vector3f.dot(normal, p0) - D );
	}
	
	public float getDistance1(Vector3f point) {
		return (A*point.x) + (B*point.y) + (C*point.z) + D;
	}
	
}

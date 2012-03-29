package org.voxeltech;

/**
 *
 * @author Static
 */
public enum Player {
    INSTANCE;
    
    public Vector3f position = new Vector3f(0.0f, 17.0f, 15.0f);
    public Model model = new Model();
    public Vector3f up = new Vector3f(0, 1, 0);
    
    private void updatePosition(Vector3f movement) {
        Vector3f temp = Vector3f.add(position, movement);
        position.set(temp.x, temp.y, temp.z);
    }
    
    public void left(float distance) {
        Vector3f movement = new Vector3f(1, 0, 0);
        movement.scale(distance);
        movement.negate();

        updatePosition(movement);
    }

    public void right(float distance) {
        Vector3f movement = new Vector3f(1, 0, 0);
        movement.scale(distance);

        updatePosition(movement);
    }
    
     public void up(float distance) {
        Vector3f movement = new Vector3f(up);
        movement.scale(distance);

        updatePosition(movement);
    }

    public void down(float distance) {
        Vector3f movement = new Vector3f(up);
        movement.scale(distance);
        movement.negate();

        updatePosition(movement);
    }
}

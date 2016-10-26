package com.wizered67.game.Enums;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Adam on 8/12/2016.
 */
public enum Direction {
    UP (0, 1, 0),
    DOWN (0, -1, Math.PI),
    LEFT (-1, 0, Math.PI / 2),
    RIGHT (1, 0, -Math.PI / 2),
    RIGHT_UP (1, 1, -Math.PI / 4),
    LEFT_UP (-1, 1, Math.PI / 4),
    RIGHT_DOWN (1, -1, -3 * Math.PI / 4),
    LEFT_DOWN (-1, -1, 3 * Math.PI / 4);

    private final Vector2 directionVector = new Vector2();
    private final double box2dDir;
    Direction(int xDir, int yDir, double box2dDir){
        directionVector.set(xDir, yDir);
        this.box2dDir = box2dDir;
    }

    public Vector2 getVector(){
        return directionVector.cpy();
    }
    public double getBox2dDir() {return box2dDir; }

    public static Direction getDirectionFromVector(Vector2 vector){
        for (Direction d : Direction.values()){
            if (d.getVector().x == vector.x && d.getVector().y == vector.y){
                return d;
            }
        }
        return UP;
    }
}

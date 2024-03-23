package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

import java.util.ConcurrentModificationException;

public class Wall extends Entity {
    private static final String wallID = "-wall-";
    private int health = 4;

    private static int wallNum;

    public Wall(double x, double y, double angle) {
        super(getSomeId(), x, y, 0);
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health--;
    }

    @Override
    public double getXBound() {
        return getX() + Constants.WALL_WIDTH;
    }

    @Override
    public double getYBound() {
        return getY() + Constants.WALL_HEIGHT;
    }

    private static String getSomeId() {
        return wallID + wallNum++;
    }

    @Override
    public void move(GameWorld gameWorld) {

    }

    @Override
    public boolean borderReached() {
        return false;
    }

    @Override
    public void checkBoundaries(GameWorld gameWorld) {
    }

}

package edu.csc413.tankgame.model;

public class PowerUp extends Entity {
    // don't think I can add powerups
    public PowerUp(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public double getXBound() {
        return getX() + 24;
    }
    @Override
    public double getYBound() {
        return getY() + 24;
    }
    @Override
    public void move(GameWorld gameWorld){}
    @Override
    public void checkBoundaries(GameWorld gameWorld){}
    @Override
    public boolean borderReached(){
        return false;
    }
}

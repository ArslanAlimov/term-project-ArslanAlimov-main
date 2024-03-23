package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

/**
 * Entity class representing all tanks in the game.
 */
public abstract class Tank extends Entity {
    private int health = 10;
    private int counter = 0;
    private double TankSpeed = Constants.TANK_MOVEMENT_SPEED;
    public Tank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }
    public void activatePwUp()
    {
    health = health+2;
    TankSpeed= TankSpeed+1.50;
    }
    public int getHealth() {
        return health;
    }

    public void decreaseHealth() {
        health--;
    }

    private double getShellX() {
        return getX() + Constants.TANK_WIDTH / 2 + 45.0 * Math.cos(getAngle()) - Constants.SHELL_WIDTH / 2;
    }

    private double getShellY() {
        return getY() + Constants.TANK_HEIGHT / 2 + 45.0 * Math.sin(getAngle()) - Constants.SHELL_HEIGHT / 2;
    }

    private double getShellAngle() {
        return getAngle();
    }

    @Override
    public double getXBound() {
        return getX() + Constants.TANK_WIDTH;
    }

    @Override
    public double getYBound() {
        return getY() + Constants.TANK_WIDTH;
    }

    @Override
    // provided by teacher.
    public void checkBoundaries(GameWorld gameWorld) {
        if (this.getX() < Constants.TANK_X_LOWER_BOUND) {
            this.setX((Constants.TANK_X_LOWER_BOUND));
        }

        if (this.getX() > Constants.TANK_X_UPPER_BOUND) {
            this.setX(Constants.TANK_X_UPPER_BOUND);
        }

        if (this.getY() < Constants.TANK_Y_LOWER_BOUND) {
            this.setY(Constants.TANK_Y_LOWER_BOUND);
        }

        if (this.getY() > Constants.TANK_Y_UPPER_BOUND) {
            this.setY(Constants.TANK_Y_UPPER_BOUND);
        }
    }

    @Override
    public boolean borderReached() {
        return false;
    }

    protected void fireShellPlayer(GameWorld gameWorld) {
        counter++;
        Shell shell = new Shell(getId(), getShellX(), getShellY(), getShellAngle());
        if (counter % 20 == 0) {
            gameWorld.createShell(shell);
        }

    }

    protected void fireShell(GameWorld gameWorld) {
        Shell shell = new Shell(getId(), getShellX(), getShellY(), getShellAngle());
        gameWorld.createShell(shell);
    }

}


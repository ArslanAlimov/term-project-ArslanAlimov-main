package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

import java.util.Random;

public class DumbAiTank extends Tank {
    Random thisRand = new Random();

    public DumbAiTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        moveForward(Constants.TANK_MOVEMENT_SPEED);
        turnRight(Constants.TANK_TURN_SPEED);
        if (thisRand.nextDouble() <= 0.03) {
            fireShell(gameWorld);

        }
    }
}

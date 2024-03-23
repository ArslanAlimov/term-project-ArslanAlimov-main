package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;
import edu.csc413.tankgame.KeyboardReader;

public class PlayerTank extends Tank {
    public PlayerTank(String id, double x, double y, double angle) {
        super(id, x, y, angle);
    }

    @Override
    public void move(GameWorld gameWorld) {
        KeyboardReader keyboardReader = KeyboardReader.instance();

        if (keyboardReader.upPressed()) {
            moveForward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboardReader.downPressed()) {
            moveBackward(Constants.TANK_MOVEMENT_SPEED);
        }
        if (keyboardReader.rightPressed()) {
            turnRight(Constants.TANK_TURN_SPEED);
        }
        if (keyboardReader.leftPressed()) {
            turnLeft(Constants.TANK_TURN_SPEED);
        }
        if (keyboardReader.spacePressed()) {
            fireShellPlayer(gameWorld);
        }
    }

}

package edu.csc413.tankgame.model;

import edu.csc413.tankgame.Constants;

public class Shell extends Entity{
    private static final String shellId = "-shell-";
    private static int shellNum;
    private static String shooter;
    public Shell(String id, double x, double y ,double angle)
    {
        super(getSomeId(),x,y,angle);
        shooter = id ;

    }
    public String getShooter(){
        return shooter;
    }
    private static String getSomeId()
    {
        return shellId + shellNum++;
    }
    private static int getShellNum()
    {
    return shellNum;
    }
    @Override
    public double getXBound(){
        return getX() + Constants.SHELL_WIDTH;
    }

    @Override
    public double getYBound(){
        return getY() + Constants.SHELL_HEIGHT;
    }
    @Override
    public boolean borderReached(){
        if(
                this.getX()<Constants.SHELL_X_LOWER_BOUND
                &this.getX()>Constants.SHELL_X_UPPER_BOUND
                &this.getX()<Constants.SHELL_Y_LOWER_BOUND
                &this.getX()>Constants.SHELL_Y_UPPER_BOUND)
        {
            return true;
        }
        return false;
    }
    @Override
    public void checkBoundaries(GameWorld gameWorld){}
    @Override
    public void move(GameWorld gameWorld)
    {
        moveForward(Constants.SHELL_MOVEMENT_SPEED);
    }
}

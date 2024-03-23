package edu.csc413.tankgame;

import edu.csc413.tankgame.model.Entity;
import edu.csc413.tankgame.model.Shell;
import edu.csc413.tankgame.model.Tank;
import edu.csc413.tankgame.model.Wall;
import edu.csc413.tankgame.view.RunGameView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collission {
/*
Colission help the methods
https://www.youtube.com/watch?v=qIr2XYZrznI&ab_channel=BroCode

https://www.youtube.com/watch?v=UMMUIY8Ap6o&ab_channel=%D0%BA%D1%83%D0%B7%D1%8C%D0%BC%D0%B0%D0%BA%D1%83%D0%B7%D0%B8%D0%BD%D0%BA%D1%83%D0%B7%D1%8C%D0%BC%D0%B0%D0%BA%D1%83%D0%B7%D0%B8%D0%BD

https://www.youtube.com/watch?v=Ig17x4XGC0w&ab_channel=%D0%A3%D0%BB%D1%8C%D1%8F%D0%BD%D0%B0%D0%95%D0%B3%D0%BE%D1%80%D0%BE%D0%B2%D0%B0%D0%A3%D0%BB%D1%8C%D1%8F%D0%BD%D0%B0%D0%95%D0%B3%D0%BE%D1%80%D0%BE%D0%B2%D0%B0
talked about plagiarism with Dawson in discord. About collision to make sure its not considered as plagiarism
*/
    //if Tank collides the wall
    // General collission we need to count distance
    // Distance 1 is always entities X bound which is different for each entity , because tank has bigger bound and shells have smaller e.t.c
    // first distance we count X bound of entity and we subtract it from entity 1
    // then we cound distance of entity1 x bound and we minus that from our entity
    //we do the same thing for Y bounds and we subtract it from entity 1 y position
    //we put everything in array of distances and then we find smallest distance and we compare that distance to all other distances to find if it collides
    // This goes for all the collissions shell at shell , tank v wall, shell at wall in this code.
    //Collission stuff
    // code below is from lecture
public static double distance1(Entity entity,Entity entity1,int i){
    if(i==1){
        return entity.getXBound() - entity1.getX();
    }
    if(i==2){
        return entity.getYBound() - entity1.getY();
    }
    if(i==3){
        return entity1.getXBound() - entity.getX();
    }
    if(i==4){
        return entity1.getYBound() - entity.getY();
    }
    return 0;
}

    public static List<Entity> collisionCheck(Entity entity, Entity entity1,RunGameView runGameView) {
        List<Entity> removedEntities = new ArrayList<>();
        if (entity instanceof Tank && entity1 instanceof Tank) {
            Collission.tankCollide(entity, entity1,runGameView);
            System.out.println("Tank" + entity.getId() + "Collided with" + entity1.getId());
        } else if (entity instanceof Tank && entity1 instanceof Wall) {
            tankWall(entity, entity1,runGameView);
            System.out.println("Tank" + entity.getId() + " Hits the wall!" + entity1.getId());
        } else if (entity instanceof Wall && entity1 instanceof Tank) {
            tankWall(entity1, entity,runGameView);
            System.out.println("Wall" + entity.getId() + "Being Hit by " + entity1.getId());
        } else if (entity instanceof Tank && entity1 instanceof Shell) {
            removedEntities.addAll(Collission.fireAtTank(entity, entity1,runGameView));
            System.out.println("Tank" + entity.getId() + " gets hit by" + entity1.getId());
        } else if (entity instanceof Shell && entity1 instanceof Tank) {
            removedEntities.addAll(Collission.fireAtTank(entity1, entity,runGameView));
            System.out.println("Shell" + entity.getId() + " Hits" + entity1.getId());
        } else if (entity instanceof Shell && entity1 instanceof Shell) {
            removedEntities.addAll(Collission.shelAtShell(entity, entity1,runGameView));
            System.out.println(entity.getId() + "Hit" + entity1.getId());
        } else if (entity instanceof Shell && entity1 instanceof Wall){
            removedEntities.addAll(Collission.shelAtWall(entity, entity1,runGameView));
            System.out.println(entity.getId() + "Hit the wall " + entity1.getId());
        } else if (entity instanceof Wall && entity1 instanceof Shell) {
            removedEntities.addAll(Collission.shelAtShell(entity1, entity,runGameView));
            System.out.println(entity.getId() + "Hit by the shell " + entity1.getId());
        }
        return removedEntities;
    }
    public static boolean areEntitiesCollide(Entity entity, Entity entity1) {
        return entity.getX() < entity1.getXBound()
                && entity.getXBound() > entity1.getX()
                && entity.getY() < entity1.getYBound()
                && entity.getYBound() > entity1.getY();
    }
    public static List<Entity> fireAtTank(Entity entity, Entity entity1,RunGameView runGameView) {
        List<Entity> removedEntities = new ArrayList<>();
        try {

            if (((Shell) entity1).getShooter().equals(entity.getId())) {
                System.out.println("Own shell was hit by" + entity.getId() + "No damage was taken");
            }
            if (!((Shell) entity1).getShooter().equals(entity.getId()) && ((Tank) entity).getHealth() != 1) {
                //The tank is taking damage
                ((Tank) entity).decreaseHealth();
                //The shells is being removed
                removedEntities.add(entity1);

                //Animation being played
                runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity.getY());
            }

            if (!((Shell) entity1).getShooter().equals(entity1.getId()) && ((Tank) entity).getHealth() == 1) {
                //Remove shell and tank
                removedEntities.add(entity);
                removedEntities.add(entity1);

                ((Tank) entity1).decreaseHealth();
                runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
                System.out.println("Sprite 1played");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return removedEntities;
    }
    public static List<Entity> shelAtWall(Entity entity, Entity entity1,RunGameView runGameView) {
        List<Entity> removedEntities = new ArrayList<>();
        removedEntities.add(entity);
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity.getX(), entity.getY());
        if(((Wall) entity1).getHealth() != 1){
            ((Wall) entity1).decreaseHealth();
        }else if(((Wall) entity1).getHealth() == 1){
            removedEntities.add(entity1);
            runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
        }
        return  removedEntities;
    }
    public static List<Entity> shelAtShell(Entity entity, Entity entity1,RunGameView runGameView) {
        //if shells shoots at shell we call an animation
        List<Entity> removedEntities = new ArrayList<>();
        removedEntities.add(entity);
        removedEntities.add(entity1);
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity.getX(), entity.getY());
        runGameView.addAnimation(RunGameView.SHELL_EXPLOSION_ANIMATION, RunGameView.SHELL_EXPLOSION_FRAME_DELAY, entity1.getX(), entity1.getY());
        return removedEntities;
    }
    public static void tankWall(Entity entity, Entity entity1,RunGameView runGameView) {
        double dist1 = distance1(entity,entity1,1);
        double dist3 = distance1(entity,entity1,2);
        double dist2 = distance1(entity,entity1,3);
        double dist4 = distance1(entity,entity1,4);
        //==create array of distances to find minimal distance
        double[] distances = {dist1, dist2, dist3, dist4};
        for(int i=0;i<distances.length;i++){
            System.out.println(distances[i]);
        }
        Arrays.sort(distances);

        double sortedDist = distances[0];
        if (sortedDist == dist1) {
            entity.setX(entity.getX() - sortedDist);
            System.out.println(entity.getX()+sortedDist+" ");
        } else if (sortedDist == dist2) {
            entity.setX(entity.getX() + sortedDist);
            System.out.println(entity.getX()+sortedDist+" ");
        } else if (sortedDist == dist3) {
            entity.setY(entity.getY() - sortedDist);
            System.out.println(entity.getY()+sortedDist+" ");
        } else if (sortedDist == dist4) {
            entity.setY(entity.getY() + sortedDist);
            System.out.println(entity.getY()+sortedDist+" ");
        }
    }

    public static void tankCollide(Entity entity, Entity entity1,RunGameView runGameView) {
        double dist1 = distance1(entity,entity1,1);
        double dist3 = distance1(entity,entity1,2);
        double dist2 = distance1(entity,entity1,3);
        double dist4 = distance1(entity,entity1,4);
        //==create array of distances to find minimal distance
        double[] distances = {dist1, dist2, dist3, dist4};
        Arrays.sort(distances);
        double sortedDist = distances[0];
        if (sortedDist == dist1) {
            entity.setX(entity.getX() - sortedDist / 2);
            entity1.setX(entity1.getX() + sortedDist / 2);
            System.out.println(entity.getX()+""+sortedDist+" ");
            System.out.println(entity1.getX()+""+sortedDist+" ");
        } else if (sortedDist == dist2) {
            entity.setX(entity.getX() + sortedDist / 2);
            entity1.setX(entity1.getX() - sortedDist / 2);
            System.out.println(entity.getX()+""+sortedDist+" ");
            System.out.println(entity1.getX()+""+sortedDist+" ");

        } else if (sortedDist == dist3) {
            entity.setY(entity.getY() - sortedDist / 2);
            entity1.setY(entity1.getY() + sortedDist / 2);
            System.out.println(entity.getY()+""+sortedDist+" ");
            System.out.println(entity1.getY()+""+sortedDist+" ");
        } else if (sortedDist == dist4) {
            entity.setY(entity.getY() + sortedDist / 2);
            entity1.setY(entity1.getY() - sortedDist / 2);
            System.out.println(entity.getY()+""+sortedDist+" ");
            System.out.println(entity1.getY()+""+sortedDist+" ");
        }
    }
}

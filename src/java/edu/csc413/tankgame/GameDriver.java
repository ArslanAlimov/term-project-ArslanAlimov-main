package edu.csc413.tankgame;

import edu.csc413.tankgame.model.*;
import edu.csc413.tankgame.view.*;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.csc413.tankgame.Collission.areEntitiesCollide;
import static edu.csc413.tankgame.Collission.tankWall;

public class GameDriver {
    private final MainView mainView;
    private final RunGameView runGameView;
    private final GameWorld gameWorld;

    public GameDriver() {
        mainView = new MainView(this::startMenuActionPerformed);
        runGameView = mainView.getRunGameView();
        gameWorld = new GameWorld();
    }

    public void start() {
        mainView.setScreen(MainView.Screen.START_GAME_SCREEN);
    }

    private void startMenuActionPerformed(ActionEvent actionEvent) {
        switch (actionEvent.getActionCommand()) {
            case StartMenuView.START_BUTTON_ACTION_COMMAND -> runGame();
            case StartMenuView.EXIT_BUTTON_ACTION_COMMAND -> mainView.closeGame();
            default -> throw new RuntimeException("Unexpected action command: " + actionEvent.getActionCommand());
        }
    }

    private void runGame() {
        mainView.setScreen(MainView.Screen.RUN_GAME_SCREEN);
        Runnable gameRunner = () -> {
            setUpGame();
            while (updateGame()) {
                runGameView.repaint();
                try {
                    Thread.sleep(10L);
                } catch (InterruptedException exception) {
                    throw new RuntimeException(exception);
                }
            }
            mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
            resetGame();
        };
        new Thread(gameRunner).start();
    }


    /**
     * setUpGame is called once at the beginning when the game is started. Entities that are present from the start
     * should be initialized here, with their corresponding sprites added to the RunGameView.
     */
    private void setUpGame() {
        // TODO: Implement.

        //Player Tank
        PlayerTank playerTank =
                new PlayerTank(
                        Constants.PLAYER_TANK_ID,
                        Constants.PLAYER_TANK_INITIAL_X,
                        Constants.PLAYER_TANK_INITIAL_Y,
                        Constants.PLAYER_TANK_INITIAL_ANGLE
                );

        //SmartAi just moves according to player movements and if distance < or > moves forward or backwards
        SmartAi aiTank2 = new SmartAi(
                Constants.AI_TANK_2_ID,
                Constants.AI_TANK_2_INITIAL_X,
                Constants.AI_TANK_2_INITIAL_Y,
                Constants.AI_TANK_2_INITIAL_ANGLE
        );

        PowerUp pwUp = new PowerUp(
                Constants.POWER_UP_1_ID,
                30.0,
                30.0,
                0);

        runGameView.addSprite(pwUp.getId(),
                RunGameView.Powerup_IMAGE_FILE,
                pwUp.getX(),
                pwUp.getY(),
                pwUp.getAngle());
        //Rotating AI
        DumbAiTank dumbTank = new DumbAiTank(
                Constants.AI_TANK_1_ID,
                Constants.AI_TANK_1_INITIAL_X,
                Constants.AI_TANK_1_INITIAL_Y,
                Constants.AI_TANK_1_INITIAL_ANGLE);
        //gameworld Entities
        gameWorld.addEntity(playerTank);
        gameWorld.addEntity(dumbTank);
        gameWorld.addEntity(aiTank2);
        //Sprites
        runGameView.addSprite(playerTank.getId(),
                RunGameView.PLAYER_TANK_IMAGE_FILE,
                playerTank.getX(),
                playerTank.getY(),
                playerTank.getAngle());
        runGameView.addSprite(dumbTank.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                dumbTank.getX(),
                dumbTank.getY(),
                dumbTank.getAngle());
        runGameView.addSprite(aiTank2.getId(),
                RunGameView.AI_TANK_IMAGE_FILE,
                aiTank2.getX(),
                aiTank2.getY(),
                aiTank2.getAngle());
        List<WallInformation> wallInformation = WallInformation.readWalls();
        for (WallInformation wallInf : wallInformation) {
            // walls don't have angles
            Wall firstWall = new Wall(wallInf.getX(), wallInf.getY(), 0);
            gameWorld.addEntity(firstWall);
            runGameView.addSprite(firstWall.getId(), wallInf.getImageFile(), firstWall.getX(), firstWall.getY(), 0);
        }

    }

    private KeyboardReader keyboardReader = KeyboardReader.instance();

    /**
     * updateGame is repeatedly called in the gameplay loop. The code in this method should run a single frame of the
     * game. As long as it returns true, the game will continue running. If the game should stop for whatever reason
     * (e.g. the player tank being destroyed, escape being pressed), it should return false.
     */
    private boolean updateGame() {
        // TODO: Implement.
        try{
            for (Entity entity : gameWorld.getEntities()) {
                entity.move(gameWorld);
            }
            for (Entity entity : gameWorld.getEntities()) {
                entity.checkBoundaries(gameWorld);
            }
            for (Entity entity : gameWorld.getEntities()) {
                runGameView.setSpriteLocationAndAngle(entity.getId(), entity.getX(), entity.getY(), entity.getAngle());
            }
            List<Entity> createdShells = gameWorld.getCreatedShells();
            for (Entity createdShell : createdShells) {
                runGameView.addSprite(createdShell.getId(), RunGameView.SHELL_IMAGE_FILE, createdShell.getX(), createdShell.getY(), createdShell.getAngle());
            }
            for (Entity entity : createdShells) {
                gameWorld.addEntity(entity);
            }
            createdShells.removeAll(createdShells);
            List<Entity> delShells = new ArrayList<>();
            for (Entity entity : gameWorld.getEntities()) {
                if (entity.borderReached()) {
                    delShells.add(entity);
                }
            }
            for (Entity entity : delShells) {
                gameWorld.delEntity(entity);
                runGameView.removeSprite(entity.getId());
            }
            List<Entity> removedEntities = new ArrayList<>();
            List<Entity> listAllEntities = gameWorld.getEntities();
            int entSize = listAllEntities.size();
            for (int i = 0; i < entSize - 1; i++) {
                for (int x = i + 1; x < entSize; x++) {
                    if (areEntitiesCollide(listAllEntities.get(i), listAllEntities.get(x))) {
                        removedEntities.addAll(Collission.collisionCheck(listAllEntities.get(i), listAllEntities.get(x),runGameView));
                    }
                }
            }
            for (Entity entity : removedEntities) {
                gameWorld.delEntity(entity);
                runGameView.removeSprite(entity.getId());
            }

            removedEntities.removeAll(removedEntities);
            for(Entity entity:gameWorld.getEntities()){
                runGameView.setSpriteLocationAndAngle(entity.getId(),entity.getX(),entity.getY(),entity.getAngle());
            }
            if(keyboardReader.escapePressed()){
                resetGame();
                return false;
            }
            if(gameWorld.getEntity(Constants.PLAYER_TANK_ID)==null)
            {
                gameWorld.removeEntity(Constants.PLAYER_TANK_ID);
                return false;
            }else if (gameWorld.getEntity(Constants.AI_TANK_1_ID)==null&&gameWorld.getEntity(Constants.AI_TANK_2_ID)==null){
                System.out.println("You won the game");
                resetGame();
                return  false;
                // don't know the sprite ='(
            }
        }catch (Exception e){
            System.out.println(e);
        }

        return true;

    }
    /**
     * resetGame is called at the end of the game once the gameplay loop exits. This should clear any existing data from
     * the game so that if the game is restarted, there aren't any things leftover from the previous run.
     */
    private void resetGame() {
        // TODO: Implement.
        mainView.setScreen(MainView.Screen.END_MENU_SCREEN);
        gameWorld.resetAll();
        runGameView.reset();

    }


    public static void main(String[] args) {
        GameDriver gameDriver = new GameDriver();
        gameDriver.start();
    }
}

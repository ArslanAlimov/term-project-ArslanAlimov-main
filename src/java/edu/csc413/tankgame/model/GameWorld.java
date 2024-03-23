package edu.csc413.tankgame.model;

import java.util.*;
import java.util.stream.Collectors;

/**
 * GameWorld holds all of the model objects present in the game. GameWorld tracks all moving entities like tanks and
 * shells, and provides access to this information for any code that needs it (such as GameDriver or entity classes).
 */

public class GameWorld {
    // TODO: Implement. There's a lot of information the GameState will need to store to provide contextual information.
    //       Add whatever instance variables, constructors, and methods are needed.
    private final List<Entity> entities;
    private final List<Entity> createdShells;

    public GameWorld() {
        // TODO: Implement.
        entities = new ArrayList<>();
        createdShells = new ArrayList<>();
    }

    /**
     * Returns a list of all entities in the game.
     */
    public List<Entity> getEntities() {
        // TODO: Implement.
        return entities;
    }

    /**
     * Adds a new entity to the game.
     */
    public void addEntity(Entity entity) {
        // TODO: Implement.
        entities.add(entity);
    }

    public void delEntity(Entity entity) {
        entities.remove(entity);
    }

    public void resetAll() {
        entities.removeAll(entities);
    }

    /**
     * Returns the Entity with the specified ID.
     */
    public Entity getEntity(String id) {
        // TODO: Implement.

        for (Entity entity : getEntities()) {
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Removes the entity with the specified ID from the game.
     */
    public void removeEntity(String id) {
        // TODO: Implement.
        entities.remove(id);
    }

    public void createShell(Entity shell) {
        createdShells.add(shell);
    }

    public List<Entity> getCreatedShells() {
        return createdShells;
    }
}

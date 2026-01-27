package com.runner.core;

import java.io.IOException;
import java.util.Random;
import com.runner.model.GameObject;
import com.runner.model.Obstacle;
import com.runner.model.Powerup;
import com.runner.model.JumpObject;
import com.runner.utils.DataPool;
import com.runner.logic.jump.HighJump;
import com.runner.logic.jump.LowJump;

/**
 * Spawns game objects at random intervals during gameplay.
 * Implements Runnable to spawn objects on a separate thread.
 * Randomly selects between obstacles, powerups, and jump abilities to spawn.
 * Safely accesses object pools starting from the first element to optimize
 * resource usage.
 */
public class ObjectSelector implements Runnable {
    private GameManager game_manager;
    private final int max_wait_ms = 2000; // maximum spawn time between objects in milliseconds.
    private boolean spawn = true;

    private int start_pos_x = 1280;
    private int start_pos_y = 550;

    public static int spawn_pos_x = 9999;
    public static int spawn_pos_y = 9999;

    private final int number_of_object_types = 4;

    public ObjectSelector(GameManager _game_manager) {
        this.game_manager = _game_manager;
    }

    @Override
    public void run() {
        try {
            RandomObjectSelector();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    public void RandomObjectSelector() throws InterruptedException, IOException {
        Random r = new Random();

        while (spawn) {
            long result = Math.abs(r.nextLong() % number_of_object_types); // wait a random time up to 1 second
            if (result == 0) {
                SelectObstacle();
            } else if (result == 1) {
                SelectPowerup();
            } else if (result == 2) {
                SelectLowJump();
            } else if (result == 3) {
                SelectHighJump();
            }
        }
    }

    private void SelectHighJump() throws InterruptedException {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0) {
            for (int i = 0; i < game_manager.getNumberOfGameDeactiveObjects(); i++) {
                GameObject obj = game_manager.getDeactiveObject(i);
                if (obj instanceof JumpObject) {
                    JumpObject jumpObject = (JumpObject) obj;
                    if (jumpObject.jump_strategy instanceof HighJump) {
                        jumpObject.setPosition(start_pos_x, start_pos_y);
                        GameEventManager.getInstance().handle_event(jumpObject);
                        game_manager.removeDeactiveObject(jumpObject);
                        game_manager.addActiveObject(jumpObject);
                        break;
                    }
                }
            }
        }
    }

    private void SelectLowJump() throws InterruptedException {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0) {
            for (int i = 0; i < game_manager.getNumberOfGameDeactiveObjects(); i++) {
                GameObject obj = game_manager.getDeactiveObject(i);
                if (obj instanceof JumpObject) {
                    JumpObject jumpObject = (JumpObject) obj;
                    if (jumpObject.jump_strategy instanceof LowJump) {
                        jumpObject.setPosition(start_pos_x, start_pos_y);
                        GameEventManager.getInstance().handle_event(jumpObject);
                        game_manager.removeDeactiveObject(jumpObject);
                        game_manager.addActiveObject(jumpObject);
                        break;
                    }
                }
            }
        }
    }

    private void SelectPowerup() throws InterruptedException {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0) {
            for (int i = 0; i < game_manager.getNumberOfGameDeactiveObjects(); i++) {
                GameObject obj = game_manager.getDeactiveObject(i);
                if (obj instanceof Powerup) {
                    Powerup powerup = (Powerup) obj;
                    powerup.setPosition(start_pos_x, start_pos_y);
                    GameEventManager.getInstance().handle_event(powerup);
                    game_manager.removeDeactiveObject(powerup);
                    game_manager.addActiveObject(powerup);
                    break;
                }
            }
        }
    }

    private void SelectObstacle() throws InterruptedException {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0) {
            for (int i = 0; i < game_manager.getNumberOfGameDeactiveObjects(); i++) {
                GameObject obj = game_manager.getDeactiveObject(i);
                if (obj instanceof Obstacle) {
                    Obstacle obstacle = (Obstacle) obj;
                    obstacle.setPosition(start_pos_x, start_pos_y);
                    GameEventManager.getInstance().handle_event(obstacle);
                    game_manager.removeDeactiveObject(obstacle);
                    game_manager.addActiveObject(obstacle);
                    break;
                }
            }
        }
    }
}

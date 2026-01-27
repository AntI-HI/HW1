/**
 * Manages collision detection and physics interactions between game objects.
 * Detects collisions between the player and other objects, and triggers
 * appropriate handlers.
 * Implements the singleton pattern to ensure a single physics manager.
 * Collision checks are performed in a synchronized block to ensure thread
 * safety with concurrent object lists.
 */
public class PhysicsManager {
    private static PhysicsManager instance = null;
    private GameManager game_manager = null;

    public final double GRAVITY = 1f;

    public boolean pause = false;

    public static PhysicsManager CreatePhysicsManagerInstance(GameManager game_manager) {
        if (instance == null) {
            instance = new PhysicsManager(game_manager);
        }
        return instance;
    }

    public static PhysicsManager getInstance() {
        return instance;
    }

    private PhysicsManager(GameManager game_manager) {
        this.game_manager = game_manager;
    }

    public boolean isCollide(GameObject source, GameObject target) {
        boolean is_collide = false;
        String message = "Unrecognized message";

        UI_Elements ui = game_manager.getUi_Elements();

        if (source.getBounds().intersects(target.hitbox)) {
            is_collide = true;
            target.collides = true;
            if (target instanceof Obstacle) {
                message = "Collided with obstacle";
            } else if (target instanceof Powerup) {
                message = "Collided with score powerup";
            } else if (target instanceof JumpObject) {
                message = "Obtained " + ((JumpObject) target).jump_strategy.toString() + " ability";
            }

            ui.updateEventLabel(message);
        }
        source.set_event(is_collide);

        return is_collide;
    }

    public boolean CollisionCheck() {
        boolean collides = false;
        Player player = game_manager.getPlayer();
        ScoreManager scoreManager = ScoreManager.getInstance();

        if (!pause) {
            java.util.List<GameObject> activeObjects = game_manager.getActiveGameObjects();
            synchronized (activeObjects) {
                for (GameObject object : activeObjects) {
                    if (object instanceof Player) {
                        continue;
                    }
                    if (object.isActive) {
                        collides = isCollide(player, object);

                        if (collides) {
                            if (object instanceof Obstacle) {
                                player.handle_clash((Obstacle) object);
                            } else if (object instanceof Powerup) {
                                player.handle_powerup((Powerup) object);
                            } else if (object instanceof JumpObject) {
                                player.setJumpStrategy(((JumpObject) object).jump_strategy);
                            }
                            object.isActive = false;
                            break;
                        } else {
                            if (object instanceof Obstacle) {
                                scoreManager.setObstacle((Obstacle) object);
                                scoreManager.UpdateScore();
                            }
                        }
                    }
                }
            }
        }

        return collides;
    }
}

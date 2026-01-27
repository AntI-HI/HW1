package com.runner.model;

import java.awt.Graphics2D;
import java.awt.Image;
import com.runner.utils.DataPool;
import com.runner.core.GameManager;
import com.runner.ui.UI_Elements;
import com.runner.logic.jump.JumpStrategy;
import com.runner.logic.jump.LowJump;
import com.runner.logic.score.ScoreManager;
import com.runner.logic.score.ScorePowerup;
import com.runner.logic.score.X2Powerup;

/**
 * Represents the player character in the game.
 * Extends GameObject and manages player movement, jumping, health, and powerup
 * handling.
 * Implements the singleton pattern to ensure only one player instance exists.
 */
public class Player extends GameObject {
    private JumpStrategy jump_strategy;
    private UI_Elements ui;

    private int initial_jump_speed;
    private int curr_vertical_speed;
    private int playerSpeed;
    private boolean is_jumping;
    private boolean first_jump;
    private boolean is_ascending;
    private boolean is_descending;
    private boolean is_dead;

    private int player_health;

    public static Player player_instance = null;

    /**
     * Factory method to create and return the singleton player instance.
     * 
     * @param _xPos Initial X position
     * @param _yPos Initial Y position
     * @return The Player instance
     */
    public static Player Create(int _xPos, int _yPos) {
        if (player_instance == null) {
            DataPool dataPool = DataPool.getInstance();
            Image img = dataPool.getPlayerSprite();
            player_instance = new Player(_xPos, _yPos, img.getWidth(null), img.getHeight(null));
            player_instance.jump_strategy = new LowJump();
        }

        return player_instance;
    }

    /**
     * Gets the singleton player instance.
     * 
     * @return The Player instance
     */
    public static Player getPlayerInstance() {
        return player_instance;
    }

    /**
     * Private constructor for the Player.
     * Initializes player state, sprite, and default values.
     * 
     * @param _xPos  Initial X position
     * @param _yPos  Initial Y position
     * @param width  Player sprite width
     * @param height Player sprite height
     */
    private Player(int _xPos, int _yPos, int width, int height) {
        super(_xPos, _yPos, width, height);
        this.ui = GameManager.getInstance().getUi_Elements();
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getPlayerSprite(), 0, 0, null);
        g2d.dispose();

        is_dead = false;
        playerSpeed = 0;
        first_jump = true;
        is_jumping = false;
        player_health = 3;

        player_instance = this;
    }

    /**
     * Updates the player state each frame.
     * Handles jump physics if the player is currently jumping.
     */
    @Override
    public void update() {
        if (is_jumping()) {
            jump();
        }
    }

    /**
     * Executes the current jump strategy.
     */
    public void jump() {
        jump_strategy.Jump();
    }

    /**
     * Gets the current jump strategy.
     * 
     * @return The current JumpStrategy
     */
    public JumpStrategy getJumpStrategy() {
        return this.jump_strategy;
    }

    /**
     * Checks if the player is dead.
     * 
     * @return True if the player is dead, false otherwise
     */
    public Boolean isDead() {
        return this.is_dead;
    }

    /**
     * Sets the jump strategy for the player.
     * 
     * @param _jump_strategy The new JumpStrategy to use
     */
    public void setJumpStrategy(JumpStrategy _jump_strategy) {
        this.jump_strategy = _jump_strategy;
    }

    /**
     * Marks the player as dead and updates the UI.
     */
    public void Die() {
        this.is_dead = true;
        ui.updateEventLabel("You Died!");
    }

    /**
     * Gets the current player speed.
     * 
     * @return The current speed value
     */
    public int getPlayerSpeed() {
        return this.playerSpeed;
    }

    /**
     * Sets the player's horizontal movement speed.
     * 
     * @param speed The new speed value
     */
    public void setPlayerSpeed(int speed) {
        this.playerSpeed = speed;
        dataPool.setSpeed(speed);
    }

    /**
     * Gets the current player health.
     * 
     * @return The current health value
     */
    public int getPlayerHealth() {
        return this.player_health;
    }

    /**
     * Initiates an upward jump for the player.
     */
    public void ascend() {
        is_jumping = true;
        is_ascending = true;
        first_jump = true;
    }

    /**
     * Checks if the player is currently jumping.
     * 
     * @return True if jumping, false otherwise
     */
    public boolean is_jumping() {
        return is_jumping;
    }

    /**
     * Checks if this is the first frame of the jump.
     * 
     * @return True if first jump frame, false otherwise
     */
    public boolean is_first_jump() {
        return first_jump;
    }

    /**
     * Checks if the player is ascending during a jump.
     * 
     * @return True if ascending, false otherwise
     */
    public boolean is_ascending() {
        return is_ascending;
    }

    /**
     * Checks if the player is descending during a jump.
     * 
     * @return True if descending, false otherwise
     */
    public boolean is_descending() {
        return is_descending;
    }

    /**
     * Gets the initial jump speed value.
     * 
     * @return The initial jump speed
     */
    public int get_initial_jump_speed() {
        return initial_jump_speed;
    }

    /**
     * Gets the current vertical speed during jump.
     * 
     * @return The current vertical speed
     */
    public int get_curr_vertical_speed() {
        return curr_vertical_speed;
    }

    /**
     * Sets the current vertical speed during jump.
     * 
     * @param speed The new vertical speed
     */
    public void set_curr_vertical_speed(int speed) {
        curr_vertical_speed = speed;
    }

    /**
     * Sets whether this is the first frame of the jump.
     * 
     * @param jump True to indicate first jump frame
     */
    public void set_first_jump(boolean jump) {
        first_jump = jump;
    }

    /**
     * Sets the ascending state during jump.
     * 
     * @param ascending True if ascending, false otherwise
     */
    public void set_is_ascending(boolean ascending) {
        is_ascending = ascending;
    }

    /**
     * Sets the descending state during jump.
     * 
     * @param descending True if descending, false otherwise
     */
    public void set_is_descending(boolean descending) {
        is_descending = descending;
    }

    /**
     * Sets the jumping state.
     * 
     * @param jumping True if jumping, false otherwise
     */
    public void set_is_jumping(boolean jumping) {
        is_jumping = jumping;
    }

    /**
     * Sets the initial jump speed value.
     * 
     * @param speed The initial jump speed
     */
    public void set_initial_jump_speed(int speed) {
        initial_jump_speed = speed;
    }

    /**
     * Handles powerup collision and applies score multiplier effects.
     * 
     * @param scorePowerup The powerup object collected
     */
    public void handle_powerup(Powerup scorePowerup) {
        ScorePowerup _scorePowerup;
        ScoreManager scoreManager = ScoreManager.getInstance();
        if (scoreManager.score_powerup == null) {
            _scorePowerup = new ScorePowerup();
            scoreManager.setScorePowerup(_scorePowerup);
        } else {
            scoreManager.score_powerup = new X2Powerup(scoreManager.score_powerup);
        }
    }

    /**
     * Handles collision with an obstacle and reduces health.
     * Dies if health reaches zero.
     * 
     * @param object The obstacle that was hit
     */
    public void handle_clash(Obstacle object) {
        if (player_health > 0) {
            player_health--;
        }
        if (player_health == 0) {
            Die();
        }
    }
}

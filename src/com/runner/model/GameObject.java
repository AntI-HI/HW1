package com.runner.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import com.runner.utils.DataPool;
import com.runner.core.GameManager;
import com.runner.core.ObjectSelector;

/**
 * Abstract base class for all game objects in the game.
 * Provides common functionality for position, velocity, collision detection,
 * and rendering.
 * All game entities (player, obstacles, powerups, etc.) extend this class.
 */
public abstract class GameObject {
    public int xPos, yPos, width, height;
    protected float velX, velY;
    public Rectangle hitbox = new Rectangle();
    protected double boundpos = 0.03; // goes 3% into the sprite of the image
    public BufferedImage img = null;
    protected Color B = new Color(0, 0, 0);

    protected Rectangle top = new Rectangle(), bottom = new Rectangle(), right = new Rectangle(),
            left = new Rectangle();
    protected DataPool dataPool;

    public boolean collides = false;

    public boolean isActive = false;

    /**
     * Default empty constructor.
     */
    public GameObject() {
    }

    /**
     * Constructor with position and dimensions.
     * 
     * @param _xPos   X position
     * @param _yPos   Y position
     * @param _width  Width of the object
     * @param _height Height of the object
     */
    public GameObject(int _xPos, int _yPos, int _width, int _height) {
        setStart(_xPos, _yPos, _width, _height);
        setVelocity(1f, 0f);
        img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);

        hitbox.setBounds((int) (xPos), (int) (yPos), (int) (width), (int) (height));
    }

    /**
     * Constructor with BufferedImage (uses default 3% boundary).
     * 
     * @param _xPos X position
     * @param _yPos Y position
     * @param img   The sprite image
     */
    public GameObject(int _xPos, int _yPos, BufferedImage img) {
        setStart(_xPos, _yPos, img.getWidth(), img.getHeight());
        setVelocity(1f, 0f);

        hitbox.setBounds((int) (_xPos + (width * boundpos)), (int) (_yPos + (height * boundpos)),
                (int) (width * (boundpos * 2)), (int) (height * (boundpos * 2)));
    }

    /**
     * Constructor with BufferedImage and custom boundary position.
     * 
     * @param _xPos    X position
     * @param _yPos    Y position
     * @param img      The sprite image
     * @param boundpos The boundary position percentage (0.0 to 1.0)
     */
    public GameObject(int _xPos, int _yPos, BufferedImage img, double boundpos) {
        setStart(_xPos, _yPos, img.getWidth(), img.getHeight());
        setVelocity(1f, 0f);

        this.boundpos = boundpos;
        hitbox.setBounds((int) (_xPos + (width * boundpos)), (int) (_yPos + (height * boundpos)),
                (int) (width * (boundpos * 2)), (int) (height * (boundpos * 2)));
    }

    /**
     * Sets up initial position and dimensions for the game object.
     * 
     * @param _xPos   X position
     * @param _yPos   Y position
     * @param _width  Width of the object
     * @param _height Height of the object
     */
    private void setStart(int _xPos, int _yPos, int _width, int _height) {
        setPosition(_xPos, _yPos);
        setDimension(_width, _height);
        this.dataPool = DataPool.getInstance();
    }

    /**
     * Sets the position of the game object and updates hitbox.
     * 
     * @param x X position
     * @param y Y position
     */
    public void setPosition(int x, int y) {
        this.xPos = x;
        this.yPos = y;
        hitbox.setBounds((int) (xPos), (int) (yPos), (int) (width), (int) (height));
    }

    /**
     * Sets the dimensions (width and height) of the game object.
     * 
     * @param _width  The width in pixels
     * @param _height The height in pixels
     */
    private void setDimension(int _width, int _height) {
        this.width = _width;
        this.height = _height;
    }

    /**
     * Sets the velocity components.
     * 
     * @param _velX Horizontal velocity
     * @param _velY Vertical velocity
     */
    private void setVelocity(float _velX, float _velY) {
        this.velX = _velX;
        this.velY = _velY;
    }

    /**
     * Gets the bounding rectangle of the game object.
     * 
     * @return A Rectangle representing the object's bounds
     */
    public Rectangle getBounds() {
        return new Rectangle(this.xPos, this.yPos, this.width, this.height);
    }

    /**
     * Sets the collision state of this object.
     * 
     * @param is_collide Whether this object is colliding
     */
    public void set_event(boolean is_collide) {
        collides = is_collide;
    }

    /**
     * Sets both the position and bounds of the game object.
     * 
     * @param _xPos   X position
     * @param _yPos   Y position
     * @param _width  Width of the object
     * @param _height Height of the object
     */
    public void setObjectPositionAndBounds(int _xPos, int _yPos, int _width, int _height) {
        setStart(_xPos, _yPos, _width, _height);
        setVelocity(1f, 0f);

        hitbox.setBounds((int) (xPos), (int) (yPos), (int) (width), (int) (height));
    }

    /**
     * Creates a new BufferedImage for the game object.
     * 
     * @param _width  Width of the new image
     * @param _height Height of the new image
     */
    protected void GameObjectCreateGraphics(int _width, int _height) {
        img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Deactivates the object and moves it to the inactive pool.
     */
    protected void DeactivateObject() {
        GameManager game_manager = GameManager.getInstance();

        setPosition(ObjectSelector.spawn_pos_x, ObjectSelector.spawn_pos_y);
        isActive = false;
        game_manager.removeActiveObject(this);
        game_manager.addDeactiveObject(this);
    }

    /**
     * Checks if the object is outside the visible game screen.
     * 
     * @return True if the object is off-screen, false otherwise
     */
    protected boolean isObjectOutsideScreen() {
        if (xPos + width < 0) {
            return true;
        }
        return false;
    }

    /**
     * Default update behavior for game objects.
     * Moves the object based on player speed and deactivates if off-screen.
     * Subclasses can override this method for custom behavior.
     */
    public void update() {
        int playerSpeed = dataPool.getSpeed();
        xPos -= playerSpeed;
        hitbox.setBounds((int) (xPos), (int) (yPos), (int) (width), (int) (height));

        if (isObjectOutsideScreen()) {
            DeactivateObject();
        }
    }
}

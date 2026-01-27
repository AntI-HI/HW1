import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Central manager class that coordinates all game systems and state.
 * Manages game objects, physics, events, score, UI, and rendering.
 * Implements the singleton pattern with a private constructor and a static
 * factory method.
 * Uses thread-safe synchronized ArrayLists for object management to support
 * concurrent updates and rendering.
 */
public class GameManager {
	private java.util.List<GameObject> deactiveObjects = java.util.Collections
			.synchronizedList(new java.util.ArrayList<GameObject>());
	private java.util.List<GameObject> activeObjects = java.util.Collections
			.synchronizedList(new java.util.ArrayList<GameObject>());

	public static int player_posX = 240;
	public static int player_posY = 550;
	public static int player_width = 50;
	public static int player_height = 80;

	public static int obstacle_width = 100;
	public static int obstacle_height = 100;

	public static int powerup_width = 100;
	public static int powerup_height = 100;

	public static int high_jump_width = 100;
	public static int high_jump_height = 100;

	public static int low_jump_width = 100;
	public static int low_jump_height = 100;

	private Player player;
	private Background background;
	public ScoreManager scoreManager;
	private AnimationPane animationPane;
	public PhysicsManager physicsManager;
	public ObjectSelector objectSelector;
	private ObjectFactory objectFactory;
	public GameEventManager event_manager;

	private UI_Elements ui;

	public int current_obstacle_idx = 1;

	private boolean paused;

	private static GameManager instance = null;

	/**
	 * Gets the singleton instance of GameManager.
	 * 
	 * @return The GameManager instance
	 */
	public static GameManager getInstance() {
		return instance;
	}

	/**
	 * Private constructor that initializes the DataPool singleton.
	 * Enforced by singleton pattern.
	 * 
	 * @throws IOException if there is an error loading game assets
	 */
	private GameManager() throws IOException {
		DataPool.CreateDataPool();
		instance = this;
	}

	/**
	 * Static factory method to get or create the singleton instance of GameManager.
	 * 
	 * @return The GameManager instance
	 * @throws IOException if the manager is being created for the first time and
	 *                     asset loading fails
	 */
	public static GameManager CreateGameManager() throws IOException {
		if (instance == null) {
			instance = new GameManager();
		}
		return instance;
	}

	/**
	 * Early initialization method that sets up all game managers, managers, and
	 * asset pools.
	 * Loads sprites, creates object pools, and starts the object selector thread.
	 */
	private void Game_Manager_Early_Init() {
		try {
			scoreManager = ScoreManager.CreateScoreManager(this);

			this.event_manager = GameEventManager.CreateEventManager(this);
			this.physicsManager = PhysicsManager.CreatePhysicsManagerInstance(this);
			this.objectSelector = new ObjectSelector(this);
			this.objectFactory = new ObjectFactory(this);
			this.animationPane = new AnimationPane(this);
			this.background = new Background();
			this.ui = new UI_Elements(animationPane);

			addPlayerSprite(Game.playerSprite, player_width, player_height, true);
			addObstacleSprite(Game.obstacleSprite, obstacle_width, obstacle_height, true);
			addPowerupSprite(Game.powerupSprite, powerup_width, powerup_height, true);
			addBackgroundSprite(Game.background_img, Game.window_width, Game.window_height);
			addHighJumpSprite(Game.high_jump_sprite, high_jump_width, high_jump_height, true);
			addLowJumpSprite(Game.low_jump_sprite, low_jump_width, low_jump_height, true);

			// objectSpawner.CreatePlayer();
			objectFactory.fillObjectPool();

			Thread t1 = new Thread(this.objectSelector);
			t1.start();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates an image filter to make a specific color transparent for powerup
	 * sprites.
	 * 
	 * @return An ImageFilter that filters out the target color
	 */
	private ImageFilter filterTransparentForPowerup() {
		ImageFilter filter = new RGBImageFilter() {
			int filterColor = -394759;

			public final int filterRGB(int x, int y, int rgb) {
				if (rgb == filterColor) {
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				} else {
					return rgb;
				}
			}
		};

		return filter;
	}

	/**
	 * Loads and configures the powerup sprite image with optional transparency
	 * filtering.
	 * 
	 * @param sprite          Path to the sprite image file
	 * @param width           Desired width of the sprite
	 * @param height          Desired height of the sprite
	 * @param requires_filter Whether to apply transparency filtering
	 * @throws IOException if the sprite file cannot be read
	 */
	private void addPowerupSprite(String sprite,
			int width,
			int height,
			boolean requires_filter) throws IOException {
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter) {
			ImageFilter filter = filterTransparentForPowerup();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img = transparentImg;
		}
		DataPool.getInstance().setPowerupSprite(img);
	}

	/**
	 * Loads and configures the background image.
	 * 
	 * @param backgroundImg     Path to the background image file
	 * @param background_width  Desired width of the background
	 * @param background_height Desired height of the background
	 */
	private void addBackgroundSprite(String backgroundImg, int background_width, int background_height) {
		Image tmp;
		BufferedImage _img;

		try {
			_img = ImageIO.read(new File(backgroundImg));
			this.background.setBackgroundImage(_img);
		} catch (IOException e) {
			e.printStackTrace();
		}

		_img = background.getBackgroundImage();
		tmp = _img.getScaledInstance(1280, 720, Image.SCALE_SMOOTH);

		BufferedImage dimg = new BufferedImage(background_width, background_height, BufferedImage.TYPE_INT_ARGB);

		this.background.setBackgroundImage(dimg);
		Graphics2D graphics = dimg.createGraphics();
		graphics.drawImage(tmp, 0, 0, null);
		graphics.dispose();
	}

	/**
	 * Loads and configures the player sprite image with optional transparency
	 * filtering.
	 * 
	 * @param sprite          Path to the sprite image file
	 * @param width           Desired width of the sprite
	 * @param height          Desired height of the sprite
	 * @param requires_filter Whether to apply transparency filtering
	 * @throws IOException if the sprite file cannot be read
	 */
	public void addPlayerSprite(String sprite,
			int width,
			int height,
			boolean requires_filter) throws IOException {
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter) {
			ImageFilter filter = filterTransparentForPlayer();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img = transparentImg;
		}

		DataPool.getInstance().setPlayerSprite(img);
	}

	/**
	 * Creates an image filter to make a specific color transparent for player
	 * sprites.
	 * 
	 * @return An ImageFilter that filters out the target color
	 */
	private ImageFilter filterTransparentForPlayer() {
		ImageFilter filter = new RGBImageFilter() {
			public final int filterRGB(int x, int y, int rgb) {
				if (rgb == 0xFFF6F6F6) {
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				} else {
					return rgb;
				}
			}
		};
		return filter;
	}

	/**
	 * Loads and configures the obstacle sprite image with optional transparency
	 * filtering.
	 * 
	 * @param sprite          Path to the sprite image file
	 * @param width           Desired width of the sprite
	 * @param height          Desired height of the sprite
	 * @param requires_filter Whether to apply transparency filtering
	 * @throws IOException if the sprite file cannot be read
	 */
	public void addObstacleSprite(String sprite,
			int width,
			int height,
			boolean requires_filter) throws IOException {
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter) {
			ImageFilter filter = filterTransparentForObstacle();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img = transparentImg;
		}

		DataPool.getInstance().setObstacleSprite(img);
	}

	/**
	 * Creates an image filter to make specific colors transparent for obstacle
	 * sprites.
	 * 
	 * @return An ImageFilter that filters out target colors
	 */
	private ImageFilter filterTransparentForObstacle() {
		ImageFilter filter = new RGBImageFilter() {
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if (rgb == 0xffeeeeee || rgb == 0xffffffff || rgb == 0xffededed) {
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				}
				if ((rgb | 0xFF000000) == transparentColor) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		return filter;
	}

	/**
	 * Loads and configures the high jump sprite image with optional transparency
	 * filtering.
	 * 
	 * @param sprite          Path to the sprite image file
	 * @param width           Desired width of the sprite
	 * @param height          Desired height of the sprite
	 * @param requires_filter Whether to apply transparency filtering
	 * @throws IOException if the sprite file cannot be read
	 */
	public void addHighJumpSprite(String sprite,
			int width,
			int height,
			boolean requires_filter) throws IOException {
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter) {
			ImageFilter filter = filterTransparentForHighJump();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img = transparentImg;
		}

		DataPool.getInstance().setHighJumpSprite(img);
	}

	/**
	 * Creates an image filter to make specific colors transparent for high jump
	 * sprites.
	 * 
	 * @return An ImageFilter that filters out black colors
	 */
	private ImageFilter filterTransparentForHighJump() {
		ImageFilter filter = new RGBImageFilter() {
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == transparentColor) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		return filter;
	}

	/**
	 * Loads and configures the low jump sprite image with optional transparency
	 * filtering.
	 * 
	 * @param sprite          Path to the sprite image file
	 * @param width           Desired width of the sprite
	 * @param height          Desired height of the sprite
	 * @param requires_filter Whether to apply transparency filtering
	 * @throws IOException if the sprite file cannot be read
	 */
	public void addLowJumpSprite(String sprite,
			int width,
			int height,
			boolean requires_filter) throws IOException {
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter) {
			ImageFilter filter = filterTransparentForLowJump();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img = transparentImg;
		}

		DataPool.getInstance().setLowJumpSprite(img);
	}

	/**
	 * Creates an image filter to make specific colors transparent for low jump
	 * sprites.
	 * 
	 * @return An ImageFilter that filters out black colors
	 */
	private ImageFilter filterTransparentForLowJump() {
		ImageFilter filter = new RGBImageFilter() {
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == transparentColor) {
					return 0x00FFFFFF & rgb;
				} else {
					return rgb;
				}
			}
		};

		return filter;
	}

	/**
	 * Starts the game manager initialization and object pool setup.
	 */
	public void Start() {
		Game_Manager_Early_Init();
	}

	/**
	 * Updates all active game objects and performs post-update operations.
	 * Skips update if player is dead or game is paused.
	 * Performs backward iteration for safe object deactivation and removal.
	 * This method is thread-safe.
	 */
	public void Update() {
		if (player != null && player.getPlayerHealth() > 0 && !paused) {
			synchronized (activeObjects) {
				for (int i = activeObjects.size() - 1; i >= 0; i--) {
					activeObjects.get(i).update();
				}
			}
			Post_Update();
		}
	}

	/**
	 * Performs post-update operations including background scrolling and collision
	 * detection.
	 */
	private void Post_Update() {
		background.Update();
		physicsManager.CollisionCheck();
	}

	/**
	 * Renders the current game state through the animation pane.
	 */
	public void draw() {
		animationPane.paint();
	}

	/**
	 * Gets the game background object.
	 * 
	 * @return The Background instance
	 */
	public Background getBackground() {
		return background;
	}

	/**
	 * Gets the player object.
	 * 
	 * @return The Player instance
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Gets the UI elements handler.
	 * 
	 * @return The UI_Elements instance
	 */
	public UI_Elements getUi_Elements() {
		return ui;
	}

	/**
	 * Gets the animation pane (game rendering panel).
	 * 
	 * @return The AnimationPane instance
	 */
	public AnimationPane getAnimationPane() {
		return animationPane;
	}

	/**
	 * Sets the animation pane reference.
	 * 
	 * @param _animationPane The AnimationPane to set
	 */
	public void setAnimationPane(AnimationPane _animationPane) {
		this.animationPane = _animationPane;
	}

	/**
	 * Sets the player object.
	 * 
	 * @param gameObject The GameObject to cast to Player
	 */
	public void setPlayer(GameObject gameObject) {
		this.player = (Player) gameObject;
	}

	/**
	 * Toggles the paused state of the game.
	 */
	public void pause_game() {
		paused = !paused;
	}

	/**
	 * Adds a game object to the active objects list.
	 * 
	 * @param object The GameObject to activate
	 */
	public void addActiveObject(GameObject object) {
		activeObjects.add(object);
	}

	/**
	 * Removes a game object from the active objects list.
	 * 
	 * @param object The GameObject to deactivate
	 */
	public void removeActiveObject(GameObject object) {
		activeObjects.remove(object);
	}

	/**
	 * Adds a game object to the inactive objects pool.
	 * 
	 * @param object The GameObject to deactivate
	 */
	public void addDeactiveObject(GameObject object) {
		deactiveObjects.add(object);
	}

	/**
	 * Removes a game object from the inactive objects pool.
	 * 
	 * @param object The GameObject to remove
	 */
	public void removeDeactiveObject(GameObject object) {
		deactiveObjects.remove(object);
	}

	/**
	 * Gets the number of inactive game objects.
	 * 
	 * @return The count of inactive objects
	 */
	public int getNumberOfGameDeactiveObjects() {
		return deactiveObjects.size();
	}

	/**
	 * Gets the number of active game objects.
	 * 
	 * @return The count of active objects
	 */
	public int getNumberOfGameActiveObjects() {
		return activeObjects.size();
	}

	/**
	 * Gets an inactive object at the specified index.
	 * 
	 * @param i The index in the inactive objects list
	 * @return The GameObject at the specified index
	 */
	public GameObject getDeactiveObject(int i) {
		return deactiveObjects.get(i);
	}

	/**
	 * Gets an active object at the specified index.
	 * 
	 * @param i The index in the active objects list
	 * @return The GameObject at the specified index
	 */
	public GameObject getActiveObject(int i) {
		return activeObjects.get(i);
	}

	/**
	 * Gets the list of all active game objects.
	 * 
	 * @return LinkedList of active GameObjects
	 */
	public java.util.List<GameObject> getActiveGameObjects() {
		return activeObjects;
	}
}

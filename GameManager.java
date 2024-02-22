import java.awt.Color;
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
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class GameManager
{
	protected LinkedList<GameObject> objects = new LinkedList<GameObject>();
	protected boolean up = false, down = false, right = false, left = false;
	protected final static double GRAVITY = 1f;

	public static int player_posX	= 240;
	public static int player_posY	= 550;
	public static int player_width	= 50;
	public static int player_height	= 80;

	public static int obstacle_width  = 100;
	public static int obstacle_height = 100;

	public static int powerup_width	  = 100;
	public static int powerup_height  = 100;

	private   Player 		  player;
	private   Obstacle        obstacle;
	private	  Powerup		  powerup;
	private   Background 	  background;
	private   ScoreManager    scoreManager;
	private	  AnimationPane   animationPane;
	private	  PhysicsManager  physicsManager;
	public    InstanceSpawner instanceSpawner;
	public GameEventManager  event_manager;

	private int current_obstacle_idx = 1;
	public boolean new_spawned	= false;
	
    public GameManager() throws IOException
	{
		DataPool.CreateDataPool();
		scoreManager = ScoreManager.CreateScoreManager();

		this.animationPane  = new AnimationPane(this);
		this.physicsManager = PhysicsManager.CreatePhysicsManagerInstance();
		this.background     = new Background();

		addPlayerSprite(Game.playerSprite, player_width, player_height, true);
		addObstacleSprite(Game.obstacleSprite, obstacle_width, obstacle_height, true);
		addPowerupSprite(Game.powerupSprite, powerup_width, powerup_height, true);
		addBackgroundSprite(Game.background_img, Game.window_width, Game.window_height);

		Game_Manager_Early_Init();
    }
	
	private void Game_Manager_Early_Init()
	{
		AddObject(player_posX, player_posY, ObjectTypes.PLAYER);
	}

	private ImageFilter filterTransparentForPowerup()
	{
		ImageFilter filter = new RGBImageFilter()
		{
			int filterColor = -394759;

			public final int filterRGB(int x, int y, int rgb)
			{
				if (rgb == filterColor)
				{
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				}
				else
				{
					return rgb;
				}
			}
		};

		return filter;
	}

	private void addPowerupSprite(String sprite,
								int width,
								int height,
								boolean requires_filter) throws IOException
	{
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter)
		{
			ImageFilter   filter 		  = filterTransparentForPowerup();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image 		  transparentImg  = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img	= transparentImg;
		}
		DataPool.getInstance().setPowerupSprite(img);
	}

	private void addBackgroundSprite(String backgroundImg, int background_width, int background_height)
	{
		Image tmp;
		BufferedImage _img;

		try
		{
			_img = ImageIO.read(new File(backgroundImg));
			this.background.setBackgroundImage(_img);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
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

	public void addPlayerSprite(String sprite,
								int width,
								int height,
								boolean requires_filter) throws IOException
	{
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter)
		{
			ImageFilter   filter 		  = filterTransparentForPlayer();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image 		  transparentImg  = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img	= transparentImg;
		}

		DataPool.getInstance().setPlayerSprite(img);
	}

	public void AddObject(int posX, int posY, ObjectTypes type)
	{
		DataPool   dataPool = DataPool.getInstance();
		Image 	   img;
		GameObject obj;

		if (type == ObjectTypes.PLAYER)
		{
			img = dataPool.getPlayerSprite();
			int width  = img.getWidth(null);
			int height = img.getHeight(null);
			this.player = Player.createPlayerInstance(posX, posY, width, height);
			obj = this.player;
			scoreManager.setPlayer(player);
		}
		else if (type == ObjectTypes.OBSTACLE)
		{
			img = dataPool.getObstacleSprite();
			int width  = img.getWidth(null);
			int height = img.getHeight(null);
			this.obstacle = new Obstacle(posX, posY, width, height);
			obj = this.obstacle;
			new_spawned = true;				// Means the new object is created. It used for waking the object selector.
			if (objects.size() == 1)
			{
				scoreManager.pause = false;
				physicsManager.pause = false;
			}
		}
		else
		{
			img = dataPool.getPowerupSprite();
			int width  = img.getWidth(null);
			int height = img.getHeight(null);
			this.powerup = new Powerup();

			this.powerup.setObjectPositionAndBounds(posX, posY, width, height);

			obj = this.powerup;
			new_spawned = true;	// Means the new object is created. It used for waking the object selector.
			if (objects.size() == 1)
			{
				scoreManager.pause = false;	  // Unpause the score calculation utility for the first obstacle spawn.
				physicsManager.pause = false; // Unpause the physics calculation utility for the first obstacle spawn.
		
			}
		}
		
		objects.add(obj);
	}

	// TODO: Once object creation design is fully functional, this function will be removed.
	public void addObstacle(GameObject obstacle)
	{
		objects.add(obstacle);
	}

	private ImageFilter filterTransparentForPlayer()
	{
		ImageFilter filter = new RGBImageFilter()
		{
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb)
			{
				if (rgb == 0xFFF6F6F6)
				{
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				}
				// if ((rgb | 0xFF000000) == transparentColor)
				// {
				// 	return 0x00FFFFFF & rgb;
				// }
				else
				{
					return rgb;
				}
			}
		};
		return filter;
	}

	public void addObstacleSprite(String sprite,
								int width,
								int height,
								boolean requires_filter) throws IOException
	{
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter)
		{
			ImageFilter   filter 		  = filterTransparentForObstacle();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image 		  transparentImg  = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img	= transparentImg;
		}

		DataPool.getInstance().setObstacleSprite(img);
	}

	private ImageFilter filterTransparentForObstacle()
	{
		ImageFilter filter = new RGBImageFilter() 
		{
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb)
			{
				if (rgb == 0xffeeeeee || rgb == 0xffffffff || rgb == 0xffededed)
				{
					rgb = rgb & 0x00FFFFFF;
					return rgb;
				}
				if ((rgb | 0xFF000000) == transparentColor)
				{
					return 0x00FFFFFF & rgb;
				}
				else
				{
					return rgb;
				}
			}
		};

		return filter;
	}

	public void Start()
	{
		this.event_manager   = GameEventManager.CreateEventManager(this);
		this.instanceSpawner = new InstanceSpawner(this);
		Thread t1 = new Thread(this.instanceSpawner);   // Using the constructor Thread(Runnable r)  
		t1.start();
	}

	public void Update()
    {
		if (player.getPlayerHealth() > 0)
		{
			for(int i = 0; i < objects.size(); i++)
			{
				objects.get(i).update();
			}
			background.Update();
	
			// TODO: Encapsulate those procedures to something like state manager.
			boolean collides = CollisionCheck();
			if (!collides)
			{
				UpdateScore();
			}
			object_selector();
		}
	}

	private void object_selector()
	{
		if (objects.size() > 1)
		{
			GameObject obstacle = objects.get(current_obstacle_idx);
			if (player.xPos > obstacle.xPos + obstacle.width && new_spawned)
			{
				if ((current_obstacle_idx < objects.size() - 1))
				{
					current_obstacle_idx++;
					scoreManager.pause = false;
					physicsManager.pause = false;
				}
				else
				{
					scoreManager.pause = true;
					physicsManager.pause = true;
					new_spawned = false;
				}
			}
		}
	}

	private boolean CollisionCheck()
	{
		boolean collides = false;
		if (!physicsManager.pause)
		{
			GameObject object = objects.get(current_obstacle_idx);
			collides = physicsManager.isCollide(player, object);
			
			if (collides)
			{
				if (object instanceof Obstacle)
				{
					player.handle_clash((Obstacle)object);
				}
				else if (object instanceof Powerup)
				{
					player.handle_powerup((Powerup)object);
				}
				physicsManager.pause = true; // Collision check only occurs once for every obstacle
			}
		}
		return collides;
	}

	private boolean UpdateScore()
	{
		boolean performed = false;
		// Make sure there is the next obstacle in the objects container
		if (!scoreManager.pause)
		{
			GameObject object = objects.get(current_obstacle_idx);
			if (object instanceof Obstacle)
			{
				// Acknowledge the current obstacle as the target score trigger.
				scoreManager.setObstacle((Obstacle)objects.get(current_obstacle_idx));
				
				ScoreManager scoreManager = ScoreManager.getInstance();

				performed = scoreManager.PerformScore(scoreManager.score_powerup.getScoreMultiplier());
				
				if (performed)
				{
					scoreManager.pause = true;	// Wait until new obstacle object spawns.
				}
			}
		}

		return performed;
	}
	
	public void draw()
    {
		animationPane.paint();
	}

	public Background getBackground()
	{
		return this.background;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public Obstacle getObstacle()
	{
		return this.obstacle;
	}

	public AnimationPane getAnimationPane()
	{
		return animationPane;
	}

	public void setAnimationPane(AnimationPane _animationPane)
	{
		this.animationPane = _animationPane;
	}
	
	public void setPlayer(GameObject gameObject)
	{
		this.player = (Player)gameObject;
	}
	
	public void addObject(GameObject tempObject)
	{
		objects.add(tempObject);
	}
	
	public void removeObject(GameObject tempObject)
	{
		objects.remove(tempObject);
	}

	public int getNumberOfGameObjects()
	{
		return objects.size();
	}
}

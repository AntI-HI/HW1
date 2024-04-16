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
import java.util.LinkedList;
import javax.imageio.ImageIO;

public class GameManager
{
	private LinkedList<GameObject> deactiveObjects = new LinkedList<GameObject>();
	private LinkedList<GameObject> activeObjects = new LinkedList<GameObject>();

	public static int player_posX	= 240;
	public static int player_posY	= 550;
	public static int player_width	= 50;
	public static int player_height	= 80;

	public static int obstacle_width  = 100;
	public static int obstacle_height = 100;

	public static int powerup_width	  = 100;
	public static int powerup_height  = 100;

	public static int high_jump_width  = 100;
	public static int high_jump_height = 100;

	public static int low_jump_width  = 100;
	public static int low_jump_height = 100;

	private   Player 		   player;
	private   Background 	   background;
	public    ScoreManager     scoreManager;
	private	  AnimationPane    animationPane;
	public	  PhysicsManager   physicsManager;
	public    ObjectSelector   objectSelector;
	private   ObjectFactory	   objectFactory;
	public    GameEventManager event_manager;

	private UI_Elements ui;

	public int current_obstacle_idx = 1;

	private boolean paused;
	

	private static GameManager instance = null;

	public static GameManager getInstance()
	{
		return instance;
	}

    public GameManager() throws IOException
	{
		DataPool.CreateDataPool();
		instance = this;
	}
	
	private void Game_Manager_Early_Init()
	{
		try {
			scoreManager = ScoreManager.CreateScoreManager(this);
			
			this.event_manager  = GameEventManager.CreateEventManager(this);
			this.physicsManager = PhysicsManager.CreatePhysicsManagerInstance(this);
			this.objectSelector = new ObjectSelector(this);
			this.objectFactory	= new ObjectFactory(this);
			this.animationPane  = new AnimationPane(this);
			this.background     = new Background();
			this.ui 			= new UI_Elements(animationPane);

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
		}
		catch (IOException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
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

	private ImageFilter filterTransparentForPlayer()
	{
		ImageFilter filter = new RGBImageFilter()
		{
			public final int filterRGB(int x, int y, int rgb)
			{
				if (rgb == 0xFFF6F6F6)
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

	public void addHighJumpSprite(String sprite,
								int width,
								int height,
								boolean requires_filter) throws IOException
	{
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter)
		{
			ImageFilter   filter 		  = filterTransparentForHighJump();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image 		  transparentImg  = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img	= transparentImg;
		}

		DataPool.getInstance().setHighJumpSprite(img);
	}

	private ImageFilter filterTransparentForHighJump()
	{
		ImageFilter filter = new RGBImageFilter() 
		{
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb)
			{
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

	public void addLowJumpSprite(String sprite,
								int width,
								int height,
								boolean requires_filter) throws IOException
	{
		Image img = ImageIO.read(new File(sprite)).getScaledInstance(width, height, Image.SCALE_SMOOTH);

		if (requires_filter)
		{
			ImageFilter   filter 		  = filterTransparentForLowJump();
			ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
			Image 		  transparentImg  = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
			img	= transparentImg;
		}

		DataPool.getInstance().setLowJumpSprite(img);
	}

	private ImageFilter filterTransparentForLowJump()
	{
		ImageFilter filter = new RGBImageFilter() 
		{
			int transparentColor = 0xFF000000;

			public final int filterRGB(int x, int y, int rgb)
			{
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
		Game_Manager_Early_Init();
	}

	public void Update()
    {
		if (player.getPlayerHealth() > 0 && !paused)
		{
			for(int i = 0; i < activeObjects.size(); i++)
			{
				activeObjects.get(i).update();
			}
			Post_Update();
		}
	}
	
	private void Post_Update()
	{
		background.Update();
		physicsManager.CollisionCheck();
	}

	public void draw()
    {
		animationPane.paint();
	}

	public Background getBackground()
	{
		return background;
	}

	public Player getPlayer()
	{
		return player;
	}

	public UI_Elements getUi_Elements()
	{
		return ui;
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

	public void pause_game()
	{
		paused = !paused;
	}

	public void addActiveObject(GameObject object)
	{
		activeObjects.add(object);
	}

	public void removeActiveObject(GameObject object)
	{
		activeObjects.remove(object);
	}

	public void addDeactiveObject(GameObject object)
	{
		deactiveObjects.add(object);
	}

	public void removeDeactiveObject(GameObject object)
	{
		deactiveObjects.remove(object);
	}

	public int getNumberOfGameDeactiveObjects()
	{
		return deactiveObjects.size();
	}

	public int getNumberOfGameActiveObjects()
	{
		return activeObjects.size();
	}

	public GameObject getDeactiveObject(int i)
	{
		return deactiveObjects.get(i);
	}

	public GameObject getActiveObject(int i)
	{
		return activeObjects.get(i);
	}

	public LinkedList<GameObject> getActiveGameObjects()
	{
		return activeObjects;
	}
}

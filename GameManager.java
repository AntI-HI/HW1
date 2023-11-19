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

	private   Player 		  player;
	private   Obstacle        obstacle;
	private   Background 	  background;
	private	  AnimationPane   animationPane;
	private	  PhysicsManager  physicsManager;
	public    InstanceSpawner instanceSpawner;
	
    public GameManager() throws IOException
	{
		DataPool.CreateDataPool();

		this.animationPane  = new AnimationPane(this);
		this.physicsManager = PhysicsManager.CreatePhysicsManagerInstance();
		this.background     = new Background();

		addPlayerSprite(Game.playerSprite, player_width, player_height, true);
		addObstacleSprite(Game.obstacleSprite, obstacle_width, obstacle_height, true);
		AddObject(player_posX, player_posY, ObjectTypes.PLAYER);

		addBackgroundSprite(Game.background_img, Game.background_width, Game.background_height);
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
		DataPool dataPool   = DataPool.getInstance();
		Image img;
		GameObject obj;

		if (type == ObjectTypes.PLAYER)
		{
			img = dataPool.getPlayerSprite();
			int width  = img.getWidth(null);
			int height = img.getHeight(null);
			this.player = Player.createPlayerInstance(posX, posY, width, height);
			obj = this.player;
		}
		else
		{
			img = dataPool.getObstacleSprite();
			int width  = img.getWidth(null);
			int height = img.getHeight(null);
			this.obstacle = new Obstacle(posX, posY, width, height);
			obj = this.obstacle;
		}
		
		objects.add(obj);
	}

	// private void addPlayer(int posX, int posY)
	// {
	// 	DataPool dataPool   = DataPool.getInstance();
	// 	Image    player_img	= dataPool.getPlayerSprite();

	// 	int width  = player_img.getWidth(null);
	// 	int height = player_img.getHeight(null);

	// 	this.player = Player.createPlayerInstance(posX, posY, width, height);
	// 	objects.add(this.player);
	// }

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
		this.instanceSpawner = new InstanceSpawner(ObjectTypes.OBSTACLE, this);
		Thread t1 = new Thread(this.instanceSpawner);   // Using the constructor Thread(Runnable r)  
		t1.start();
	}

	public void Update()
    {		
		for(int i = 0; i < objects.size(); i++)
		{
			objects.get(i).update();
		}
		background.Update();
		physicsManager.CollisionCheck(player, objects);
	}
	
	public void draw()
    {
		animationPane.paint();
	}

	// public void addObstacle(int obstacle_width, int obstacle_height) throws IOException
	// {
	// 	this.obstacle = new Obstacle(1280, 550, obstacle_width, obstacle_height);

	// 	this.obstacle.img = ImageIO.read(new File(Game.obstacleSprite));

	// 	ImageFilter filter = new RGBImageFilter() {
	// 		int transparentColor = 0xFF000000;

	// 		public final int filterRGB(int x, int y, int rgb)
	// 		{
	// 			if (rgb == 0xffeeeeee || rgb == 0xffffffff || rgb == 0xffededed)
	// 			{
	// 				rgb = rgb & 0x00FFFFFF;
	// 				return rgb;
	// 			}
	// 			if ((rgb | 0xFF000000) == transparentColor)
	// 			{
	// 			  return 0x00FFFFFF & rgb;
	// 		   	}
	// 			else
	// 			{
	// 			  return rgb;
	// 		   	}
	// 		}
	// 	};

	// 	Image img = this.obstacle.img.getScaledInstance(obstacle_width, obstacle_height, Image.SCALE_SMOOTH);
	// 	img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
	// 	Image tmp = img.getScaledInstance(this.obstacle.width, this.obstacle.height, Image.SCALE_SMOOTH);
		
	// 	ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
	// 	Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
		
	// 	BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
	// 	tmp = transparentImg;

	// 	Graphics2D g2d = dimg.createGraphics();
	// 	g2d.drawImage(tmp, 0, 0, null);
	// 	g2d.dispose();

	// 	this.obstacle.img = dimg;

	// 	objects.add(this.obstacle);
	// }

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
}

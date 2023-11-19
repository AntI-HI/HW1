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

	private   Player 		  player;
	private   Obstacle        obstacle;
	private   Background 	  background;
	private	  AnimationPane   animationPane;
	private	  PhysicsManager  physicsManager;
	public   InstanceSpawner instanceSpawner;
	
    public GameManager(int init_player_posX, int init_player_posY,
						int width, int height, String sprite) throws IOException
						{
							
		DataPool.CreateDataPool();

		this.animationPane   = new AnimationPane(this);
		this.physicsManager  = PhysicsManager.CreatePhysicsManagerInstance();
		this.player 	     = Player.createPlayerInstance(init_player_posX, init_player_posY, width, height);
		this.background      = new Background();

		try
		{
			addBackgroundSprite(Game.background_img, Game.background_width, Game.background_height);
			addPlayerSprite(sprite, width, height);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
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

	public void addPlayerSprite(String player_sprite, int player_width, int player_height) throws IOException
	{
		this.player.img = ImageIO.read(new File(player_sprite));

		ImageFilter filter = new RGBImageFilter() {
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

		BufferedImage img = this.player.img;
		Image tmp = img.getScaledInstance(this.player.width, this.player.height, Image.SCALE_SMOOTH);

		ImageProducer filteredImgProd = new FilteredImageSource((tmp).getSource(), filter);
		Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);

		BufferedImage dimg = new BufferedImage(this.player.width, this.player.height, BufferedImage.TYPE_INT_ARGB);

		animationPane.setGraphics2d(dimg.createGraphics());
		animationPane.getGraphics2d().drawImage(transparentImg, 0, 0, null);
		animationPane.getGraphics2d().dispose();

		this.player.img = dimg;
		objects.add(this.player);
	}

	public void addObstacleSprite(String obstacle_sprite, int obstacle_width, int obstacle_height) throws IOException
	{
		this.obstacle = new Obstacle(600, 520, obstacle_width, obstacle_height);

		this.obstacle.img = ImageIO.read(new File(obstacle_sprite));

		ImageFilter filter = new RGBImageFilter() {
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

		Image img = this.obstacle.img.getScaledInstance(obstacle_width, obstacle_height, Image.SCALE_SMOOTH);
		img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		Image tmp = img.getScaledInstance(this.obstacle.width, this.obstacle.height, Image.SCALE_SMOOTH);
		
		ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
		Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
		
		BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		tmp = transparentImg;

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		this.obstacle.img = dimg;

		objects.add(this.obstacle);
	}

	public void Start()
	{
		this.instanceSpawner = new InstanceSpawner(ObjectTypes.OBSTACLE, this);
		// this.instanceSpawner.RandomInitializer();
		Thread t1 =new Thread(this.instanceSpawner);   // Using the constructor Thread(Runnable r)  
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

	public void addObstacle(int obstacle_width, int obstacle_height) throws IOException
	{
		this.obstacle = new Obstacle(1280, 550, obstacle_width, obstacle_height);

		this.obstacle.img = ImageIO.read(new File(Game.obstacleSprite));

		ImageFilter filter = new RGBImageFilter() {
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

		Image img = this.obstacle.img.getScaledInstance(obstacle_width, obstacle_height, Image.SCALE_SMOOTH);
		img = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		Image tmp = img.getScaledInstance(this.obstacle.width, this.obstacle.height, Image.SCALE_SMOOTH);
		
		ImageProducer filteredImgProd = new FilteredImageSource((img).getSource(), filter);
		Image transparentImg = Toolkit.getDefaultToolkit().createImage(filteredImgProd);
		
		BufferedImage dimg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		tmp = transparentImg;

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		this.obstacle.img = dimg;

		objects.add(this.obstacle);
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
}

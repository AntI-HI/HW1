import java.awt.*;
import java.awt.image.BufferedImage;

enum ObjectTypes
{
	PLAYER,
	OBSTACLE, POWERUP
}

public abstract class GameObject /* extends JPanel */
{
	protected int xPos, yPos, width, height;
	protected float velX, velY;
	protected Rectangle hitbox = new Rectangle();
	protected double boundpos = 0.03; // goes 3% into the sprite of the image
	protected BufferedImage img = null;
	protected Color B = new Color(0, 0, 0);

	protected Rectangle top = new Rectangle(), bottom = new Rectangle(), right = new Rectangle(), left = new Rectangle();
	protected DataPool dataPool;

	protected boolean collides = false;

	public GameObject(){}

	public GameObject(int _xPos, int _yPos, int _width, int _height)
	{
		setStart(_xPos, _yPos, _width, _height);
		setVelocity(1f, 0f);
		img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);

		hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
	}

	// for the basic 4 percent inside boundary
	public GameObject(int _xPos, int _yPos, BufferedImage img)
    {
		setStart(_xPos, _yPos, img.getWidth(), img.getHeight());
		setVelocity(1f, 0f);
		
		hitbox.setBounds((int)(_xPos + (width * boundpos)), (int)(_yPos + (height * boundpos)),
			        (int)(width * (boundpos * 2)),(int)(height * (boundpos * 2)));
	}
	
	// for custom boundaries
	public GameObject(int _xPos, int _yPos, BufferedImage img, double boundpos)
    {
		setStart(_xPos, _yPos, img.getWidth(), img.getHeight());
		setVelocity(1f, 0f);

		this.boundpos = boundpos;
		hitbox.setBounds((int)(_xPos + (width * boundpos)), (int)(_yPos + (height * boundpos)),
			        (int)(width * (boundpos * 2)),(int)(height * (boundpos * 2)));
	}
	
	private void setStart(int _xPos, int _yPos, int _width, int _height)
	{
		setPosition(_xPos, _yPos);
		setDimension(_width, _height);
		this.dataPool = DataPool.getInstance();
	}

	protected void setPosition(int x, int y)
	{
		this.xPos = x;
		this.yPos = y;
		hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));

	}

	private void setDimension(int _width, int _height)
	{
		this.width  = _width;
		this.height = _height;
	}

	private void setVelocity(float _velX, float _velY)
	{
		this.velX = _velX;
		this.velY = _velY;
	}

	public Rectangle getBounds() 
	{
		return new Rectangle(this.xPos, this.yPos, this.width, this.height);
	}

	public void set_event(boolean is_collide)
	{
		collides = is_collide;
	}

	public void setObjectPositionAndBounds(int _xPos, int _yPos, int _width, int _height)
	{
		setStart(_xPos, _yPos, _width, _height);
		setVelocity(1f, 0f);

		hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
	}

	protected void GameObjectCreateGraphics(int _width, int _height)
	{
		img = new BufferedImage(_width, _height, BufferedImage.TYPE_INT_ARGB);
	}
	
	public abstract void update();
	
	
	/* TODO: Try to perform drawing procedures for individual Game Object inside the Game Object class methods 
	and seperate the drawing process for AnimationPane. */
	

	// public abstract void draw();

	// @Override
    // public Dimension getPreferredSize()
    // {
    //     return this.img == null ? super.getPreferredSize() : new Dimension(this.img.getWidth() * 4, this.img.getHeight());
    // }

    // @Override
    // protected void paintComponent(Graphics g)
    // {
    //     super.paintComponent(g);

    //     // Player player = this.handler.getPlayer();
    //     // Obstacle obstacle = this.handler.getObstacle();
    //     // int y = getHeight() - this.img.getHeight();

    //     // Drawing two background to obtain scrolling background effect.
    //     // g.drawImage(background, xPos, y, this);
    //     // g.drawImage(background, xPos2, y, this);


    //     // g.drawImage(player.img, player.xPos, player.yPos, this);
    //     // g.drawImage(obstacle.img, xPos2, obstacle.yPos, this);
	// 	g.drawImage(this.img, this.xPos, this.yPos, this);
    // }
}

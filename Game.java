import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Dimension;
import javax.swing.JFrame;
import java.io.IOException;


public class Game extends JFrame implements KeyListener, Runnable
{
	// private static final long serialVersionUID = 1L;
	protected final static int GWIDTH 	= 1280;	/*(int) Toolkit.getDefaultToolkit().getScreenSize().getWidth()*/
	protected final static int GHEIGHT 	= 720;	/*(int) Toolkit.getDefaultToolkit().getScreenSize().getHeight()*/
	protected final static int SCALE 	= 72;

	protected final static double amountOfTicks = 60.0;

	protected 	static Thread  thread;
	private 	static boolean running;

	protected static Game 		 game;
	protected static GameManager game_manager;

	public static final String playerSprite   = "assets/player_sprite.png";
	public static final String obstacleSprite = "assets/obstacle.jpeg";
	public static final String powerupSprite  = "assets/powerup.png";
	public static final String background_img = "assets/background.gif";

	public static final int window_width  = 1280;
	public static final int window_height = 720;

	private static Game frame		   = null;
	private static int  instance_count = 0;

	public static double delta = 0;
	
	public static void main(String args[])
    {
		Create_Game_Instance();
		Create_Game_Manager();
		Set_Up_Frame();

		game.Start();
    }
	
	private void Start()
	{
		running = true;
		thread = new Thread(this);
		thread.start();
	}

	private static void Create_Game_Manager()
	{
		try
		{
			game_manager = new GameManager();
			game_manager.Start();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void Set_Up_Frame()
	{
		if (frame != null)
		{
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setLayout(new BorderLayout());

			frame.add(game_manager.getAnimationPane());
			frame.setPreferredSize(new Dimension(window_width, window_height));
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		}
	}

	private static Game Create_Game_Instance()
	{
		if (instance_count == 0)
		{
			frame = new Game();
			Game.game = frame;
			instance_count++;
			return frame;
		}
		else
		{
			return Game.game;
		}
	}


	public Game()
	{
		addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }
	

	public void keyPressed(KeyEvent evt) 
	{
		int key = evt.getKeyCode();
		Player player = game_manager.getPlayer();
		
		if (key == KeyEvent.VK_RIGHT)
		{
			if (!player.is_jumping())
			{
				player.setPlayerSpeed(20);
			}
		}
		else if ( key == KeyEvent.VK_UP)
		{
			if (!player.is_jumping())
			{
				player.ascend();
			}

		}
	}

	public void keyReleased(KeyEvent evt)
	{
		int key = evt.getKeyCode();
		Player player = game_manager.getPlayer();

		if(key == KeyEvent.VK_RIGHT)
		{
			player.setPlayerSpeed(0);
		}
	}

	public void keyTyped(KeyEvent arg0) {}

	// Calls the Game Manager's Update method and instantiates game logic to Game Manager.
	private void UpdateGlobal()
	{
		game_manager.Update();
		game_manager.draw();
	}

	@Override
	public void run()
	{
		this.requestFocus();
		long lastTime = System.nanoTime();
		double ns = 1000000000 / amountOfTicks;
		
		long timer = System.currentTimeMillis();

		while (running)
		{
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;

			while (delta >= 1)
			{
				UpdateGlobal();
				delta--;
			}

			if (System.currentTimeMillis() - timer > 1000)
			{
				timer += 1000;
			}
		}
		stop();
	}

	public void stop()
	{
		System.exit(0);
	}
}

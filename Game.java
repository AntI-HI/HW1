import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.IOException;

public class Game extends JFrame implements Runnable
{
	protected final static int GWIDTH 	= 1280;
	protected final static int GHEIGHT 	= 720;
	protected final static int SCALE 	= 72;

	protected final static double amountOfTicks = 60.0;

	protected 	static Thread  thread;
	private 	static boolean running;

	protected static GameManager game_manager;

	public static final String playerSprite     = "assets/player_sprite.png";
	public static final String obstacleSprite   = "assets/obstacle.jpeg";
	public static final String powerupSprite    = "assets/powerup.png";
	public static final String background_img   = "assets/background.gif";
	public static final String high_jump_sprite = "assets/high_jump.png";
	public static final String low_jump_sprite  = "assets/low_jump.png";

	public static final int window_width  = 1280;
	public static final int window_height = 720;

	private static Game   frame		   = null;
	private static JPanel mainPanel	   = null;
	private static UI_Elements   ui = null;
	private static int  instance_count = 0;

	public static double delta = 0;
	
	public static void main(String args[])
    {
		Create_Game_Instance();
		Create_Game_Manager();
		Set_Up_Frame();
		Set_Up_UI_Elements();

		frame.Start();
    }

	private void Start()
	{
		InputManager input_manager = new InputManager(game_manager);
		this.addKeyListener(input_manager);
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

	private static void Set_Up_UI_Elements()
	{
		ui = game_manager.getUi_Elements();
		ui.initLabels();
		ui.initButtons();
	}

	private static void Set_Up_Frame()
	{
		if (frame != null)
		{
			mainPanel = game_manager.getAnimationPane();

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setPreferredSize(new Dimension(window_width, window_height));

			frame.getContentPane().add(mainPanel);
			
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
			instance_count++;
			return frame;
		}
		else
		{
			return Game.frame;
		}
	}

	public static JFrame getFrame()
	{
		return frame;
	}

	public Game()
	{
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

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

	public static void stop()
	{
		System.exit(0);
	}
}

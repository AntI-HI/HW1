import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputManager implements KeyListener
{
    private GameManager game_manager;

    public InputManager(GameManager game_manager)
    {
        this.game_manager = game_manager;
	}

    @Override
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

    @Override
	public void keyReleased(KeyEvent evt)
	{
		int key = evt.getKeyCode();
		Player player = game_manager.getPlayer();

		if(key == KeyEvent.VK_RIGHT)
		{
			player.setPlayerSpeed(0);
		}
	}

    @Override
	public void keyTyped(KeyEvent arg0) {}
    
}

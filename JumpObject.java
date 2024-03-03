import java.awt.Graphics2D;
import java.awt.Image;

public class JumpObject extends GameObject 
{
    // private Player player;
    // private PhysicsManager physics_manager;
    public JumpStrategy jump_strategy;
    
    public static GameObject Create(int pos_x, int pos_y, JumpStrategy jumpStrategy)
    {
        DataPool   dataPool = DataPool.getInstance();
		Image 	   img      = dataPool.getObstacleSprite();
		GameObject object;
        int        width;
        int        height;

        width  = img.getWidth(null);
        height = img.getHeight(null);
        object = new JumpObject(pos_x, pos_y, width, height, jumpStrategy);
        
        return object;
    }

    public JumpObject(int _xPos, int _yPos, int _width, int _height, JumpStrategy jumpStrategy)
    {
        super(_xPos, _yPos, _width, _height);
        jump_strategy = jumpStrategy;

        Graphics2D g2d = img.createGraphics();
        if (jumpStrategy instanceof LowJump)
        {
            g2d.drawImage(dataPool.getLowJumpSprite(), 0, 0, null);
        }
        else if (jumpStrategy instanceof HighJump)
        {
            g2d.drawImage(dataPool.getHighJumpSprite(), 0, 0, null);
        }
        g2d.dispose();
        // player = Player.getPlayerInstance();
        // player.set_initial_jump_speed(speed);
        // physics_manager = PhysicsManager.getInstance();
    }

    // @Override
    // public void Jump()
    // {
    //     // System.out.println("LOW JUMP");
        
    //     if (player.is_jumping())
    //     {
    //         if (player.is_first_jump())
    //         {
    //             player.set_curr_vertical_speed(player.get_initial_jump_speed());
    //             player.set_first_jump(false);
    //         }
    
    //         double jump_delta = player.get_curr_vertical_speed();
    //         if (player.is_ascending())
    //         {
    //             player.yPos -= jump_delta;
    //             player.set_curr_vertical_speed(player.get_curr_vertical_speed() - (int)physics_manager.GRAVITY);
    //         }
    //         else if (player.is_descending())
    //         {
    //             player.yPos += jump_delta;
    //             player.set_curr_vertical_speed(player.get_curr_vertical_speed() + (int)physics_manager.GRAVITY);
    //         }

    //         if (player.get_curr_vertical_speed() <= 0 && player.is_ascending())
    //         {
    //             player.set_curr_vertical_speed(0);

    //             player.set_is_ascending(false);
    //             player.set_is_descending(true);
    //         }

    //         if (player.yPos >= GameManager.player_posY)
    //         {
    //             player.set_is_jumping(false);
    //             player.set_is_ascending(false);
    //             player.set_is_descending(false);
    //         }
    //     }
    // }

    @Override
    public void update()
    {
        int playerSpeed = dataPool.getSpeed();
        xPos -= playerSpeed;
        hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
    }
}

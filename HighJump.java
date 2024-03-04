public class HighJump implements JumpStrategy 
{
    private Player player;
    private final int speed = 25;
    private PhysicsManager physics_manager;

    public static HighJump Create()
    {
        return new HighJump();
    }

    public HighJump()
    {
        player = Player.getPlayerInstance();
        physics_manager = PhysicsManager.getInstance();
    }

    @Override
    public void Jump()
    {
        if (player.is_jumping())
        {
            if (player.is_first_jump())
            {
                player.set_curr_vertical_speed(speed);
                player.set_first_jump(false);
            }
    
            double jump_delta = player.get_curr_vertical_speed();
            if (player.is_ascending())
            {
                player.yPos -= jump_delta;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() - (int)physics_manager.GRAVITY);
            }
            else if (player.is_descending())
            {
                player.yPos += jump_delta;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() + (int)physics_manager.GRAVITY);
            }

            if (player.get_curr_vertical_speed() <= 0 && player.is_ascending())
            {
                player.set_curr_vertical_speed(0);

                player.set_is_ascending(false);
                player.set_is_descending(true);
            }

            if (player.yPos >= GameManager.player_posY)
            {
                player.set_is_jumping(false);
                player.set_is_ascending(false);
                player.set_is_descending(false);
            }
        }
    }
}

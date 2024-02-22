public class LowJump implements JumpStrategy 
{
    private Player player;
    private final int speed = 18;

    public LowJump()
    {
        player = Player.getPlayerInstance();
        player.set_initial_jump_speed(speed);
    }

    @Override
    public void Jump()
    {
        // System.out.println("LOW JUMP");
        
        if (player.is_jumping())
        {
            if (player.is_first_jump())
            {
                player.set_curr_vertical_speed(player.get_initial_jump_speed());
                player.set_first_jump(false);
            }
    
            double jump_delta = player.get_curr_vertical_speed();
            if (player.is_ascending())
            {
                player.yPos -= jump_delta;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() - (int)GameManager.GRAVITY);
            }
            else if (player.is_descending())
            {
                player.yPos += jump_delta;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() + (int)GameManager.GRAVITY);
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

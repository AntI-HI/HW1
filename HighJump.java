public class HighJump implements JumpStrategy 
{
    private Player player;

    public HighJump()
    {
        // player = Player.getPlayerInstance();
    }

    @Override
    public void Jump()
    {
        System.out.println("High Jump!");

        player = Player.getPlayerInstance();
        player.set_initial_jump_speed(25);

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
                // curr_vertical_speed -= GameHandler.GRAVITY;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() - (int)GameManager.GRAVITY);
            }
            else if (player.is_descending())
            {
                player.yPos += jump_delta;
                // curr_vertical_speed += GameHandler.GRAVITY;
                player.set_curr_vertical_speed(player.get_curr_vertical_speed() + (int)GameManager.GRAVITY);

            }

            if (player.get_curr_vertical_speed() <= 0 && player.is_ascending())
            {
                player.set_curr_vertical_speed(0);

                player.set_is_ascending(false);
                player.set_is_descending(true);
            }

            if (player.yPos >= Game.player_posY)
            {
                player.set_is_jumping(false);
                player.set_is_ascending(false);
                player.set_is_descending(false);
            }
        }
    }
}

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Player extends GameObject
{
    private JumpStrategy jump_strategy;

    private int     initial_jump_speed;
    private int     curr_vertical_speed;
    private int     playerSpeed;
    private boolean is_jumping;
    private boolean first_jump;
    private boolean is_ascending;
    private boolean is_descending;
    private boolean is_dead;

    private int player_health;

    public static Player player_instance = null;

    public static Player createPlayerInstance(int _xPos, int _yPos, int width, int height)
    {
        if (player_instance == null)
        {
            player_instance = new Player(_xPos, _yPos, width, height);
            player_instance.jump_strategy = new LowJump();
        }

        return player_instance;
    }

    public static Player getPlayerInstance()
    {
        return player_instance;
    }

    public Player(int _xPos, int _yPos, int width, int height)
    {
        super(_xPos, _yPos, width, height);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getPlayerSprite(), 0, 0, null);
        g2d.dispose();

        is_dead = false;
        playerSpeed = 0;
        first_jump = true;
        is_jumping  = false;
        player_health = 3;

        player_instance = this;
    }

    @Override
    public void update()
    {
       if (is_jumping())
        {
            jump();
        }
    }

    public void jump()
    {
        jump_strategy.Jump();
    }

    public JumpStrategy getJumpStrategy()
    {
        return this.jump_strategy;
    }

    public Boolean isDead()
    {
        return this.is_dead;
    }

    public void setJumpStrategy(JumpStrategy _jump_strategy)
    {
        this.jump_strategy = _jump_strategy;
    }

    public void Die()
    {
        this.is_dead = true;
    }

    public int getPlayerSpeed()
    {
        return this.playerSpeed;
    }

    public void setPlayerSpeed(int speed)
    {
        this.playerSpeed = speed;
        dataPool.setSpeed(speed);
    }

    public int getPlayerHealth()
    {
        return this.player_health;
    }

    public void ascend()
    {
        is_jumping  = true;
        is_ascending = true;
        first_jump = true; 
    }

    public boolean is_jumping()
    {
        return is_jumping;
    }

    public boolean is_first_jump()
    {
        return first_jump;
    }

    public boolean is_ascending()
    {
        return is_ascending;
    }

    public boolean is_descending()
    {
        return is_descending;
    }
    
    public int get_initial_jump_speed()
    {
        return initial_jump_speed;
    }

    public int get_curr_vertical_speed()
    {
        return curr_vertical_speed;
    }

    public void set_curr_vertical_speed(int speed)
    {
        curr_vertical_speed = speed;
    }

    public void set_first_jump(boolean jump)
    {
        first_jump = jump;
    }

    public void set_is_ascending(boolean ascending)
    {
        is_ascending = ascending;
    }

    public void set_is_descending(boolean descending)
    {
        is_descending = descending;
    }

    public void set_is_jumping(boolean jumping)
    {
        is_jumping = jumping;
    }

    public void set_initial_jump_speed(int speed)
    {
        initial_jump_speed = speed;
    }

    public void handle_powerup(Powerup scorePowerup)
    {
        ScorePowerup _scorePowerup;
        ScoreManager scoreManager = ScoreManager.getInstance();
        if (scoreManager.score_powerup == null)
        {
            _scorePowerup = new ScorePowerup();
			scoreManager.setScorePowerup(_scorePowerup);
        }
        else
        {
            scoreManager.score_powerup = new X2Powerup(scoreManager.score_powerup);
        }
    }

    public void handle_clash(Obstacle object)
    {
        if (player_health > 0)
        {
            player_health--;
        }
        if (player_health == 0)
        {
            System.out.println("You Died!");

        }
    }
}

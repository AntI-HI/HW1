import java.awt.Image;

public class DataPool
{
    private Image player_sprite;
    private Image obstacle_sprite;
    private Image powerup_sprite;
    private Image high_jump_sprite;
    private Image low_jump_sprite;
    
    private HighJump high_jump;
    
    private LowJump  low_jump;
    
    private int speed;
    
    private static DataPool instance = null;
    
    public static DataPool CreateDataPool ()
    {
        if (instance == null)
        {
            instance = new DataPool();
        }
        
        return instance;
    }
    
    public static DataPool getInstance()
    {
        return instance;
    }
    
    private DataPool()
    {
        speed = 0;
    }
    
    public HighJump getHigh_jump() {
        return high_jump;
    }
    
    public LowJump getLow_jump() {
        return low_jump;
    }
    
    public void setHigh_jump(HighJump high_jump) {
        this.high_jump = high_jump;
    }

    public void setLow_jump(LowJump low_jump) {
        this.low_jump = low_jump;
    }

    public void setPlayerSprite(Image sprite)
    {
        player_sprite = sprite;
    }

    public void setObstacleSprite(Image sprite)
    {
        obstacle_sprite = sprite;
    }

    public void setPowerupSprite(Image sprite)
    {
        powerup_sprite = sprite;
    }

    public void setLowJumpSprite(Image sprite)
    {
        low_jump_sprite = sprite;
    }

    public void setHighJumpSprite(Image sprite)
    {
        high_jump_sprite = sprite;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public Image getPlayerSprite()
    {
        return player_sprite;
    }

    public Image getObstacleSprite()
    {
        return obstacle_sprite;
    }
    public Image getPowerupSprite()
    {
        return powerup_sprite;
    }

    public Image getHighJumpSprite()
    {
        return high_jump_sprite;
    }

    public Image getLowJumpSprite()
    {
        return low_jump_sprite;
    }

    public int getSpeed()
    {
        return speed;
    }
}
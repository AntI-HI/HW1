import java.awt.Image;

public class DataPool
{
    private Image player_sprite;
    private Image obstacle_sprite;
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

    public void setPlayerSprite(Image sprite)
    {
        player_sprite = sprite;
    }

    public void setObstacleSprite(Image sprite)
    {
        obstacle_sprite = sprite;
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

    public int getSpeed()
    {
        return speed;
    }
}
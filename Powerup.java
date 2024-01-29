import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

public abstract class Powerup extends GameObject
{
    int score_multiplier = 1;
    int jump_mode = 0;

    public Powerup()
    {
        super();
        this.dataPool = DataPool.getInstance();

        if (img != null)
        {
            Image image = this.dataPool.getPowerupSprite();
            int width  = image.getWidth(null);
            int height = image.getHeight(null);

            GameObjectCreateGraphics(width, height);
            
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
            g2d.dispose();
        }
    }

    public void setPosition(int _xPos, int _yPos, int _width, int _height)
    {
        // super(_xPos, _yPos, _width, _height);

        // setRandomScoreMultiplier();
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
        g2d.dispose();
    }

    // public abstract int getScoreMultiplier();

    // private void setRandomScoreMultiplier()
    // {
    //     Random r = new Random();

    //     int result = (int)Math.abs(r.nextLong() % 3);

    //     if (result == 0)
    //     {
    //         score_multiplier = 2;
    //     }
    //     else if (result == 1)
    //     {
    //         score_multiplier = 4;
    //     }
    //     else
    //     {
    //         score_multiplier = 8;
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

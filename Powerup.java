import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;

public class Powerup extends GameObject
{
    int jump_mode = 0;

    public ScorePowerup score_powerup = null;

    public Powerup()
    {
        super();
        this.dataPool = DataPool.getInstance();

        if (img == null)
        {
            Image image = this.dataPool.getPowerupSprite();
            int width  = image.getWidth(null);
            int height = image.getHeight(null);

            GameObjectCreateGraphics(width, height);
            
            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
            g2d.dispose();
        }
        score_powerup = new ScorePowerup();
        CreateRandomScoreMultiplierPowerup();
    }
    
    public ScorePowerup getScorePowerup()
    {
        return score_powerup;
    }

    public void setPosition(int _xPos, int _yPos, int _width, int _height)
    {
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
        g2d.dispose();
    }


    private void CreateRandomScoreMultiplierPowerup()
    {
        Random r = new Random();

        int result = (int)Math.abs(r.nextLong() % 2);

        if (result == 0)
        {
            score_powerup = new X2Powerup(score_powerup);
        }
        else
        {
            score_powerup = new X5Powerup(score_powerup);
        }
    }

    @Override
    public void update()
    {
        int playerSpeed = dataPool.getSpeed();
        xPos -= playerSpeed;
        hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
    }
}

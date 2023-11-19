import java.awt.Graphics2D;

public class Obstacle extends GameObject
{
    public Obstacle(int _xPos, int _yPos, int _width, int _height)
    {
        super(_xPos, _yPos, _width, _height);

        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getObstacleSprite(), 0, 0, null);
        g2d.dispose();
    }

    @Override
    public void update()
    {
        int playerSpeed = dataPool.getSpeed();
        xPos -= playerSpeed;
        hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
    }
}

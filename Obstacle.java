import java.awt.Graphics2D;

public class Obstacle extends GameObject
{
    public Obstacle(int _xPos, int _yPos, int _width, int _height)
    {
        super(_xPos, _yPos, _width, _height);
    }

    @Override
    public void update()
    {
        int playerSpeed = dataPool.getSpeed();
        xPos -= playerSpeed;
        hitbox.setBounds((int)(xPos), (int)(yPos), (int)(width),(int)(height));
    }
}

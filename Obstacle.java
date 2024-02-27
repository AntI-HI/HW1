import java.awt.Graphics2D;
import java.awt.Image;

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

    public static GameObject Create(int pos_x, int pos_y)
    {
        DataPool   dataPool = DataPool.getInstance();
		Image 	   img = dataPool.getObstacleSprite();
		GameObject obstacle;
        int        width;
        int        height;

        width  = img.getWidth(null);
        height = img.getHeight(null);
        obstacle = new Obstacle(pos_x, pos_y, width, height);

        return obstacle;
    }
}

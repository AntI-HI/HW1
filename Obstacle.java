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

    public static GameObject Create()
    {
        // handler.AddObject(1280, 550, object_type);

        DataPool   dataPool = DataPool.getInstance();
		Image 	   img = dataPool.getObstacleSprite();
		GameObject obstacle;
        int        width;
        int        height;

        img = dataPool.getObstacleSprite();
        width  = img.getWidth(null);
        height = img.getHeight(null);
        obstacle = new Obstacle(1280, 550, width, height);
        // obj = this.obstacle;
        // new_spawned = true;				// Means the new object is created. It used for waking the object selector.
        // if (objects.size() == 1)
        // {
        //     scoreManager.pause = false;	  // Unpause the score calculation functionality for the first obstacle spawn.
        //     physicsManager.pause = false; // Unpause the physics calculation functionality for the first obstacle spawn.
    
        // }
        return obstacle;
    }
}

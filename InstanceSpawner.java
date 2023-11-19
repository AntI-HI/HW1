import java.io.IOException;
import java.util.Random;

public class InstanceSpawner implements Runnable
{
    private ObjectTypes object_type;
    private GameManager handler;

    public InstanceSpawner(ObjectTypes type, GameManager handler)
    {
        this.object_type = type;
        this.handler     = handler;
    }
    
    @Override
    public void run()
    {
        try {
            RandomInitializer();
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    public void setObjectToSpawn(ObjectTypes type)
    {
        object_type = type;
    }

    public void InstantiateObject() throws IOException
    {
        if (object_type == ObjectTypes.OBSTACLE)
        {
            handler.addObstacle(Game.obstacle_width, Game.obstacle_height);
        }            
    }

    public void RandomInitializer() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        while(true)
        {
            Thread.sleep(Math.abs(r.nextLong() % 1)); // wait a random time up to 1 second
            
            if (dataPool.getSpeed() > 0)
            {
                InstantiateObject();
            }
        }
    }


}

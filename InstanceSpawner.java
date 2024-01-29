import java.io.IOException;
import java.util.Random;

public class InstanceSpawner implements Runnable
{
    private GameManager handler;
    private final int max_wait_ms = 4000;   // maximum spawn time between objects in milliseconds.
    private boolean spawn = true;

    public InstanceSpawner(GameManager handler)
    {
        this.handler     = handler;
    }
    
    @Override
    public void run()
    {
        try {
            RandomObjectInitializer();
        } catch (InterruptedException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
    }

    public void InstantiateObject(ObjectTypes object_type) throws IOException
    {
        handler.AddObject(1280, 550, object_type);
    }

    public void RandomObjectInitializer() throws InterruptedException, IOException
    {
        Random r = new Random();

        while (spawn)
        {
            long result = Math.abs(r.nextLong() % 2); // wait a random time up to 1 second
            if (result == 0)
            {
                CreateObstacle();
            }
            else
            {
                CreatePowerup();
            }
        }
    }

    public void CreateObstacle() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0)
        {
            InstantiateObject(ObjectTypes.OBSTACLE);
        }
    }

    public void CreatePowerup() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0)
        {
            InstantiateObject(ObjectTypes.POWERUP);
        }
    }
}

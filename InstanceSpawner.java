import java.io.IOException;
import java.util.Random;

public class InstanceSpawner implements Runnable
{
    private ObjectTypes object_type;
    private GameManager handler;
    private final int max_wait_ms = 4000;   // maximum spawn time between objects in milliseconds.

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
            handler.AddObject(1280, 550, object_type);
        }            
    }

    public void RandomInitializer() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        while(true)
        {
            Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
            if (dataPool.getSpeed() > 0)
            {
                InstantiateObject();
            }
        }
    }


}

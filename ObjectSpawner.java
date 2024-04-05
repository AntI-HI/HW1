import java.io.IOException;
import java.util.Random;

public class ObjectSpawner implements Runnable
{
    private GameManager game_manager;
    private final int max_wait_ms = 4000;   // maximum spawn time between objects in milliseconds.
    private boolean spawn = true;

    private int spawn_pos_x = 1280;
    private int spawn_pos_y = 550;

    private final int number_of_object_types = 4;

    public ObjectSpawner(GameManager _game_manager)
    {
        this.game_manager = _game_manager;
    }
    
    @Override
    public void run()
    {
        try {
            RandomObjectInitializer();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }        
    }

    public void RandomObjectInitializer() throws InterruptedException, IOException
    {
        Random r = new Random();

        while (spawn)
        {
            long result = Math.abs(r.nextLong() % number_of_object_types); // wait a random time up to 1 second
            if (result == 0)
            {
                CreateObstacle();
            }
            else if (result == 1)
            {
                CreatePowerup();
            }
            else if (result == 2)
            {
                CreateLowJump();
            }
            else if (result == 3)
            {
                CreateHighJump();
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
            GameObject obstacle = Obstacle.Create(spawn_pos_x, spawn_pos_y);
            GameEventManager.getInstance().handle_event(obstacle);
            game_manager.addObject(obstacle);
        }
    }

    public void CreatePowerup() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0)
        {
            GameObject powerup = Powerup.Create(spawn_pos_x, spawn_pos_y);
            GameEventManager.getInstance().handle_event(powerup);
            game_manager.addObject(powerup);
        }
    }

    public void CreateHighJump() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0)
        {
            GameObject object = JumpObject.Create(spawn_pos_x, spawn_pos_y, dataPool.getHigh_jump());
            GameEventManager.getInstance().handle_event(object);
            game_manager.addObject(object);
        }
    }

    public void CreateLowJump() throws InterruptedException, IOException
    {
        Random r = new Random();
        DataPool dataPool = DataPool.getInstance();

        Thread.sleep(Math.abs(r.nextLong() % max_wait_ms)); // wait a random time up to 1 second
        if (dataPool.getSpeed() > 0)
        {
            GameObject object = JumpObject.Create(spawn_pos_x, spawn_pos_y, dataPool.getLow_jump());
            GameEventManager.getInstance().handle_event(object);
            game_manager.addObject(object);
        }
    }

    public void CreatePlayer(UI_Elements ui, int pos_x, int pos_y) throws InterruptedException, IOException
    {
        GameObject player = Player.Create(ui, pos_x, pos_y);
        GameEventManager.getInstance().handle_event(player);
        game_manager.addObject(player);
    }
}

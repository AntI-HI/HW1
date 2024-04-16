import java.io.IOException;

public class ObjectFactory 
{
    private GameManager game_manager;

    private int spawn_pos_x = 9999;
    private int spawn_pos_y = 9999;
    
    public ObjectFactory(GameManager game_manager)
    {
        this.game_manager   = game_manager;
    }
    
    public void fillObjectPool() throws InterruptedException, IOException
    {
        CreatePlayer();
        for(int i = 0; i < 10; i++)
        {
            CreateObstacle();
        }
        for(int i = 0; i < 10; i++)
        {
            CreatePowerup();
        }
        for(int i = 0; i < 10; i++)
        {
            CreateLowJump();
        }
        for(int i = 0; i < 10; i++)
        {
            CreateHighJump();
        }
    }

    public void CreatePlayer()
    {
        GameObject player = Player.Create(GameManager.player_posX, GameManager.player_posY);
        GameEventManager.getInstance().handle_event(player);
        game_manager.addActiveObject(player);
    }

    private void CreateHighJump()
    {
        GameObject highjump = JumpObject.Create(spawn_pos_x, spawn_pos_y, DataPool.getInstance().getHigh_jump());
        game_manager.addDeactiveObject(highjump);
    }

    private void CreateLowJump()
    {
        GameObject lowjump = JumpObject.Create(spawn_pos_x, spawn_pos_y, DataPool.getInstance().getLow_jump());
        game_manager.addDeactiveObject(lowjump);
    }

    private void CreatePowerup()
    {
        GameObject powerup = Powerup.Create(spawn_pos_x, spawn_pos_y);
        game_manager.addDeactiveObject(powerup);
    }

    private void CreateObstacle()
    {
        GameObject obstacle = Obstacle.Create(spawn_pos_x, spawn_pos_y);
        game_manager.addDeactiveObject(obstacle);
    }
}

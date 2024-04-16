public class GameEventManager
{
    public static GameEventManager instance;

    private GameManager game_manager;
    GameObject obj;

    public static GameEventManager CreateEventManager(GameManager _game_manager)
    {
        if (instance == null)
        {
            instance = new GameEventManager(_game_manager);
        }

        return instance;
    }

    public static GameEventManager getInstance()
    {
        return instance;
    }

    private GameEventManager(GameManager _game_manager)
    {
        game_manager = _game_manager;
    }

    public void handle_event(GameObject obj)
    {
        if (obj instanceof Player)
        {
            game_manager.setPlayer((Player)obj);
            ScoreManager.getInstance().setPlayer((Player)obj);
            DataPool dataPool = DataPool.getInstance();
            dataPool.setHigh_jump(new HighJump());
            dataPool.setLow_jump(new LowJump());
        }
        else
        {
            game_manager.new_spawned = true;
        }
        obj.isActive = true;
    }

    public void PauseGame()
    {
        game_manager.pause_game();
    }
}

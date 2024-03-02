import java.util.LinkedList;

public class PhysicsManager
{
    private static PhysicsManager instance = null;
    private GameManager game_manager = null;

	public final double GRAVITY = 1f;

    public boolean pause = true;

    public static PhysicsManager CreatePhysicsManagerInstance(GameManager game_manager)
    {
        if (instance == null)
        {
            instance = new PhysicsManager(game_manager);
        }
        return instance;
    }

    public static PhysicsManager getInstance()
    {
        return instance;
    }

    private PhysicsManager(GameManager game_manager)
    {
        this.game_manager = game_manager;
    }

    public boolean isCollide(GameObject source, GameObject target)
    {
        boolean is_collide = false;

        if(source.getBounds().intersects(target.hitbox)) 
        {
            is_collide = true;
            target.collides = true;
            if (target instanceof Obstacle)
            {
                System.out.println("Collided with obstacle");
            }
            else
            {
                System.out.println("Collided with powerup");
            }
        }
        // TODO: Think about using event management properly. Maybe creating a new class named Event Manager.
        source.set_event(is_collide);   

        return is_collide;
    }

    public boolean CollisionCheck()
    {
        boolean collides = false;
        Player player = game_manager.getPlayer();

        if (!pause)
		{
			GameObject object = game_manager.getGameObject(game_manager.current_obstacle_idx);
			collides = isCollide(player, object);
			
			if (collides)
			{
				if (object instanceof Obstacle)
				{
					player.handle_clash((Obstacle)object);
				}
				else if (object instanceof Powerup)
				{
					player.handle_powerup((Powerup)object);
				}
				pause = true; // Collision check only occurs once for every obstacle
			}
		}

        return collides;
    }
}

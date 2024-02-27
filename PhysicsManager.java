import java.util.LinkedList;

public class PhysicsManager
{
    private static PhysicsManager instance = null;

    // private long current_time;
    // private long last_update_time;

    public boolean pause = true;

    public static PhysicsManager CreatePhysicsManagerInstance()
    {
        if (instance == null)
        {
            instance = new PhysicsManager();
        }
        return instance;
    }

    public static PhysicsManager getInstance()
    {
        return instance;
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
}

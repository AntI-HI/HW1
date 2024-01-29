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

    // TODO: This dead code will be removed.
    // public void CollisionCheck(GameObject source, LinkedList<GameObject> objects)
    // {
    //     boolean is_collide = false;

    //     current_time = System.currentTimeMillis();
    //     if (current_time - last_update_time > 1000)
    //     {
    //         for(int i = 1; i < objects.size(); i++)
    //         {
    //             GameObject tempObject = objects.get(i);
    //             if(source.getBounds().intersects(tempObject.hitbox)) 
    //             {
    //                 last_update_time = System.currentTimeMillis();
    //                 is_collide = true;
    //                 System.out.println("Collision Happened");
    //                 break;
    //             }
    //         }
    //         source.set_event(is_collide);
    //     }
    // }

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

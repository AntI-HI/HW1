import java.util.LinkedList;

public class PhysicsManager
{
    private static PhysicsManager instance = null;

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

    private PhysicsManager() {}


    public void CollisionCheck(GameObject source, LinkedList<GameObject> objects)
    {
        boolean is_collide = false;

        for(int i = 1; i < objects.size(); i++)
		{
			GameObject tempObject = objects.get(i);
			if(source.getBounds().intersects(tempObject.hitbox)) 
			{
                is_collide = true;
				break;
			}
		}
		source.set_event(is_collide);
    }
}

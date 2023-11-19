public class DataPool
{
    private int speed;

    private static DataPool instance = null;

    public static DataPool CreateDataPool ()
    {
        if (instance == null)
        {
            instance = new DataPool();
        }

        return instance;
    }

    public static DataPool getInstance()
    {
        return instance;
    }

    private DataPool()
    {
        speed = 0;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getSpeed()
    {
        return speed;
    }
}
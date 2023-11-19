import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

public class Background
{
    private BufferedImage img;
    private int xPos, yPos;
    private int xPos_cont;
    private DataPool dataPool;

    public Background()
    {
        dataPool = DataPool.getInstance();
    }

    public Background(BufferedImage _img)
    {
        setBackgroundImage(_img);
        setCoordinates(0, 0);
    }

    public Background(BufferedImage _img, int _xPos, int _yPos)
    {
        setBackgroundImage(_img);
        setCoordinates(_xPos, _yPos);
    }

    public void setBackgroundImage(BufferedImage img)
    {
        this.img = img;
    }

    public void setCoordinates(int xPos, int yPos)
    {
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void setXPos(int xPos)
    {
        this.xPos = xPos;
    }

    public void setYPos(int yPos)
    {
        this.yPos = yPos;
    }
    
    public BufferedImage getBackgroundImage()
    {
        return img;
    }

    public ArrayList<Integer> getCoordinates()
    {
        ArrayList<Integer> coords = new ArrayList<Integer>();

        coords.add(xPos);
        coords.add(yPos);

        return coords;
    }

    public int getXPosCont()
    {
        return this.xPos_cont;
    }

    public void Update()
    {
        int scroll_speed = dataPool.getSpeed();
        xPos -= scroll_speed;

        if (xPos <= -this.img.getWidth())
        {
            xPos = 0;
        }
    
        xPos_cont = xPos + this.img.getWidth();
    }
}

import java.awt.Graphics2D;
import java.awt.Image;

public class JumpObject extends GameObject 
{
    public JumpStrategy jump_strategy;
    
    public static GameObject Create(int pos_x, int pos_y, JumpStrategy jumpStrategy)
    {
        DataPool   dataPool = DataPool.getInstance();
        Image img;
		GameObject object;
        int        width;
        int        height;
        
        if (jumpStrategy instanceof HighJump)
        {
            img = dataPool.getHighJumpSprite();
        }
        else
        {
            img = dataPool.getLowJumpSprite();
        }

        width  = img.getWidth(null);
        height = img.getHeight(null);

        object = new JumpObject(pos_x, pos_y, width, height, jumpStrategy);
        
        return object;
    }

    public JumpObject(int _xPos, int _yPos, int _width, int _height, JumpStrategy jumpStrategy)
    {
        super(_xPos, _yPos, _width, _height);
        jump_strategy = jumpStrategy;

        Graphics2D g2d = img.createGraphics();
        if (jumpStrategy instanceof LowJump)
        {
            g2d.drawImage(dataPool.getLowJumpSprite(), 0, 0, null);
        }
        else if (jumpStrategy instanceof HighJump)
        {
            g2d.drawImage(dataPool.getHighJumpSprite(), 0, 0, null);
        }
        g2d.dispose();
    }
}

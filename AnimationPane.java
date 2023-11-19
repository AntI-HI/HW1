import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class AnimationPane extends JPanel
{
    private GameManager handler;
    private Graphics2D g2d;
    
    public AnimationPane(GameManager _handler)
    {
        this.handler = _handler;
    }

    public AnimationPane(GameManager _handler, String background_img, int background_width, int background_height)
    {
        this.handler = _handler;
    }

    public Graphics2D getGraphics2d()
    {
        return this.g2d;
    }

    public void setGraphics2d(Graphics2D _g2d)
    {
        this.g2d = _g2d;
    }

    public void paint()
    {   
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        Background background = this.handler.getBackground();
        ArrayList<Integer> coords = background.getCoordinates();
        int xPos_cont = background.getXPosCont();
        
        g.drawImage(background.getBackgroundImage(), coords.get(0), coords.get(1), null);
        g.drawImage(background.getBackgroundImage(), xPos_cont, coords.get(1), null);

        for (int i=0; i<this.handler.objects.size(); ++i)
        {
            int _xPos = this.handler.objects.get(i).xPos;
            int _yPos = this.handler.objects.get(i).yPos;
            BufferedImage _img = this.handler.objects.get(i).img;

            g.drawImage(_img, _xPos, _yPos, null);
        }
    }
}
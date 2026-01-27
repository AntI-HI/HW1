import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * JPanel subclass responsible for rendering all game visuals.
 * Handles drawing the background with scrolling effect and all active game
 * objects.
 * Rendering is synchronized with the game object lists to prevent concurrent
 * modification.
 */
public class AnimationPane extends JPanel {
    private GameManager game_manager;
    private Graphics2D g2d;

    public AnimationPane(GameManager _handler) {
        this.game_manager = _handler;
    }

    public AnimationPane(GameManager _handler, String background_img, int background_width, int background_height) {
        this.game_manager = _handler;
    }

    public Graphics2D getGraphics2d() {
        return this.g2d;
    }

    public void setGraphics2d(Graphics2D _g2d) {
        this.g2d = _g2d;
    }

    public void paint() {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Background background = this.game_manager.getBackground();
        ArrayList<Integer> coords = background.getCoordinates();
        int xPos_cont = background.getXPosCont();

        g.drawImage(background.getBackgroundImage(), coords.get(0), coords.get(1), null);
        g.drawImage(background.getBackgroundImage(), xPos_cont, coords.get(1), null);

        java.util.List<GameObject> activeObjects = this.game_manager.getActiveGameObjects();
        synchronized (activeObjects) {
            for (int i = 0; i < activeObjects.size(); ++i) {
                GameObject obj = activeObjects.get(i);
                int _xPos = obj.xPos;
                int _yPos = obj.yPos;
                BufferedImage _img = obj.img;

                g.drawImage(_img, _xPos, _yPos, null);
            }
        }
    }
}
package com.runner.model;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;
import com.runner.utils.DataPool;
import com.runner.logic.score.ScorePowerup;
import com.runner.logic.score.X2Powerup;
import com.runner.logic.score.X5Powerup;

/**
 * Represents a score multiplier powerup in the game.
 * Extends GameObject and contains a score multiplier that can be enhanced with
 * decorators.
 * Randomly generates X2 or X5 powerup multipliers.
 */
public class Powerup extends GameObject {
    public ScorePowerup score_powerup = null;

    public Powerup() {
        super();
        this.dataPool = DataPool.getInstance();

        if (img == null) {
            Image image = this.dataPool.getPowerupSprite();
            int width = image.getWidth(null);
            int height = image.getHeight(null);

            GameObjectCreateGraphics(width, height);

            Graphics2D g2d = img.createGraphics();
            g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
            g2d.dispose();
        }
        score_powerup = new ScorePowerup();
        CreateRandomScoreMultiplierPowerup();
    }

    public ScorePowerup getScorePowerup() {
        return score_powerup;
    }

    public void setPosition(int _xPos, int _yPos, int _width, int _height) {
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(dataPool.getPowerupSprite(), 0, 0, null);
        g2d.dispose();
    }

    private void CreateRandomScoreMultiplierPowerup() {
        Random r = new Random();

        int result = (int) Math.abs(r.nextLong() % 2);

        if (result == 0) {
            score_powerup = new X2Powerup(score_powerup);
        } else {
            score_powerup = new X5Powerup(score_powerup);
        }
    }

    public static GameObject Create(int pos_x, int pos_y) {
        DataPool dataPool = DataPool.getInstance();
        Image img = dataPool.getPowerupSprite();
        GameObject powerup;
        int width;
        int height;

        width = img.getWidth(null);
        height = img.getHeight(null);
        powerup = new Powerup();
        powerup.setObjectPositionAndBounds(pos_x, pos_y, width, height);

        return powerup;
    }
}

package com.runner.logic.score;

/**
 * Base class for score powerups using the Decorator pattern.
 * Provides the default score multiplier of 1.
 * Can be decorated with X2Powerup or X5Powerup to increase the multiplier.
 */
public class ScorePowerup {
    /**
     * Default constructor for ScorePowerup.
     */
    public ScorePowerup() {
    }

    /**
     * Gets the score multiplier for this powerup.
     * Default implementation returns 1 (no multiplier).
     * 
     * @return The score multiplier value
     */
    public int getScoreMultiplier() {
        return 1;
    }

}

package com.runner.logic.score;

/**
 * Decorator class that applies a 5x score multiplier to a powerup.
 * Extends ScorePowerupDecorator and follows the Decorator design pattern.
 */
public class X5Powerup extends ScorePowerupDecorator {
    public X5Powerup(ScorePowerup powerup) {
        this.score_powerup = powerup;
    }

    @Override
    public int getScoreMultiplier() {
        return this.score_powerup.getScoreMultiplier() * 5;
    }

}

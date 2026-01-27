/**
 * Decorator class that applies a 2x score multiplier to a powerup.
 * Extends ScorePowerupDecorator and follows the Decorator design pattern.
 */
public class X2Powerup extends ScorePowerupDecorator
{
    public X2Powerup(ScorePowerup powerup)
    {
        this.score_powerup = powerup;
    }   

    @Override
    public int getScoreMultiplier()
    {    
        return this.score_powerup.getScoreMultiplier() * 2;
    }
}

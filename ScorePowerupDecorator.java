/**
 * Abstract decorator class for score powerups.
 * Extends ScorePowerup and provides a base for concrete decorators like X2Powerup and X5Powerup.
 * Follows the Decorator design pattern to add functionality dynamically.
 */
public abstract class ScorePowerupDecorator extends ScorePowerup
{
    /**
     * The wrapped score powerup object that this decorator enhances.
     */
    public ScorePowerup score_powerup;

}

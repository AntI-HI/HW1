public class X5Powerup extends ScorePowerupDecorator
{
    public X5Powerup(ScorePowerup powerup)
    {
        this.score_powerup = powerup;
    }

    @Override
    public int getScoreMultiplier()
    {    
        return this.score_powerup.getScoreMultiplier() * 5;
    }
    
}

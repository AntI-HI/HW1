public class ScoreManager
{
    public static ScoreManager instance;

    private int current_score;

    private Player   player;
    private Obstacle obstacle;

    public ScorePowerup score_powerup;

    public boolean pause;

    public static ScoreManager CreateScoreManager ()
    {
        if (instance == null)
        {
            instance = new ScoreManager();
        }

        return instance;
    }

    public static ScoreManager getInstance()
    {
        return instance;
    }

    private ScoreManager()
    {
        score_powerup    = new ScorePowerup();
        current_score    = 0;
        pause            = true;
    }

    public ScorePowerup getScorePowerup()
    {
        return score_powerup;
    }

    public void setScorePowerup(ScorePowerup _score_powerup)
    {
        score_powerup = _score_powerup;
    }

    public int getCurrentScore()
    {
        return current_score;
    }

    public void updateScore(int value)
    {
        current_score += value;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public void setObstacle(Obstacle obstacle)
    {
        this.obstacle = obstacle;
    }

    public boolean PerformScore(int score_multiplier)
    {
        if (!obstacle.collides && player.xPos > obstacle.xPos + obstacle.width)
        {
            current_score = current_score + score_multiplier;
            System.out.printf("Score: %d\n", current_score);
            return true;
        }
        else
        {
            return false;
        }
    }
}

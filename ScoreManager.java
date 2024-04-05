public class ScoreManager
{
    public static ScoreManager instance;
    private GameManager game_manager;

    private int current_score;

    private Player   player;
    private Obstacle obstacle;

    public ScorePowerup score_powerup;

    public boolean pause;

    public static ScoreManager CreateScoreManager (GameManager game_manager)
    {
        if (instance == null)
        {
            instance = new ScoreManager(game_manager);
        }

        return instance;
    }

    public static ScoreManager getInstance()
    {
        return instance;
    }

    private ScoreManager(GameManager game_manager)
    {
        this.game_manager  = game_manager;
        score_powerup      = new ScorePowerup();
        current_score      = 0;
        pause              = true;
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

    public boolean UpdateScore()
	{
		boolean performed = false;
		// Make sure there is the next obstacle in the objects container
		if (!pause)
		{
			GameObject object = game_manager.getGameObject(game_manager.current_obstacle_idx);
			if (object instanceof Obstacle)
			{
				// Acknowledge the current obstacle as the target score trigger.
				setObstacle((Obstacle)game_manager.getGameObject(game_manager.current_obstacle_idx));
				
				ScoreManager scoreManager = ScoreManager.getInstance();

				performed = scoreManager.PerformScore(scoreManager.score_powerup.getScoreMultiplier());
				
				if (performed)
				{
					scoreManager.pause = true;	// Wait until new obstacle object spawns.
				}
			}
		}

		return performed;
	}

    public boolean PerformScore(int score_multiplier)
    {
        if (!obstacle.collides && player.xPos > obstacle.xPos + obstacle.width)
        {
            UI_Elements ui = game_manager.getUi_Elements();
            current_score = current_score + score_multiplier;
            ui.updateScoreLabel("Score: " + current_score);
            
            return true;
        }
        else
        {
            return false;
        }
    }
}

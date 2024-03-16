import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UI_Elements
{
    private JPanel main_panel;

    private JLabel  scoreLabel;
    private JLabel  eventLabel;
    private JButton exitButton;
    private JButton pauseButton;
    private JButton startButton;


    public UI_Elements(JPanel panel)
    {
        main_panel = panel;
    }

    public void initButtons()
    {

    }

    public void initEventLabel()
    {

    }

    public void initScoreLabel()
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel
		scoreLabel = new JLabel("Score: 0");
        main_panel.add(scoreLabel, BorderLayout.NORTH);
    }

    public void updateScoreLabel(String message)
    {
        scoreLabel.setText(message);
    }

}
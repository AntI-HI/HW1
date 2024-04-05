import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UI_Elements implements ActionListener
{
    private JPanel main_panel;

    private JLabel  scoreLabel;
    private JLabel  eventLabel;
    private JLabel  pauseLabel;

    private JButton exitButton;
    private JButton pauseButton;

    public UI_Elements(JPanel panel)
    {
        main_panel = panel;
    }

    public void initButtons()
    {
        initExitButton();
        initPauseButton();
    }

    private void initExitButton()
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel

        exitButton = new JButton();
		exitButton.setBounds(1180, 20, 80, 40);
		exitButton.addActionListener(this);
		exitButton.setText("EXIT!");

        exitButton.setFocusable(false);
		exitButton.setFont(new Font("Comic Sans",Font.BOLD,15));
		exitButton.setForeground(Color.red);
		exitButton.setBackground(Color.lightGray);
		exitButton.setBorder(BorderFactory.createEtchedBorder());

        main_panel.add(exitButton);
    }

    private void initPauseButton()
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel

        pauseButton = new JButton();
		pauseButton.setBounds(1180, 60, 80, 40);
		pauseButton.addActionListener(this);
		pauseButton.setText("PAUSE!");

        pauseButton.setFocusable(false);
		pauseButton.setFont(new Font("Comic Sans",Font.BOLD,15));
		pauseButton.setForeground(Color.blue);
		pauseButton.setBackground(Color.lightGray);
		pauseButton.setBorder(BorderFactory.createEtchedBorder());

        main_panel.add(pauseButton);
    }

    public void initEventLabel()
    {
        eventLabel = new JLabel("Nothing");
		eventLabel.setBounds(440, 20, 300, 50);

        eventLabel.setHorizontalTextPosition(JButton.CENTER);
		eventLabel.setVerticalTextPosition(JButton.BOTTOM);
		eventLabel.setFont(new Font("Comic Sans",Font.BOLD,20));
        eventLabel.setForeground(Color.green);
		eventLabel.setBackground(Color.lightGray);
        
        eventLabel.setVisible(false);
        
        main_panel.add(eventLabel);
    }

    public void initLabels()
    {
        initScoreLabel();
        initPauseLabel();
        initEventLabel();
    }

    private void initScoreLabel()
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setBounds(10, 10, 1000, 40);

        scoreLabel.setHorizontalTextPosition(JButton.CENTER);
		scoreLabel.setVerticalTextPosition(JButton.BOTTOM);
		scoreLabel.setFont(new Font("Comic Sans",Font.BOLD,20));
        scoreLabel.setForeground(Color.orange);
		scoreLabel.setBackground(Color.lightGray);
        
        main_panel.add(scoreLabel);
    }

    public void updateScoreLabel(String message)
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel
        
        scoreLabel.setText(message);
    }

    public void updateEventLabel(String message)
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel

        eventLabel.setText(message);
        eventLabel.setVisible(true);
    }

    private void initPauseLabel()
    {
		pauseLabel = new JLabel("PAUSED");
		pauseLabel.setBounds(440, 320, 1000, 100);

        pauseLabel.setHorizontalTextPosition(JButton.CENTER);
		pauseLabel.setVerticalTextPosition(JButton.BOTTOM);
		pauseLabel.setFont(new Font("Comic Sans",Font.BOLD,100));
        pauseLabel.setForeground(Color.orange);
		pauseLabel.setBackground(Color.lightGray);
        
        pauseLabel.setVisible(false);
        
        main_panel.add(pauseLabel);
    }

    private void ShowPauseLabel()
    {
        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel

        if(!pauseLabel.isVisible())
        {
            pauseButton.setText("RESUME");
            pauseLabel.setVisible(true);
        }
        else
        {
            pauseButton.setText("PAUSE");
            pauseLabel.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource() == exitButton)
        {
            Game.stop();
		}
        else if (e.getSource() == pauseButton)
        {
            GameEventManager.getInstance().PauseGame();
            ShowPauseLabel();
        }
    }

}
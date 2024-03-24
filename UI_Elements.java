import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UI_Elements implements ActionListener
{
    private JFrame main_frame;
    private JPanel main_panel;

    private JLabel  scoreLabel;
    private JLabel  eventLabel;
    private JButton exitButton;
    private JButton pauseButton;
    private JButton startButton;


    public UI_Elements(JFrame frame, JPanel panel)
    {
        main_frame = frame;
        main_panel = panel;
    }

    public void initButtons()
    {
        main_frame = Game.getFrame();

        main_panel.setLayout(new BorderLayout()); // Setting BorderLayout for the main panel

        exitButton = new JButton();
		exitButton.setBounds(1200, 20, 50, 40);
		exitButton.addActionListener(this);
		exitButton.setText("EXIT!");

        exitButton.setFocusable(false);
		exitButton.setFont(new Font("Comic Sans",Font.BOLD,15));
		exitButton.setForeground(Color.red);
		exitButton.setBackground(Color.lightGray);
		exitButton.setBorder(BorderFactory.createEtchedBorder());
        
        main_panel.add(exitButton);
    }

    public void initEventLabel()
    {

    }

    public void initScoreLabel()
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

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(e.getSource()==exitButton)
        {
            Game.stop();
		}
    }

}
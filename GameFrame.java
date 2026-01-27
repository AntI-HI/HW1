import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

/**
 * Extended JFrame class for the game window.
 * Configures frame properties specific to the game display.
 */
public class GameFrame extends JFrame {

	public GameFrame(Game sw, int GWIDTH, int GHEIGHT) {
		requestFocus();
		setUndecorated(true);
		//setAlwaysOnTop(true);
		setSize(GWIDTH, GHEIGHT);

		add(sw);
		setVisible(true);
	}
}

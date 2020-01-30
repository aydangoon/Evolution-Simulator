import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JFrame;

import displays.Game;
import displays.GameStateHandler;
import displays.InfoDisplay;
import settings.Constants;
import vectormath.*;

public class EvolutionMain extends JFrame{

	public static void main(String[] args) {
		
		EvolutionMain window = new EvolutionMain();
		
		InfoDisplay disp = new InfoDisplay();
		Game game = new Game(disp);
		GameStateHandler gsh = new GameStateHandler(game);
		
		window.setTitle("Evolution");
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    window.setSize(new Dimension(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT));
	    window.setLayout(new FlowLayout());
	    window.setResizable(false);
	    window.add(game);
	    window.add(gsh);
	    window.add(disp);
	    window.setVisible(true);
		
	}

}

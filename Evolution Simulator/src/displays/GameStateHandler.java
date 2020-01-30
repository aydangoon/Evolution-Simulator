package displays;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import life.DNA;
import settings.Constants;
import settings.Parameters;

public class GameStateHandler extends JPanel{

	private Game game;
	
	private Parameters p;
	
	private JButton startButton;
	private JSlider[] parameterVals;
	private JSlider replicationCost;
	private JSlider totalFood;
	private JCheckBox[] viewToggles;
	
	public GameStateHandler(Game g) {
		game = g;
		this.setPreferredSize(new Dimension(Constants.GSH_WIDTH, Constants.GSH_HEIGHT));
		this.setFocusable(false);
		this.setVisible(true);
		this.setBackground(Color.WHITE);
		
		
		/*
		 * TODO fix this garbage code
		 */
		
		p = new Parameters();
		
		parameterVals = new JSlider[7];
		for(int i = 0; i < parameterVals.length; i++) {
			parameterVals[i] = new TraitSlider(0, 100, 10, 10, DNA.getTraitName(i), this);
		}
		
		replicationCost = new TraitSlider(0, 2000, 750, 1, "replication cost", this);
		totalFood = new TraitSlider(0, 1000, 100, 1, "total food", this);
		
		startButton = new JButton("Start");
		startButton.setFocusable(false);
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				for(int i = 0; i < parameterVals.length; i++) {
					p.setCost(i, ((double)parameterVals[i].getValue()) / 10);
					
				}
				p.setReplicationCost(replicationCost.getValue());
				p.setTotalFood(totalFood.getValue());
				game.start(p);
			}
		});
		this.add(startButton);
		
		viewToggles = new JCheckBox[7];
		for(int i = 0; i < viewToggles.length; i++) {
			viewToggles[i] = new JCheckBox(DNA.getTraitName(i), true);
			viewToggles[i].setFocusable(false);
			viewToggles[i].setPreferredSize(new Dimension(Constants.GSH_WIDTH, 20));
			int z = i;
			viewToggles[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					game.setIndDisplay(z);
				}
			});
			this.add(viewToggles[i]);
		}
		
	}
	
}

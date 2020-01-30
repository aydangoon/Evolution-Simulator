package displays;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import infoandutils.Data;
import settings.Constants;

public class InfoDisplay extends JPanel{
	
	private ArrayList<Data> data;
	
	public InfoDisplay() {
		this.setPreferredSize(new Dimension(Constants.ID_WIDTH, Constants.ID_HEIGHT));
		this.setFocusable(true);
		this.setVisible(true);

		data = new ArrayList<Data>();
	}
	public void clearData() {
		data.clear();
	}
	public void addDataPoint(Data d) {
		data.add(d);
		if(data.size() > 100)
			data.remove(0);
		
		repaint();
	}
	public void paintComponent(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.WHITE);
		g.drawLine(10, 10, 10, 390);
		g.drawLine(10, 390, 390, 390);
		
		g.drawString("% Carnivore", 40, 410);
		g.drawString("% Camoflage", 40, 430);
		g.drawString("% Land Speed", 40, 450);
		g.drawString("% Water Speed", 40, 470);
		g.drawString("% Perception", 40, 490);
		g.drawString("% Stamina", 40, 510);
		g.drawString("% Poison Resistance", 40, 530);
		if(data.size() > 0)
		g.drawString("Population: " + data.get(data.size() - 1).getPopulation(), 40, 550);
		
		g.setColor(Color.cyan);
		g.fillRect(20, 400, 10, 10);
		g.setColor(Color.green);
		g.fillRect(20, 420, 10, 10);
		g.setColor(Color.green.darker());
		g.fillRect(20, 440, 10, 10);
		g.setColor(Color.cyan.darker());
		g.fillRect(20, 460, 10, 10);
		g.setColor(Color.pink);
		g.fillRect(20, 480, 10, 10);
		g.setColor(Color.pink.darker());
		g.fillRect(20, 500, 10, 10);
		g.setColor(Color.pink.brighter());
		g.fillRect(20, 520, 10, 10);
		
		double spacing = 390.0 / 100.0;

		for(int i = 0; i < 7; i++) {
			switch(i) {
				case 0: g.setColor(Color.cyan); break;
				case 1: g.setColor(Color.green); break;
				case 2: g.setColor(Color.green.darker()); break;
				case 3: g.setColor(Color.cyan.darker()); break;
				case 4: g.setColor(Color.pink); break;
				case 5: g.setColor(Color.pink.darker()); break;
				default:g.setColor(Color.pink.brighter()); break;			
			}
			int oldX = 0;
			int oldY = 0;
			int newX = 0;
			int newY = 0;
			for(int j = 0; j < data.size(); j++) {
				newX = (int) (10 + (j * spacing));
				newY = (int) (390 - (390 * data.get(j).get(i) / Constants.MAX_VALUES[i]));
				if(j != 0)
					g.drawLine(oldX, oldY, newX, newY);
				oldX = newX;
				oldY = newY;
			}
		}
		
	}
}

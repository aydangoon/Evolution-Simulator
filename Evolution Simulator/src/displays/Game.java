package displays;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.JPanel;
import javax.swing.Timer;

import infoandutils.Camera;
import infoandutils.Data;
import infoandutils.Date;
import infoandutils.DateBroadcast;
import life.DNA;
import life.Food;
import life.Organism;
import settings.Constants;
import settings.Parameters;
import tiles.Tile;
import tiles.TileType;
import vectormath.*;

public class Game extends JPanel implements KeyListener {
	/***
	 * @type InfoDisplay
	 * The information display window. Gets passed data about the game.
	 */
	private InfoDisplay disp;
	/***
	 * @type Tile[][]
	 * The map as a double array of tiles in the form (column, row).
	 */
	private Tile[][] tiles;
	/***
	 * @type Camera
	 * An object that represents the location of the camera's upper left-hand coordinate.
	 */
	private Camera camera;
	
	/***
	 * @type Date
	 * An object that stores the date of the game.
	 */
	private Date date;
	
	/***
	 * @type ArrayList<Food>
	 * An array list of Food objects. Gets refilled every new day.
	 */
	private ArrayList<Food> food;
	
	/***
	 * @type ArrayList<Organism>
	 * An array list of Organism objects. 
	 */
	private ArrayList<Organism> organisms;
	
	/***
	 * @type boolean
	 * Booleans that represented whether an arrow key is being pressed down or not. Used
	 * to change the camera's position.
	 */
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean downPressed;
	
	/***
	 * @type Timer
	 * The game's global timer. Refreshes at 100 fps. It calls the update method every frame.
	 */
	private Timer t;
	
	private boolean[] individualDisplays;
	private Parameters parameters;
	
	public Game(InfoDisplay d) {
		
		disp = d;
		
		parameters = new Parameters();
		
		this.setPreferredSize(new Dimension(Constants.GAME_WIDTH, Constants.GAME_HEIGHT));
		this.setFocusable(true);
		this.setVisible(true);
		this.setBackground(Color.GRAY);
		this.addKeyListener(this);
		tiles = new Tile[Constants.COLUMNS][Constants.ROWS];
		
		for(int c = 0; c<tiles.length; c++)
			for(int r = 0; r<tiles[c].length; r++)
				tiles[c][r] = null;
		
		generateTileMap();
		
		camera = new Camera();
		
		date = new Date();
		
		food = new ArrayList<Food>();
		
		addFreshFood();
		
		organisms = new ArrayList<Organism>();
		for(int i = 0; i < Constants.NUMBER_OF_ORGANISMS; i++) {
			Organism o = new Organism();
			o.setCostPerHour(parameters.costPerHour(o.getDNACopy()));
			organisms.add(o);
		}
		
		leftPressed = false;
		rightPressed = false;
		upPressed = false;
		downPressed = false;
		
		individualDisplays = new boolean[7];
		for(int i = 0; i < individualDisplays.length; i++)
			individualDisplays[i] = true;
		
		t = new Timer(Constants.REFRESH_RATE, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		t.start();
		
		repaint();
	}
	
	/****************************************************************************************************************
	 ****************************************************************************************************************
	 * 												Functional Updates
	 * **************************************************************************************************************
	 * **************************************************************************************************************
	 */
	public void start(Parameters p) {
		
		disp.clearData();
		
		organisms.clear();
		
		t.stop();
		
		for(int c = 0; c<tiles.length; c++)
			for(int r = 0; r<tiles[c].length; r++)
				tiles[c][r] = null;
		
		parameters = p;
		
		generateTileMap();
		
		date = new Date();
		
		food = new ArrayList<Food>();
		
		addFreshFood();
		
		for(int i = 0; i < Constants.NUMBER_OF_ORGANISMS; i++) {
			Organism o = new Organism();
			o.setCostPerHour(parameters.costPerHour(o.getDNACopy()));
			organisms.add(o);
		}
		
		t.restart();
		
	}

	public void update() {
		DateBroadcast db = date.timeStep();
		
		updateCamera();
		
		if(db == DateBroadcast.NEW_DAY) {
			addFreshFood();
			addOffspring();
		}else if(db == DateBroadcast.NEW_DAY || db == DateBroadcast.HOUR_PASSED) {
			setData();
			updateOrganismEnergy();
		}
		
		updateFood();

		updateOrganisms();
		
		killOrganisms();
		
		repaint();
	}
	public void updateCamera() {
		int hor = (rightPressed ? 1 : 0) - (leftPressed ? 1 : 0);
		int ver = (downPressed ? 1 : 0) - (upPressed ? 1 : 0);
		camera.updateCameraPos(hor, ver);
	}
	public void updateFood() {
		for(int i = food.size() - 1; i>=0; i--)
			if(!food.get(i).isAlive())
				food.remove(i);
	}
	public void updateOrganismEnergy() {
		for(Organism o : organisms)
			o.useEnergy();
	}
	public void updateOrganisms() {	
		
		for(Organism o : organisms) {
			
			o.checkPrey();
			o.checkPredator();
			
			o.setOnTile(tiles[o.getX()/Constants.TILE_SIDE][o.getY()/Constants.TILE_SIDE].getType());
			
			Angle left = new Angle(o.getDirection().get());
			left.shift(20);
			Angle right = new Angle(o.getDirection().get());
			right.shift(-20);
			
			int colL = (int)((o.getX() + (o.get(DNA.perception) * Angle.cos(left.get())))/Constants.TILE_SIDE);
			int rowL = (int)((o.getY() + (o.get(DNA.perception) * Angle.sin(left.get())))/Constants.TILE_SIDE);
			
			if(rowL < 0)
				rowL = 0;
			if(colL < 0)
				colL = 0;
			if(rowL > Constants.ROWS - 1)
				rowL = Constants.ROWS - 1;
			if(colL > Constants.COLUMNS - 1)
				colL = Constants.COLUMNS - 1;
			
			o.setHeadingToLeft(tiles[colL][rowL].getType());
						
			int colR = (int)((o.getX() + (o.get(DNA.perception) * Angle.cos(right.get())))/Constants.TILE_SIDE);
			int rowR = (int)((o.getY() + (o.get(DNA.perception) * Angle.sin(right.get())))/Constants.TILE_SIDE);
			
			if(rowR < 0)
				rowR = 0;
			if(colR < 0)
				colR = 0;
			if(rowR > Constants.ROWS - 1)
				rowR = Constants.ROWS - 1;
			if(colR > Constants.COLUMNS - 1)
				colR = Constants.COLUMNS - 1;
			
			o.setHeadingToRight(tiles[colR][rowR].getType());
			
			if(!o.hasPredator() && !o.hasPrey()) {
				scanSurroundings(o);
			}
			
			o.move();
			
		}
	}
	public void killOrganisms() {
		ListIterator<Organism> temp = organisms.listIterator();
		while(temp.hasNext()) {
			if(!temp.next().isAlive()) {
				temp.remove();
			}
		}
	}

	public void scanSurroundings(Organism o) {
		for(Organism o2 : organisms) {
			if(o != o2 && o.canSense(o2) && 
			  (o2.getCurrentTile() == o.getPrefTile() || o.getPrefTile() == TileType.NONE)) {
				
				if(o.isThreatenedBy(o2)) {
					o.setPredator(o2);
					return;
				}else if(o2.isThreatenedBy(o)){
					o.updatePrey(o2);
				}
			}
		}
		if(o.get(DNA.carnivore) < Constants.STRICTLY_CARNIVORE) {
			for(Food f : food) {
				if(o.canSense(f) && (f.getCurrentTile() == o.getPrefTile() || o.getPrefTile() == TileType.NONE)) {
					o.updatePrey(f);
				}
			}
		}
	}
	public void addFreshFood() {
		while(food.size() < parameters.getTotalFood()) {
			Food f = new Food();
			f.setOn(tiles[f.getX()/Constants.TILE_SIDE][f.getY()/Constants.TILE_SIDE].getType());
			food.add(f);
		}
	}
	public void addOffspring() {
		ArrayList<Organism> babies = new ArrayList<Organism>();
		for(Organism o : organisms)
			if(o.getEnergy() >= parameters.getReplicationCost()) {
				Organism baby = o.getBaby();
				baby.setCostPerHour(parameters.costPerHour(baby.getDNACopy()));
				babies.add(baby);
			}
		
		organisms.addAll(babies);
	}
	
	public void setData() {
		ArrayList<DNA> popGenes = new ArrayList<DNA>();
		for(Organism o : organisms)
			popGenes.add(o.getDNACopy());
		
		disp.addDataPoint(new Data(DNA.average(popGenes), organisms.size()));
		
	}
	public void setIndDisplay(int i) {
		if(i >= 0 && i < 7) {
			individualDisplays[i] = !individualDisplays[i];
		}
	}
	
	/****************************************************************************************************************
	 ****************************************************************************************************************
	 * 												Painting and Graphics
	 * **************************************************************************************************************
	 * **************************************************************************************************************
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		/***
		 * Colors for tiles
		 */
		int cStart = camera.getXStartTile();
		int cEnd   = camera.getXEndTile();
		int rStart = camera.getYStartTile();
		int rEnd   = camera.getYEndTile();
		for(int c = cStart; c < cEnd; c++) {
			for(int r = rStart; r < rEnd; r++) {			
				g.setColor(tiles[c][r].getColor());
				g.fillRect((c * Constants.TILE_SIDE) - camera.getXOffset(), 
						   (r * Constants.TILE_SIDE) - camera.getYOffset(), Constants.TILE_SIDE, Constants.TILE_SIDE);
			}
		}
		
		/***
		 * String location
		 */
		g.setColor(Color.WHITE);
		g.drawString(camera.toString(), 10, 40);
		
		/***
		 * String date
		 */
		g.drawString(date.getDate(), 10, 20);
		
		/***
		 * Draw food
		 */
		for(Food f : food) {
			g.setColor(Color.orange);
			g.fillOval(f.getX() - camera.getXOffset(), f.getY() - camera.getYOffset(), 
					   Constants.FOOD_RADIUS, Constants.FOOD_RADIUS);
		}
		/***
		 * draw organism
		 */
		for(Organism o : organisms) {
		if(o.getPrefTile() == TileType.LAND)
			g.setColor(Color.GREEN);
		else
			g.setColor(Color.CYAN);
		g.fillRect(o.getX() - camera.getXOffset(), o.getY() - camera.getYOffset(), 4, 4);
		g.setColor(Color.WHITE);
		
		/***
		 * draw organism direction vector
		 */
		
		Angle a = o.getDirection();
		g.setColor(Color.WHITE);
		g.drawLine(o.getX() - camera.getXOffset() + 2, o.getY() - camera.getYOffset() + 2, 
				   o.getX() + (int)(8 * Angle.cos(a.get())) - camera.getXOffset() + 2,
				   o.getY() + (int)(8 * Angle.sin(a.get())) - camera.getYOffset() + 2); 
		
		/***
		 * draw organism's info
		 */
		g.setFont(new Font("times new roman", Font.PLAIN, 11));
		int yPos = 0;
		for(int i = 0; i < individualDisplays.length; i++) {
			if(individualDisplays[i] && i != DNA.perception && i != DNA.stamina) {
				g.setColor(Color.white);
				g.drawString(DNA.getTraitName(i) + ": " + (double)((int)(10 * o.get(i))) / 10, o.getX() - camera.getXOffset() + 10,
							 o.getY() - camera.getYOffset() + (yPos * 10));
				yPos++;
			}else if(individualDisplays[i] && i == DNA.perception) {
				g.setColor(Color.CYAN);
				g.drawOval(o.getX() - camera.getXOffset() - (int)o.get(DNA.perception), 
						   o.getY() - camera.getYOffset() - (int)o.get(DNA.perception), 
						   (int)(2*o.get(DNA.perception)), (int)(2*o.get(DNA.perception)));
			}else if(individualDisplays[i] && i == DNA.stamina) {
				g.setColor(Color.RED.darker());
				g.fillRect(o.getX() - camera.getXOffset(), o.getY() - camera.getYOffset() - 15, (int)o.get(DNA.stamina)/2, 4);
				g.setColor(Color.RED);
				g.fillRect(o.getX() - camera.getXOffset(), o.getY() - camera.getYOffset() - 15, (int)o.getStamina()/2, 4);
			}
		}
		g.setColor(Color.ORANGE.darker());
		g.fillRect(o.getX() - camera.getXOffset(), o.getY() - camera.getYOffset() - 19, (int)100, 4);
		if(o.getEnergy()/10 > 100)
			g.setColor(Color.ORANGE.brighter());
		else
			g.setColor(Color.orange);
		g.fillRect(o.getX() - camera.getXOffset(), o.getY() - camera.getYOffset() - 19, (int)(o.getEnergy()/10 > 100 ? 100 : o.getEnergy()/10), 4);

		}
	}
	/****************************************************************************************************************
	 ****************************************************************************************************************
	 * 												Tile Mapping
	 * **************************************************************************************************************
	 * **************************************************************************************************************
	 */
	public void generateTileMap() {
		tiles[0][0] = new Tile(0, 0, 0);
		
		for(int c = 0; c < Constants.COLUMNS; c++) {
			for(int r = 0; r < Constants.ROWS; r++) {
				tiles[c][r] = new Tile(c, r, tiles);
			}
		}
		clearSingletons();
		specializeTileMapTypes();
		
	}
	public void specializeTileMapTypes() {
		for(int c = 0; c < Constants.COLUMNS; c++) {
			for(int r = 0; r < Constants.ROWS; r++) {
					tiles[c][r].assertHue();				
			}
		}
	}
	public void clearSingletons() {
		for(int c = 0; c < Constants.COLUMNS; c++) {
			for(int r = 0; r < Constants.ROWS; r++) {
				tiles[c][r].generateColor(tiles, true);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
			
			case KeyEvent.VK_UP:
				upPressed = true;
			break;
			
			case KeyEvent.VK_DOWN:
				downPressed = true;
			break;
			
			case KeyEvent.VK_RIGHT:
				rightPressed = true;
			break;
			
			case KeyEvent.VK_LEFT:
				leftPressed = true;
			break;
		}
		
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		
		case KeyEvent.VK_UP:
			upPressed = false;
		break;
		
		case KeyEvent.VK_DOWN:
			downPressed = false;
		break;
		
		case KeyEvent.VK_RIGHT:
			rightPressed = false;
		break;
		
		case KeyEvent.VK_LEFT:
			leftPressed = false;
		break;
		
		}	
	}	
}

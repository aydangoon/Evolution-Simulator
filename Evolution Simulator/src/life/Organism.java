package life;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import settings.Constants;
import tiles.TileType;
import vectormath.*;

public class Organism extends Lifeform{
		
	private double x;
	private double y;
	
	private Angle dir;
	private Vector vel;
	private double stamina;
	
	private DNA dna;
	private double costPerHour;
	private double energy;
	
	private Lifeform prey;
	private Lifeform predator;
	
	private TileType prefType;
	private TileType onNow;
	private TileType headingToLeft;
	private TileType headingToRight;
	
	public Organism() {
		
		dna = new DNA();
		
		instantiateBasics();
		
	}
	public Organism(DNA dna) {
				
		this.dna = dna;
		
		instantiateBasics();
	}
	private void instantiateBasics() {
		
		x = 1 + (Math.random() * (Constants.TILE_SIDE * Constants.COLUMNS - 1));
		y = 1 + (Math.random() * (Constants.TILE_SIDE * Constants.ROWS - 1));
		double angle = new Random().nextInt(360) + Math.random();
		
		dir = new Angle(angle);
		vel = new Vector(0, 0);
		energy = 1000;
		costPerHour = 0;
		stamina = dna.get(DNA.stamina);
		prey = null;
		predator = null;
		
		if(dna.get(DNA.land_speed) - dna.get(DNA.water_speed) > 0.5)
			prefType = TileType.LAND;
		else if(dna.get(DNA.land_speed) - dna.get(DNA.water_speed) < 0)
			prefType = TileType.SEA;
		else
			prefType = TileType.NONE;
		
		onNow = null;
		headingToLeft = null;
		headingToRight = null;
	}
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public double get(int i) {
		return dna.get(i);
	}
	public void move() {
		double speed = dna.get(DNA.land_speed);
		if(onNow != null && onNow == TileType.LAND)
			speed = stamina >= 0.5 ? dna.get(DNA.land_speed) : dna.get(DNA.land_speed) / 4;
		else if(onNow != null && onNow == TileType.SEA)
			speed = stamina >= 0.5 ? dna.get(DNA.water_speed) : dna.get(DNA.water_speed) / 4;
		
		if(!hasPrey() && !hasPredator())
			speed /= 2;
		
		if(x < 1) {
			bounce(true);
			x = 1;
		}else if(x > Constants.COLUMNS * Constants.TILE_SIDE - 20) {
			bounce(true);
			x = Constants.COLUMNS * Constants.TILE_SIDE - 20;
		}
		
		
		if(y < 1) {
			bounce(false);
			y = 1;
		}else if(y > Constants.ROWS* Constants.TILE_SIDE - 20) {
			bounce(false);
			y = Constants.ROWS * Constants.TILE_SIDE - 20;
		}
		
		if(hasPredator()) {
			setDirection(predator, true);
			if(stamina >= 0.5)
				stamina-=0.5;
			
			if(getHitbox().intersects(predator.getHitbox())) {
				this.energy = 0;
			}
		}else if(hasPrey()) {
			if(stamina >= 0.5)
				stamina-=0.5;
			
			setDirection(prey, false);
			if(getHitbox().intersects(prey.getHitbox())) {
				energy = energy + 200 > 2000 ? 2000 : energy + 500;
				prey.kill();
			}
		}
			
		if(headingToLeft != prefType && headingToRight == prefType) {
			dir.shift(-10);
		}else if(headingToRight != prefType && headingToLeft == prefType) {
			dir.shift(10);
		}else if(headingToLeft != prefType && headingToRight != prefType && onNow == prefType)
			dir.shift(10);
		
		if(stamina < dna.get(DNA.stamina))
			stamina+=0.25;
		
		
		vel.setX(speed * Angle.cos(dir.get())); 
		vel.setY(speed * Angle.sin(dir.get())); 
		
		x += vel.getX();
		y += vel.getY();
	}
	public void setCostPerHour(double c) {
		costPerHour = c;
	}
	public void useEnergy() {
		energy -= costPerHour;
	}
	public boolean isThreatenedBy(Organism o) {
		return (o.get(DNA.carnivore) >= Constants.CARNIVORE_LEVEL && o.get(DNA.carnivore) - get(DNA.carnivore) > 0.5);
	}
	public void setDirection(Lifeform o, boolean fleeing) {
		Angle ang;
		if(fleeing)
		 ang = Vector.angleBetween(o.getX(), o.getY(), x, y);
		else
		 ang = Vector.angleBetween(x, y, o.getX(), o.getY());
		
		dir = ang;
			
	}
	public void checkPrey() {
		if(prey != null && (!prey.isAlive() || !canSense(prey) || 
		  (prey.getCurrentTile() != prefType && prefType != TileType.NONE)))
			prey = null;
			
	}
	public boolean hasPrey() {
		return prey != null;
	}
	public void checkPredator() {
		if(predator != null && (!predator.isAlive() || !canSense(predator)))
			predator = null;
	}
	public boolean hasPredator() {
		return predator != null;
	}
	public void updatePrey(Lifeform l) {
		if(prey == null) {
			prey = l;
		}else if(Vector.distanceBetween((int)x, (int)y, l.getX(), l.getY()) < 
		   Vector.distanceBetween((int)x, (int)y, prey.getX(), prey.getY())) {
			prey = l;
		}
	}
	public void setPredator(Lifeform l) {
		predator = l;
	}
	public void setOnTile(TileType t) {
		onNow = t;
	}
	public void setHeadingToLeft(TileType t) {
		headingToLeft = t;
	}
	public void setHeadingToRight(TileType t) {
		headingToRight = t;
	}
	public Angle getDirection() {
		return dir;
	}
	public Vector getVel() {
		return vel;
	}
	public double getStamina() {
		return stamina;
	}
	public TileType getCurrentTile() {
		return onNow;
	}
	public TileType getPrefTile() {
		return prefType;
	}
	public double getEnergy() {
		return energy;
	}
	public Organism getBaby() {
		return new Organism(dna.replicate());
	}
	public DNA getDNACopy() {
		return dna.clone();
	}
	/***
	 * HELPERS
	 */
	public boolean canSense(Lifeform l) {
		double offset = l.getCreature() == CreatureType.ANIMAL ? Math.pow(((Organism)l).get(DNA.camoflage), 2) : 0;
	    return Vector.distanceBetween((int)x, (int)y, l.getX(), l.getY()) < (get(DNA.perception) - offset);
	}
	@Override
	public boolean isAlive() {
		return energy > 0;
	}
	
	public Rectangle getHitbox() {
		return new Rectangle((int)x, (int)y, 4, 4);
	}
	@Override
	public void kill() {		
		energy = 0;
	}
	private void bounce(boolean hor) {
		if(hor) {
			dir = Vector.angleBetween(0, 0, -vel.getX(), vel.getY());
		}else {
			dir = Vector.angleBetween(0, 0, vel.getX(), -vel.getY());
		}
	}
	@Override
	public CreatureType getCreature() {
		return CreatureType.ANIMAL;
	}
	
}

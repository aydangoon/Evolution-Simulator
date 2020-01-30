package life;
import java.awt.Rectangle;
import java.util.Random;

import settings.Constants;
import tiles.TileType;

public class Food extends Lifeform{

	private int x;
	private int y;
	private boolean alive;
	private TileType on;
	
	public Food() {
		alive = true;
		x = (int)(Math.random() * (Constants.COLUMNS - 1) * (Constants.TILE_SIDE));
		y = (int)(Math.random() * (Constants.ROWS - 1) * (Constants.TILE_SIDE));
		on = TileType.LAND;

	}
	public Food(int x, int y) {
		this.x = x;
		this.y = y;
		on = TileType.LAND;

	}
	public void setOn(TileType t) {
		on = t;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}

	public boolean isAlive() {
		return alive;
	}
	
	@Override
	public void kill() {
		alive = false;
	}
	@Override
	public Rectangle getHitbox() {
		return new Rectangle(x, y, 10, 10);
	}
	@Override
	public CreatureType getCreature() {
		return CreatureType.PLANT;
	}
	@Override
	public TileType getCurrentTile() {
		return on;
	}
	
}

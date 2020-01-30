package life;
import java.awt.Rectangle;

import tiles.TileType;

public abstract class Lifeform {

	public abstract int getX();
	public abstract int getY();
	public abstract boolean isAlive();
	public abstract void kill();
	public abstract Rectangle getHitbox();
	public abstract CreatureType getCreature();
	public abstract TileType getCurrentTile();
	
}

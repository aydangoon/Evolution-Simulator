package infoandutils;
import settings.Constants;

public class Camera {

	private int xOffset;
	private int yOffset;
	
	public Camera() {
		xOffset = 0;
		yOffset = 0;
	}
	public void updateCameraPos(int hor, int ver) {
		if(hor == -1) {
			moveLeft();
		}else if(hor == 1) {
			moveRight();
		}
		
		if(ver == -1) {
			moveUp();
		}else if(ver == 1) {
			moveDown();
		}
	}
	private void moveDown() {
		if(yOffset < (Constants.MAP_SIZE_FACTOR - 1) * Constants.GAME_HEIGHT)
			yOffset+=Constants.CAMERA_SPEED;
	}
	private void moveUp() {
		if(yOffset > 0)
			yOffset-=Constants.CAMERA_SPEED;
	}
	private void moveRight() {
		if(xOffset < (Constants.MAP_SIZE_FACTOR - 1) * Constants.GAME_WIDTH)
			xOffset+=Constants.CAMERA_SPEED;
	}
	private void moveLeft() {
		if(xOffset > 0)
			xOffset-=Constants.CAMERA_SPEED;
	}
	public int getXOffset() {
		return xOffset;
	}
	public int getYOffset() {
		return yOffset;
	}
	public int getXStartTile() {
		return xOffset / Constants.TILE_SIDE;
	}
	public int getXEndTile() {
		if(getXStartTile() + (Constants.GAME_WIDTH / Constants.TILE_SIDE) + 1 < Constants.COLUMNS)
			return getXStartTile() + (Constants.GAME_WIDTH / Constants.TILE_SIDE) + 1;
		return Constants.COLUMNS;
	}
	public int getYStartTile() {
		return yOffset / Constants.TILE_SIDE;
	}
	public int getYEndTile() {
		if(getYStartTile() + (Constants.GAME_HEIGHT / Constants.TILE_SIDE) + 1 < Constants.ROWS)
			return getYStartTile() + (Constants.GAME_HEIGHT / Constants.TILE_SIDE) + 1;
		return Constants.ROWS;
	}
	public String toString() {
		return "(" + xOffset / Constants.CAMERA_SPEED +", " + yOffset / Constants.CAMERA_SPEED + ")";
	}
}

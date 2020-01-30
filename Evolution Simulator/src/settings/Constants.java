package settings;
import java.awt.Color;

public class Constants {
	/*
	 * TODO move these to the appropriate classes
	 */
	public static final int WINDOW_WIDTH = 1500;
	public static final int WINDOW_HEIGHT = 800;
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = WINDOW_HEIGHT;
	public static final int GSH_WIDTH = 200;
	public static final int GSH_HEIGHT = WINDOW_HEIGHT;
	public static final int ID_WIDTH = 400;
	public static final int ID_HEIGHT = WINDOW_HEIGHT;
	public static final int TILE_SIDE = 20;
	public static final int MAP_SIZE_FACTOR = 8;
	public static final int ROWS = MAP_SIZE_FACTOR * GAME_WIDTH / TILE_SIDE;
	public static final int COLUMNS = ROWS;
	public static final int CAMERA_SPEED = 10;
	public static final int TILE_VARIABLITY = 20;
	public static final int REFRESH_RATE = 10;
	public static final int STARTER_SPECIES = 4;
	public static final Color FOOD_COLOR = new Color(200, 200, 0);
	public static final int TOTAL_FOOD = 30;
	public static final int NUMBER_OF_ORGANISMS = 4;
	public static final int FOOD_RADIUS = 8;
	public static final int CARNIVORE_LEVEL = 5;
	public static final double STRICTLY_CARNIVORE = 7.5;
	public static final double[] MAX_VALUES = new double[] {10, 10, 5, 5, 200, 200, 10};
}

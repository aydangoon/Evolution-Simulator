package tiles;
import java.awt.Color;
import java.util.Random;

import settings.Constants;

public class Tile {

	private int depth;
	private int row;
	private int column;
	private Color color;
	
	public Tile() {
		
	}
	public Tile(int c, int r, int d) {
		row = r;
		column = c;
		depth = d;
	}
	public Tile(int c, int r, Tile[][] board) {
		row = r;
		column = c;
		generateColor(board, false);
	}
	public void generateColor(Tile[][] board, boolean disc) {
		int dOffset = getRandom();
		double pSum = 0;
		double nSum = 0;
		double pcount = 0;
		double ncount = 0;
		for(int c = column - 1; c <= column + 1; c++) {
			for(int r = row - 1; r <=  row + 1; r++) {
				if(c >= 0 && c < Constants.COLUMNS && r >= 0 && r < Constants.ROWS 
				   && !(r==row && c==column) && board[c][r] != null) {
					if(board[c][r].getDepth() >= 0) {
						pcount++;
						pSum+=board[c][r].getDepth();
					}else {
						ncount++;
						nSum+=board[c][r].getDepth();
					}
				}
			}
		}
		
		if(disc && (ncount > 4 || pcount > 4)){
			if(ncount > pcount)
				depth = (int)(nSum / ncount);
			else
				depth = (int)(pSum / pcount);
		}else {
			depth = (int)((nSum + pSum) / (ncount + pcount)) + dOffset;
		}
		
		if(depth < -255)
			depth = -255;
		if(depth > 255)
			depth = 255;
		
	}
	public void assertHue() {
		depth = Math.abs(2 * depth) > 255 ? (depth / Math.abs(depth)) * 255 : 2 * depth;
		
		int green;
		int blue;
		if(depth > 0) {
			green = 255 - depth > 70 ? 255 - depth : 70;
			blue = green - 70 > 10 ? green - 70 : 10;
		}else {
			blue = 255 - Math.abs(depth) > 70 ? 255 - Math.abs(depth) : 70;
			green = blue - 70 > 10 ? blue - 70 : 10;
		}
		color = new Color(0, green, blue);
	}
	public int getDepth() {
		return depth;
	}
	private static int getRandom() {

		int amount = new Random().nextInt(Constants.TILE_VARIABLITY);
		
		if(new Random().nextBoolean())
			return amount * -1;
		return amount;
	}
	public String toString() {
		return "c: " + column + " r: " + row;
	}
	public Color getColor() {	
		return color;
	}
	public TileType getType() {
		return depth >= 0 ? TileType.LAND : TileType.SEA;
	}
}

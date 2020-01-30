package vectormath;

public class Angle {
	
	private double degrees;
	
	public Angle() {
		degrees = 0;
	}
	public Angle(double d) {
		degrees = d % 360;
	}
	
	public void shift(double delta) {
		degrees = (degrees + delta) % 360;
	}
	
	public double get() {
		return degrees;
	}
	
	public String toString() {
		return ""+ (int)degrees;
	}
	
	public static double sin(double d) {
		return -1 * Math.sin(Math.toRadians(d));
	}
	public static double cos(double d) {
		return Math.cos(Math.toRadians(d));
	}
	
}

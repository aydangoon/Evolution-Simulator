package vectormath;

public class Vector {
	
	private double x;
	private double y;
	
	public Vector() {
		x = 1;
		y = 1;
	}
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public Vector(double m, Angle angle) {
		this.x = m * Angle.cos(angle.get());
		this.y = m * Angle.sin(angle.get());
	}
	public void shift(Vector oV) {
		this.x += oV.getX();
		this.y += oV.getY();
	}
	public void scale(double c) {
		x *= c;
		y *= c;
	}
	public void flipX() {
		x = -x;
	}
	public void flipY() {
		y = -y;
	}
	public void setMagnitude(double m) {
		x = m * Angle.cos(getAngle().get());
		y = m * Angle.sin(getAngle().get());
	}
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	public Angle getAngle() {
		return angleBetween(0, 0, x, y);
	}
	public double getMagnitude() {
		return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
    public static Angle angleBetween(double x1, double y1, double x2, double y2) {
    	double yDiff = -1 * (y2 - y1);
    	double xDiff = (x2 - x1);
    	double angleInRadians = Math.atan2(yDiff, xDiff);

    	return new Angle(Math.toDegrees(angleInRadians)); 		
	}
    public static double distanceBetween(int x1, int y1, int x2, int y2) {
    	return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2-y1, 2));
    }
    public String toString() {
    	return "[" + x + ", " + y +"]";
    }
}

package infoandutils;

public class Date {

	private int day;
	private int hour;
	private int minute;
	
	public Date(){
		day = 0;
		hour = 0;
		minute = 0;
	}
	
	public DateBroadcast timeStep() {
		minute = (minute + 1) % 60;
		if(minute == 0) {
			hour = (hour + 1) % 24;
			if(hour == 0) {
				day++;
				return DateBroadcast.NEW_DAY;
			}
			return DateBroadcast.HOUR_PASSED;
		}
		return DateBroadcast.NONE;
	}
	public String getDate() {
		return "Day: " + day + " Hour: " + hour;
	}
	
}

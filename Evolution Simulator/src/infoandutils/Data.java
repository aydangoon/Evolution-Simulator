package infoandutils;
import life.DNA;

public class Data {

	private DNA avgDNA;
	private int population;
	
	public Data(DNA avgDNA, int population) {
		
		this.avgDNA = avgDNA;
		this.population = population;
		
	}
	
	public double get(int i) {
		return avgDNA.get(i);
	}
	
	public int getPopulation() {
		return population;
	}
	
}

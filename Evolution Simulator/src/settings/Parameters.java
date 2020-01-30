package settings;

import life.DNA;

public class Parameters {

	private double[] costCoefficients;
	private int totalFood;
	private double replicationCost;
	
	public Parameters() {
		costCoefficients = new double[7];
		for(int i = 0; i < 7; i++)
			costCoefficients[i] = 1;
		totalFood = 100;
		replicationCost = 0.01;
	}
	public void setCost(int ind, double val) {
		if(ind >= 0 && ind < costCoefficients.length)
			costCoefficients[ind] = val;
	}
	public double get(int ind){
		if(ind >= 0 && ind < costCoefficients.length)
			return costCoefficients[ind];
		return 0;
	}
	public void setTotalFood(int i) {
		totalFood = i;
	}
	public int getTotalFood() {
		return totalFood;
	}
	public void setReplicationCost(double i) {
		replicationCost = i;
	}
	public double getReplicationCost() {
		return replicationCost;
	}
	
	public double costPerHour(DNA dna) {
		return 0.1 * ((costCoefficients[DNA.land_speed] * Math.pow(dna.get(DNA.land_speed), 1.5)) +
			   (costCoefficients[DNA.water_speed] * Math.pow(dna.get(DNA.water_speed), 1.5)) + 
			   (costCoefficients[DNA.stamina] * dna.get(DNA.stamina)/10) + 
			   (costCoefficients[DNA.carnivore] * (2*Math.cos(Math.PI * (dna.get(DNA.carnivore)) / 5) + 5)) + 
		   	   (costCoefficients[DNA.camoflage] * Math.pow(dna.get(DNA.camoflage), 2)) + 
		   	   (costCoefficients[DNA.poison_resistance] * Math.pow(dna.get(DNA.poison_resistance), 2)) + 
		   	   (costCoefficients[DNA.perception] * dna.get(DNA.perception)));
	}
	public String toString() {
		String s = "[";
		for(double d : costCoefficients) {
			s += d + ", ";
		}
		s+="]";
		return s;
	}
}

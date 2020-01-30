package life;
import java.util.ArrayList;
import settings.Constants;

public class DNA {

	private double[] traits;
	
	public static final int carnivore = 0;
	public static final int camoflage = 1;
	public static final int land_speed = 2;
	public static final int water_speed = 3;
	public static final int perception = 4;
	public static final int stamina = 5;
	public static final int poison_resistance = 6;
	
	public DNA() {
		traits = new double[7];
		for(int i = 0; i < traits.length; i++)
			traits[i] = Math.random() * Constants.MAX_VALUES[i];

	}
	
	public DNA(double[] d) {
		traits = d;
		vet();
	}
	
	public void set(int index, double d) {
		if(index >= 0 && index < 7) {
			traits[index] = d;
		}
		vet();
	}
	public double get(int index) {
		if(index >= 0 && index < 7)
			return traits[index];
		return traits[0];
	}
	private void vet() {
		for(int i = 0; i < traits.length; i++)
			if(traits[i] < 0)
				traits[i] = 0;
			else if(traits[i] > Constants.MAX_VALUES[i])
				traits[i] = Constants.MAX_VALUES[i];

	}
	public DNA replicate() {
		double[] newTraits = traits.clone();
		for(int i = 0; i < newTraits.length; i++)
			newTraits[i] += (Math.random() > 0.5 ? -1 : 1) * (Constants.MAX_VALUES[i] * 0.2 * Math.random());
		
		return new DNA(newTraits);
		
	}
	@Override()
	public DNA clone() {
		return new DNA(traits.clone());
	}

	public static DNA average(ArrayList<DNA> popGenes) {
		double[] newTraits = new double[] {0,0,0,0,0,0,0};
		for(DNA d : popGenes) {
			for(int i = 0; i < 7; i++)
				newTraits[i] = newTraits[i] + d.get(i);
		}
		if(popGenes.size() > 0) {
			for(int i = 0; i < newTraits.length; i++) {
				newTraits[i] /= popGenes.size();
			}
		}
		return new DNA(newTraits);
	}
	
	public static String getTraitName(int i) {
		switch(i) {
		case 0: return "carnivore";
		case 1: return "camoflage";
		case 2: return "land speed";
		case 3: return "water speed";
		case 4: return "perception";
		case 5: return "stamina";
		default: return "poison resistance";
		}
	}
	
	
}

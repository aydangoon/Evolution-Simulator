package displays;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class TraitSlider extends JSlider{

	public TraitSlider(int min, int max, int val, double factor, String name, JPanel goesOn) {
		super(min, max, val);
		this.setFocusable(false);
		this.setBorder(BorderFactory.createTitledBorder(name + ": " + val / factor));
		this.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				setBorder(BorderFactory.createTitledBorder(name + ": " + getValue() / factor));
			}
			
		});
		goesOn.add(this);
	}
	
	
}

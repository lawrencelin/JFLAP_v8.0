package view.action.newactions;

import view.pumping.PumpingLemmaChooser;
import view.pumping.RegPumpingLemmaChooser;

/**
 * Creates a new RegPumpingLemmaChooser from the main menu.
 * 
 * @author Ian McMahon
 *
 */
public class NewRegPumpingLemmaAction extends NewAction<PumpingLemmaChooser>{

	public NewRegPumpingLemmaAction() {
		super("Regular Pumping Lemma");
	}

	@Override
	public PumpingLemmaChooser createNewModel() {
		return new RegPumpingLemmaChooser();
	}

}

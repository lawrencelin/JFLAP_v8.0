package view.action.newactions;

import view.pumping.CFPumpingLemmaChooser;
import view.pumping.PumpingLemmaChooser;

/**
 * Creates a new CFPumpingLemmaChooser from the main menu.
 * 
 * @author Ian McMahon
 *
 */
public class NewCFPumpingLemmaAction extends NewAction<PumpingLemmaChooser>{

	public NewCFPumpingLemmaAction() {
		super("Context-Free Pumping Lemma");
	}

	@Override
	public PumpingLemmaChooser createNewModel() {
		return new CFPumpingLemmaChooser();
	}


}

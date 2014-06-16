package file.xml.formaldef.components.states;

import model.automata.StateSet;
import model.automata.acceptors.FinalStateSet;

public class FinalStateSetTransducer extends StateSetTransducer {


	@Override
	public StateSet createEmptyComponent() {
		return new FinalStateSet();
	}

	@Override
	public String getTag() {
		return FINAL_STATESET_TAG;
	}

	
}

package oldnewstuff.view.menus.old;


import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;

import jflap.actions.SetFinalStateAction;
import jflap.model.automaton.State;




public class SetFinalStateCheckBox extends ModifyStateCheckbox {


	public SetFinalStateCheckBox(State s) {
		super(s, new SetFinalStateAction(s));
	}

	@Override
	public boolean isSelected() {
		if (this.getModifyingState() != null)
			return getModifyingState().isFinal();
		return false;
	}

}

package oldnewstuff.view.menus.old;


import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;

import jflap.model.automaton.State;



public abstract class ModifyStateCheckbox extends JCheckBoxMenuItem {

	private State myState;

	public ModifyStateCheckbox(State s, Action a){
		super(a);
		setModifyingState(s);
		this.setSelected(this.isSelected());
	}

	public void setModifyingState(State s) {
		myState = s;
	}
	
	public State getModifyingState(){
		return myState;
	}

	@Override
	public abstract boolean isSelected();
	
	
	
}

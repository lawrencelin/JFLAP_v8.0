package view.action.newactions;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import universe.JFLAPUniverse;
import view.ViewFactory;

public abstract class NewAction<T> extends AbstractAction {

	public NewAction(String name) {
		super(name);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object model = createNewModel();
		if (model != null) {
			Component c = ViewFactory.createView(model);
			JFLAPUniverse.registerEnvironment(c);
		}
	}

	public abstract T createNewModel();

	public static NewAction[] getAllNewActions() {
		return new NewAction[] {
				new NewFSAAction(),
				new NewMealyAction(),
				new NewMooreAction(),
				new NewPDAAction(), 
				new NewMultiTapeTMAction(),
				new NewBlockTMAction(),
				new NewGrammarAction(), 
//				new NewSetsAction(),
				new NewLSystemAction(),
				new NewRegexAction(),
				new NewRegPumpingLemmaAction(), 
				new NewCFPumpingLemmaAction() };
	}

}

package view.action.newactions;

import model.lsystem.LSystem;

/**
 * Creates a new L System from the main menu.
 * 
 * @author Ian McMahon
 *
 */
public class NewLSystemAction extends NewAction<LSystem>{

	public NewLSystemAction() {
		super("L-System");
	}

	@Override
	public LSystem createNewModel() {
		return new LSystem();
	}

}

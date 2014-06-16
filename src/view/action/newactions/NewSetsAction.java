package view.action.newactions;

import model.sets.SetsManager;

@SuppressWarnings("serial")
public class NewSetsAction extends NewAction<SetsManager> {

	public NewSetsAction () {
		super("Sets");
	}

	@Override
	public SetsManager createNewModel() {
		return new SetsManager();
	}

}

package view.sets.state;


import javax.swing.JComponent;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import view.sets.ParameterConstructionHelper;
import view.sets.SetDefinitionView;
import view.sets.SetsDropdownMenu;

public class UseExistingState extends State {

	private UndoKeeper myKeeper;
	private SetsDropdownMenu mySource;
	private AbstractSet mySet;

	public UseExistingState (SetsDropdownMenu source, UndoKeeper keeper) {
		mySource = source;
		myKeeper = keeper;
	}


	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {
		Class c = mySource.getSelectedSetClass();
		JComponent params = ParameterConstructionHelper.createInputPanel(c);
		if (params == null) {
			mySet = (AbstractSet) c.newInstance();
		}
		return mySet;
	}

	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.remove(mySet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.add(mySet);
		return true;
	}



	@Override
	public SetDefinitionView createDefinitionView() {

		return new ExistingView(mySet, myKeeper);

	}


	private class ExistingView extends SetDefinitionView {

		private AbstractSet mySet;

		public ExistingView(AbstractSet set, UndoKeeper keeper) {
			super(keeper);
			mySet = set;
			updateDefinitionPanel();
		}

		@Override
		public void updateDefinitionPanel() {
			myNameTextField.setText(mySet.getName());
			myDescriptionTextField.setText(mySet.getDescription());
			myElementsTextField.setText(mySet.getSetAsString());

			setFieldsEditable(false);

		}

	}
}

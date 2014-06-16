package view.sets.state;

import java.util.Set;

import debug.JFLAPDebug;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.SetsManager;
import model.sets.elements.Element;
import model.sets.elements.ElementsParser;
import model.undo.UndoKeeper;
import view.sets.SetDefinitionView;

/**
 * State for creating a new set entirely
 * 
 *
 */
public class CreateState extends State {

	private AbstractSet mySet;
	private UndoKeeper myKeeper;

	private SetDefinitionView myDefinition;

	public CreateState(UndoKeeper keeper) {
		myKeeper = keeper;
	}


	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {

		String name = myDefinition.getNameOfSet();
		if (name == null)
			name = SetsManager.getAutomatedName();
		String description = myDefinition.getDescriptionOfSet();
		ElementsParser parser = new ElementsParser(myDefinition.getElements());
		Set<Element> elements = parser.parse();
		if (description == null) {
			mySet = new FiniteSet(name, elements);
		}
		else {
			mySet = new FiniteSet(name, description, elements);
		}

		JFLAPDebug.print("New set created");
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
		return false;
	}


	@Override
	public SetDefinitionView createDefinitionView() {
		myDefinition = new CreateDefinitionView(myKeeper);
		return myDefinition;
	}


	private class CreateDefinitionView extends SetDefinitionView {

		public CreateDefinitionView(UndoKeeper keeper) {
			super(keeper);
		}

		@Override
		public void updateDefinitionPanel() {

		}

	}

}

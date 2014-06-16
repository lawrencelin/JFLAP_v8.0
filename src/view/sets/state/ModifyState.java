package view.sets.state;


import java.awt.BorderLayout;
import java.util.Set;

import debug.JFLAPDebug;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.SetsManager;
import model.sets.elements.Element;
import model.sets.elements.ElementsParser;
import model.undo.UndoKeeper;
import view.sets.PropertiesPanel;
import view.sets.SetDefinitionView;

/**
 * State for modifying an existing set
 *
 *
 */
public class ModifyState extends State {

	private SetDefinitionView myDefinition;
	private UndoKeeper myKeeper;
	
	private AbstractSet myOriginalSet;
	private AbstractSet myModifiedSet;

	public ModifyState(AbstractSet set, UndoKeeper keeper) {
		myOriginalSet = set;
		myKeeper = keeper;
	}

	@Override
	public AbstractSet finish(UndoKeeper keeper) throws Exception {
		SetsManager.ACTIVE_REGISTRY.remove(myOriginalSet);
//		if (myOriginalSet.isFinite()) {
//
//			String name = mySource.getSetName() == null ? CreateState.getAutomatedName() : mySource.getSetName();
//			String description = mySource.getDescription();
//			if (mySource.getElements() == null || mySource.getElements().trim().length() == 0) {
//				throw new Exception("Set must contain at least one element!");
//			}
//
//			ElementsParser parser = new ElementsParser(mySource.getElements());
//			try {
//				Set<Element> elements = parser.parse();
//
//				if (description == null)
//					myModifiedSet = new FiniteSet(name, elements);
//				myOriginalSet = new FiniteSet(name, description, elements);
//			} catch (JFLAPException e) {
//
//			} catch (Exception e) {
//
//			}
//
//		}
//		return myOriginalSet;

		myModifiedSet = (AbstractSet) myOriginalSet.copy();
		
		String name = myDefinition.getNameOfSet();
		if (name != null) {
			myModifiedSet.setName(name);
		}
		
		myModifiedSet.setDescription(myDefinition.getDescriptionOfSet());
		
		ElementsParser parser = new ElementsParser(myDefinition.getElements());
		if (myModifiedSet.isFinite()) {
			((FiniteSet) myModifiedSet).setElements(parser.parse());
		}
		
		return myModifiedSet;

	}


	@Override
	public boolean undo() {
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.add(myOriginalSet);
		return true;
	}

	@Override
	public boolean redo() {
		SetsManager.ACTIVE_REGISTRY.add(myModifiedSet);
		SetsManager.ACTIVE_REGISTRY.remove(myModifiedSet);
		return true;
	}

	@Override
	public SetDefinitionView createDefinitionView() {
		JFLAPDebug.print("Building view from set: " + myOriginalSet.toString());
		myDefinition = new ModifyingDefinitionView(myOriginalSet, myKeeper);
		return myDefinition;
	}


	private class ModifyingDefinitionView extends SetDefinitionView {
		
		private AbstractSet mySet;
		
		public ModifyingDefinitionView(AbstractSet set, UndoKeeper keeper) {
			super(keeper);
			mySet = set;
			updateDefinitionPanel();
		}

		@Override
		public void updateDefinitionPanel() {
			myNameTextField.setText(mySet.getName());
			myDescriptionTextField.setText(mySet.getDescription());
			myElementsTextField.setText(mySet.getSetAsString());
			add(new PropertiesPanel(mySet), BorderLayout.SOUTH);
			setFieldsEditable(true);
		
		}
	}
}

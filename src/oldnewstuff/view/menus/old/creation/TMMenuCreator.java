package oldnewstuff.view.menus.old.creation;


import java.util.List;

import javax.swing.JMenuItem;

import jflap.actions.BuildingBlockSimulateAction;
import jflap.actions.ConvertPDAToGrammarAction;
import jflap.actions.MultipleOutputSimulateAction;
import jflap.actions.SimulateNoClosureAction;
import jflap.actions.TuringToUnrestrictGrammarAction;
import jflap.controller.JFLAPController;




public class TMMenuCreator extends AutomataMenuCreator {
	
	
	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return combineActionLists(super.getConvertMenuComponents(),
				new JMenuItem(new TuringToUnrestrictGrammarAction()));
	}
}

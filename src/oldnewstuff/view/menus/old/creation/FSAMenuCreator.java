package oldnewstuff.view.menus.old.creation;


import java.util.List;

import javax.swing.JMenuItem;

import jflap.actions.ConvertFSAToGrammarAction;
import jflap.actions.ConvertFSAToREAction;
import jflap.actions.ConvertPDAToGrammarAction;
import jflap.actions.DFAEqualityAction;
import jflap.actions.MinimizeTreeAction;
import jflap.actions.NFAToDFAAction;
import jflap.actions.SimulateNoClosureAction;
import jflap.controller.JFLAPController;




public class FSAMenuCreator extends AutomataMenuCreator {

	@Override
	public List<JMenuItem> getTestMenuComponents() {
		return combineActionLists(super.getTestMenuComponents(),
							new JMenuItem (new DFAEqualityAction()));
	}

	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return combineActionLists(super.getConvertMenuComponents(),
				new JMenuItem(new NFAToDFAAction()),
				new JMenuItem(new MinimizeTreeAction()),
				new JMenuItem(new ConvertFSAToGrammarAction()),
				new JMenuItem(new ConvertFSAToREAction()));
	}

}

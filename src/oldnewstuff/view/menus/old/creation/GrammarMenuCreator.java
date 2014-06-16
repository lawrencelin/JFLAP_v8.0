package oldnewstuff.view.menus.old.creation;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import jflap.actions.AddGroupingAction;
import jflap.actions.BruteParseAction;
import jflap.actions.CYKParseAction;
import jflap.actions.ConvertCFGLL;
import jflap.actions.ConvertCFGLR;
import jflap.actions.ConvertRegularGrammarToFSA;
import jflap.actions.GrammarTypeTestAction;
import jflap.actions.LLParseTableAction;
import jflap.actions.LRParseTableAction;
import jflap.actions.MultipleBruteParseAction;
import jflap.actions.MultipleCYKParseAction;
import jflap.actions.SortProductionsAction;
import jflap.actions.UserControlParseAction;
import jflap.controller.JFLAPController;




public class GrammarMenuCreator extends FormalDefinitionMenuCreator {

	@Override
	public List<JMenuItem> getInputMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(),
				new JMenuItem(new LLParseTableAction()),
				new JMenuItem(new LRParseTableAction()),
				new JMenuItem(new BruteParseAction()),
				new JMenuItem(new MultipleBruteParseAction()),
				new JMenuItem(new UserControlParseAction()),
				new JMenuItem(new CYKParseAction()),
				new JMenuItem(new MultipleCYKParseAction()));
	}

	
	
	@Override
	public List<JMenuItem> getModifyMenuComponents() {
		return combineActionLists(super.getModifyMenuComponents(),
				new JMenuItem(new SortProductionsAction()),
				new JMenuItem(new AddGroupingAction()));
	}



	@Override
	public List<JMenuItem> getTestMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(),
				new JMenuItem(new GrammarTypeTestAction()));
	}

	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(),
				new JMenuItem(new ConvertCFGLL()),
				new JMenuItem(new ConvertCFGLR()),
				new JMenuItem(new ConvertRegularGrammarToFSA()));
	}

}

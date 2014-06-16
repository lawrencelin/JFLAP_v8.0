package oldnewstuff.view.menus.old.creation;


import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import oldview.menus.PrimaryMenuCreator;

import jflap.actions.TrimAlphabetsAction;
import jflap.actions.delete.DeleteSelectionAction;
import jflap.actions.delete.DeleteSelectionMenuAction;
import jflap.actions.undo.RedoAction;
import jflap.actions.undo.UndoAction;
import jflap.controller.JFLAPController;




public abstract class FormalDefinitionMenuCreator extends PrimaryMenuCreator {

	@Override
	public List<JMenuItem> getModifyMenuComponents() {
			return combineActionLists(new ArrayList<JMenuItem>(), 
					new JMenuItem(new UndoAction()), 
					new JMenuItem(new RedoAction()), 
					new JMenuItem(new DeleteSelectionMenuAction()),
					new JMenuItem(new TrimAlphabetsAction()));
					
	}

	
	
}

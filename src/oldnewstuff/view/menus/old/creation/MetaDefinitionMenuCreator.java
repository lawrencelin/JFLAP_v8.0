package oldnewstuff.view.menus.old.creation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import oldview.formaldef.definitioncreator.importing.ImportMenu;
import oldview.formaldef.definitioncreator.interaction.toolbar.ClearAlphabetAction;
import oldview.formaldef.definitioncreator.interaction.toolbar.CopyAlphabetAction;
import oldview.formaldef.definitioncreator.interaction.toolbar.PasteAlphabetAction;
import oldview.menus.PrimaryMenuCreator;

import jflap.actions.ConvertFSAToGrammarAction;
import jflap.actions.ConvertFSAToREAction;
import jflap.actions.MinimizeTreeAction;
import jflap.actions.NFAToDFAAction;
import jflap.actions.undo.RedoAction;
import jflap.actions.undo.UndoAction;
import jflap.actions.undo.UndoableAction;



public class MetaDefinitionMenuCreator extends PrimaryMenuCreator {

	@Override
	public List<JMenuItem> getInputMenuComponents() {
		return null;
	}

	@Override
	public List<JMenuItem> getModifyMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(),
				new JMenuItem(new UndoAction()),
				new JMenuItem(new RedoAction()),
				new ImportMenu(),
				new JMenuItem(new CopyAlphabetAction()),
				new JMenuItem(new ClearAlphabetAction()),
				new JMenuItem(new PasteAlphabetAction()));
	}

	@Override
	public List<JMenuItem> getTestMenuComponents() {
		return null;
	}

	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return null;
	}
	
	

}

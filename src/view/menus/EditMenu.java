package view.menus;

import java.awt.Component;

import javax.swing.Action;
import javax.swing.JMenu;

import model.grammar.StartVariable;
import model.undo.UndoKeeper;
import util.ISelector;
import view.EditingPanel;
import view.action.edit.DeleteAction;
import view.action.grammar.SortProductionsAction;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.undoing.AutomataRedoAction;
import view.automata.undoing.AutomataUndoAction;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;
import view.grammar.GrammarView;
import view.grammar.LanguageGeneratorView;
import view.grammar.parsing.ParserView;
import view.grammar.productions.ProductionTable;
import view.grammar.productions.ProductionTableModel;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;
import view.undoing.UndoRelatedMenuItem;
import view.undoing.redo.MenuRedoAction;
import view.undoing.redo.RedoAction;
import view.undoing.redo.RedoButton;
import view.undoing.undo.MenuUndoAction;
import view.undoing.undo.UndoAction;
import view.undoing.undo.UndoButton;

public class EditMenu extends JMenu implements TabChangeListener {

	public EditMenu(JFLAPEnvironment e) {
		super("Edit");
		e.addTabListener(this);
		this.update(e.getCurrentView());
	}

	public Action[] createActions(JFLAPEnvironment e) {
		return new Action[] { new MenuUndoAction(e), new MenuRedoAction(e) };
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());

	}

	private void update(Component view) {
		this.removeAll();
		UndoKeeper keeper = null;
		
		if (view instanceof EditingPanel
				&& isValidEditingPanel(view)) {
			keeper = ((EditingPanel) view).getKeeper();
			this.setVisible(true);
		} else {
			this.setVisible(false);
		}
		if (keeper != null) {
			UndoAction undo = new UndoAction(keeper);
			RedoAction redo = new RedoAction(keeper);
			
			if(view instanceof AutomatonView){
				AutomatonView aView = (AutomatonView) view;
				AutomatonEditorPanel panel = (AutomatonEditorPanel) aView.getCentralPanel();
				
				undo = new AutomataUndoAction(keeper, panel);
				redo = new AutomataRedoAction(keeper, panel);
			}
			
			this.add(new UndoRelatedMenuItem(redo));
			this.add(new UndoRelatedMenuItem(undo));
		}
		// Grammar Options
		if (view instanceof GrammarView) {
			GrammarView v = (GrammarView) view;
			ProductionTable table = (ProductionTable) v.getCentralPanel();
			this.add(new DeleteAction(table));
			this.add(new SortProductionsAction(keeper,
					(ProductionTableModel) table.getModel()));
		}

	}
	
	private boolean isValidEditingPanel(Component view){
		return !(view instanceof PumpingLemmaInputView || view instanceof PumpingLemmaChooserView)
		&& !(view instanceof ParserView)
		&& !(view instanceof LanguageGeneratorView);
	}

}

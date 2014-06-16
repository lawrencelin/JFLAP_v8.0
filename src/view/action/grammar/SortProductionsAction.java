package view.action.grammar;

import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.AbstractAction;

import model.change.events.UndoableEvent;
import model.grammar.Production;
import model.grammar.Variable;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

import view.action.UndoingAction;
import view.grammar.productions.ProductionDataHelper;
import view.grammar.productions.ProductionTableModel;

public class SortProductionsAction extends UndoingAction implements IUndoRedo{

	private ProductionTableModel myModel;
	private Production[] oldOrdering;
	private Production[] newOrdering;

	public SortProductionsAction(UndoKeeper keeper, ProductionTableModel model) {
		super("Sort Productions", keeper);
		myModel = model;
	}

	@Override
	public IUndoRedo createEvent(ActionEvent e) {
		oldOrdering = myModel.getOrderedProductions();
		newOrdering = Arrays.copyOf(oldOrdering, oldOrdering.length);
		Arrays.sort(newOrdering, createComparator());
		return this;
	}

	public Comparator<? super Production> createComparator() {
		return new Comparator<Production>() {
			@Override
			public int compare(Production o1, Production o2) {
				Variable start = myModel.getGrammar().getStartVariable();
				if (o1.isStartProduction(start) && !o2.isStartProduction(start))
					return -1;
				if (!o1.isStartProduction(start) && o2.isStartProduction(start))
					return 1;
				return o1.compareTo(o2);
			}
		};
	}

	@Override
	public boolean undo() {
		return myModel.applyOrdering(oldOrdering);
	}

	@Override
	public boolean redo() {
		return myModel.applyOrdering(newOrdering);
	}

	@Override
	public String getName() {
		return (String) super.getValue(NAME);
	}
	

}

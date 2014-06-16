package oldnewstuff.model.change.interactions;

import java.util.List;
import java.util.Set;

import oldnewstuff.model.change.ChangeDistributor;
import oldnewstuff.model.change.ChangeEvent;

import debug.JFLAPDebug;

import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.SetComponentEvent;
import model.change.events.UndoableEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.symbols.Symbol;

public class DefaultRemoveProductionInteraction extends Interaction {

	private Grammar myGrammar;

	public DefaultRemoveProductionInteraction(Grammar distributor) {
		super(ITEM_REMOVE, distributor);
		myGrammar = distributor;
	}

	@Override
	protected void addAndApplyInteractions(ChangeEvent e,
			CompoundUndoableChangeEvent event) {
		SetComponentEvent<Production> e2 = (SetComponentEvent<Production>) e;
		List<Production> items = e2.getItems();
		VariableAlphabet vars = myGrammar.getVariables();
		TerminalAlphabet terms = myGrammar.getTerminals();
		for (Production p : items){
			Set<Variable> used = p.getVariablesUsed();
			Set<Symbol> allUsed = myGrammar.getUniqueSymbolsUsed(vars);
			used.removeAll(allUsed);
			if (!used.isEmpty()){
				UndoableEvent temp = vars.createRemoveEvent(used.toArray(new Symbol[0]));
				event.addSubEvents(temp);
				temp.applyChange();
			}
			Set<Terminal> used2 = p.getTerminalsUsed();
			allUsed = myGrammar.getUniqueSymbolsUsed(terms);
			used2.removeAll(allUsed);
			if (!used.isEmpty()){
				UndoableEvent temp = terms.createRemoveEvent(used2.toArray(new Symbol[0]));
				event.addSubEvents(temp);
				temp.applyChange();
			}

		}
	}
}

package oldnewstuff.model.change.interactions;

import java.util.List;

import oldnewstuff.model.change.ChangeDistributor;
import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.ChangeListener;

import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.SetComponentEvent;
import model.change.events.UndoableEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CustomModeSymbolRemovedInteraction extends Interaction implements ChangeListener{

	private Grammar myGrammar;
	private CompoundUndoableChangeEvent myEvent;

	public CustomModeSymbolRemovedInteraction(Grammar distributor) {
		super(ITEM_REMOVE, distributor);
		myGrammar = distributor;
	}

	@Override
	protected void addAndApplyInteractions(ChangeEvent e,
			CompoundUndoableChangeEvent event) {
		List<Symbol> removed = ((SetComponentEvent<Symbol>) e).getItems();
		myEvent = event;
		ProductionSet prods = myGrammar.getProductionSet();
		for (Symbol s: removed){
			for (Production p: prods){
				p.addListener(this);
				SymbolString rhs = p.getRHS();
				SymbolString lhs = p.getLHS();
				rhs = rhs.replaceAll(s);
				lhs = lhs.replaceAll(s);
				p.setTo(new Production(lhs, rhs));
				p.removeListener(this);
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		myEvent.addSubEvents((UndoableEvent) event);
	}

}

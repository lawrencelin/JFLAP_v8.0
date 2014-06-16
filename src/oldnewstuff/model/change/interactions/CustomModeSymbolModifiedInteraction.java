package oldnewstuff.model.change.interactions;

import java.util.List;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.ChangeListener;

import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.change.events.UndoableEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CustomModeSymbolModifiedInteraction extends Interaction implements ChangeListener{

	private Grammar myGrammar;
	private CompoundUndoableChangeEvent myEvent;

	public CustomModeSymbolModifiedInteraction(Grammar distributor) {
		super(ITEM_MODIFY, distributor);
		myGrammar = distributor;
	}

	@Override
	protected void addAndApplyInteractions(ChangeEvent e,
			CompoundUndoableChangeEvent event) {
		SetComponentModifyEvent<Symbol> e2 = ((SetComponentModifyEvent<Symbol>) e);
		myEvent = event;
		ProductionSet prods = myGrammar.getProductionSet();
		for (Production p: prods){
			p.addListener(this);
			SymbolString rhs = p.getRHS();
			SymbolString lhs = p.getLHS();
			Symbol s = e2.getItems().get(0);
			rhs = rhs.replaceAll(s, e2.getModifiedItem());
			lhs = lhs.replaceAll(s, e2.getModifiedItem());
			p.setTo(new Production(lhs, rhs));
			p.removeListener(this);
		}
	}

	@Override
	public void stateChanged(ChangeEvent event) {
		myEvent.addSubEvents((UndoableEvent) event);
	}

}
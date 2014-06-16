package model.algorithms.conversion.gramtoauto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import util.UtilFunctions;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.StartVariable;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CFGtoPDAConverterLR extends CFGtoPDAConverter {

	public CFGtoPDAConverterLR(Grammar g) {
		super(g);
	}

	@Override
	public String getSubtype() {
		return "LR";
	}

	@Override
	protected boolean setUpTransitions() {
		Symbol bos = this.getConvertedAutomaton().getBottomOfStackSymbol();
		Variable start = this.getGrammar().getStartVariable();
		TransitionSet<PDATransition> transitions = this.getConvertedAutomaton().getTransitions();
		
		
		PDATransition initial = new PDATransition(getStartState(),
													getMiddleState(), 
													new SymbolString(),
													new SymbolString(start), 
													new SymbolString());
		
		PDATransition toFinal = new PDATransition(getMiddleState(), 
													getFinalState(),
													new SymbolString(), 
													new SymbolString(bos), 
													new SymbolString());
		
		transitions.add(initial);
		transitions.add(toFinal);
		
		PDATransition[] loops = createAllPushLoops();
		
		return transitions.addAll(Arrays.asList(loops));
	}

	@Override
	public PDATransition convertProduction(Production p) {
		State start = this.getStartState();
		SymbolString input = new SymbolString(),
						pop = new SymbolString(UtilFunctions.reverse(p.getRHS())),
						push = new SymbolString(p.getLHS());
		
		return new PDATransition(start, start, input, pop, push);
	}
	
	private PDATransition[] createAllPushLoops(){
		ArrayList<PDATransition> trans = new ArrayList<PDATransition>();
		State start = this.getStartState();
		for (Symbol s: this.getGrammar().getTerminals()){
			trans.add(new PDATransition(start,
										start,
										new SymbolString(s),
										new SymbolString(),
										new SymbolString(s)));
		}
		
		return trans.toArray(new PDATransition[0]);
	}

}

package model.algorithms.conversion.gramtoauto;

import java.util.ArrayList;
import java.util.Arrays;

import model.automata.State;
import model.automata.SingleInputTransition;
import model.automata.TransitionSet;
import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PDATransition;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.StartVariable;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CFGtoPDAConverterLL extends CFGtoPDAConverter {

	public CFGtoPDAConverterLL(Grammar g) {
		super(g);
	}

	@Override
	public String getSubtype() {
		return "LL";
	}

	@Override
	public PDATransition convertProduction(Production p) {
		State focus = this.getMiddleState();
		SymbolString input = new SymbolString(),
						pop = new SymbolString(p.getLHS()),
						push = new SymbolString(p.getRHS());
		
		return new PDATransition(focus, focus, input, pop, push);
	}

	@Override
	protected boolean setUpTransitions() {
		Symbol bos = this.getConvertedAutomaton().getBottomOfStackSymbol();
		Variable start = this.getGrammar().getStartVariable();
		TransitionSet<PDATransition> transitions = this.getConvertedAutomaton().getTransitions();
		
		
		PDATransition initial = new PDATransition(getStartState(),
													getMiddleState(), 
													new SymbolString(),
													new SymbolString(bos), 
													new SymbolString(start,bos));
		
		PDATransition toFinal = new PDATransition(getMiddleState(), 
													getFinalState(),
													new SymbolString(), 
													new SymbolString(bos), 
													new SymbolString());
		
		transitions.add(initial);
		transitions.add(toFinal);
		
		
		PDATransition[] loops = createAllReduceLoops();
		
		return transitions.addAll(Arrays.asList(loops));
	}
	

	private PDATransition[] createAllReduceLoops(){
		ArrayList<PDATransition> trans = new ArrayList<PDATransition>();
		State middle = this.getMiddleState();
		for (Symbol s: this.getGrammar().getTerminals()){
			trans.add(new PDATransition(middle,
										middle,
										new SymbolString(s),
										new SymbolString(s),
										new SymbolString()));
		}
		
		return trans.toArray(new PDATransition[0]);
	}
	

}

package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.operators.KleeneStar;
import model.symbols.SymbolString;

public class KleeneStarDeX extends DeExpressionifier {

	private KleeneStar myKleeneStar;

	public KleeneStarDeX(OperatorAlphabet alph) {
		super(alph);
		myKleeneStar = alph.getKleeneStar();
	}

	@Override
	public List<FSATransition> adjustTransitionSet(
			FSATransition trans, GeneralizedTransitionGraph gtg) {
		SymbolString input = new SymbolString(trans.getInput());
		trans.setInput(input.subList(0,input.size()-1));
		List<FSATransition> toAdd = new ArrayList<FSATransition>();
		//do forward lambda transition
		toAdd.add(new FSATransition(trans.getFromState(), 
												trans.getToState()));
		//do backward lambdaTransition
		toAdd.add(new FSATransition(trans.getToState(), 
											trans.getFromState()));
		
		return toAdd;
	}

	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.isEmpty() && first.endsWith(myKleeneStar);
	}



}

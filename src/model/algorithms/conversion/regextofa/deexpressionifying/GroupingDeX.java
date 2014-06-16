package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class GroupingDeX extends DeExpressionifier {


	private CloseGroup myCloseGroup;

	public GroupingDeX(OperatorAlphabet alph) {
		super(alph);
		myCloseGroup = alph.getCloseGroup();
	}

	@Override
	public List<FSATransition> adjustTransitionSet(
			FSATransition trans, GeneralizedTransitionGraph gtg) {
		//May need to grab transition from GTG, not sure if the assumption that
		//trans is already in the gtg will always hold true.
		SymbolString input = new SymbolString(trans.getInput());
		trans.setInput(input.subList(1,input.size()-1));
		return new ArrayList<FSATransition>();
	}

	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.isEmpty() && first.endsWith(myCloseGroup);
	}

}

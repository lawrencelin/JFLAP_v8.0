package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.OperatorAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;
import model.symbols.SymbolString;

public class UnionDeX extends FourStateDeX{
	
	private UnionOperator myUnionOperator;
	
	public UnionDeX(OperatorAlphabet alph) {
		super(alph);
		myUnionOperator = alph.getUnionOperator();

	}



	@Override
	protected int getShiftFromFirstOp() {
		return 1;
	}

	@Override
	protected List<FSATransition> createLambdaTransitions(
			State[] s, FSATransition trans) {
		ArrayList<FSATransition> toAdd = new ArrayList<FSATransition>();

		toAdd.add(new FSATransition(trans.getFromState(), s[0]));
		toAdd.add(new FSATransition(trans.getFromState(), s[2]));
		toAdd.add(new FSATransition(s[1], trans.getToState()));
		toAdd.add(new FSATransition(s[3], trans.getToState()));

		return toAdd;
	}



	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.startsWith(myUnionOperator);
	}

}

package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.OperatorAlphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;
import model.symbols.SymbolString;

public class ConcatDeX extends FourStateDeX{

	private UnionOperator myUnionOperator;
	
	public ConcatDeX(OperatorAlphabet alph) {
		super(alph);
		myUnionOperator = alph.getUnionOperator();
	}

	@Override
	protected int getShiftFromFirstOp() {
		return 0;
	}



	@Override
	protected List<FSATransition> createLambdaTransitions(
			State[] s, FSATransition trans) {
		ArrayList<FSATransition> toAdd = new ArrayList<FSATransition>();

		toAdd.add(new FSATransition(trans.getFromState(), s[0]));
		toAdd.add(new FSATransition(s[1], s[2]));
		toAdd.add(new FSATransition(s[3], trans.getToState()));

		return toAdd;
	}





	@Override
	protected boolean isApplicable(SymbolString first, SymbolString rest) {
		return rest.size() > 0 && !rest.startsWith(myUnionOperator);
	}
}

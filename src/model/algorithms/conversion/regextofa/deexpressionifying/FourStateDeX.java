package model.algorithms.conversion.regextofa.deexpressionifying;

import java.util.ArrayList;
import java.util.List;

import model.algorithms.conversion.regextofa.DeExpressionifier;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.symbols.SymbolString;

public abstract class FourStateDeX extends DeExpressionifier {

	public FourStateDeX(OperatorAlphabet alph) {
		super(alph);
	}

	@Override
	public List<FSATransition> adjustTransitionSet(
			FSATransition trans, GeneralizedTransitionGraph gtg) {
		
		SymbolString input = new SymbolString(trans.getInput());
		SymbolString before = RegularExpression.getFirstOperand(input, getOperatorAlphabet());
		SymbolString after = input.subList(before.size()+getShiftFromFirstOp());

		TransitionSet<FSATransition> transSet = gtg.getTransitions();
		
		transSet.remove(trans);
		
		State s1 = gtg.getStates().createAndAddState();
		State s2 = gtg.getStates().createAndAddState();
		State s3 = gtg.getStates().createAndAddState();
		State s4 = gtg.getStates().createAndAddState();

		transSet.add(new FSATransition(s1, s2, before));
		transSet.add(new FSATransition(s3, s4, after));
		
		return createLambdaTransitions(new State[]{s1,s2,s3,s4},trans);
	}

	protected abstract int getShiftFromFirstOp();

	protected abstract List<FSATransition> createLambdaTransitions(State[] states,
														FSATransition trans);

}

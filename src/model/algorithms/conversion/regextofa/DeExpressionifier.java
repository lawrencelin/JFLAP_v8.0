package model.algorithms.conversion.regextofa;

import java.util.List;

import model.algorithms.AlgorithmException;
import model.automata.TransitionSet;
import model.automata.acceptors.fsa.FSATransition;
import model.regex.GeneralizedTransitionGraph;
import model.regex.OperatorAlphabet;
import model.regex.RegularExpression;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * An interface designed to provide some syntactic sugar for the 
 * implementation of the RE to NFA conversion class. This
 * also makes it fairly easy to implement different definitions
 * of the operators in an RE.
 * 
 * @author Julian Genkins
 *
 */
public abstract class DeExpressionifier {
	
	private OperatorAlphabet myOpAlph;

	public DeExpressionifier(OperatorAlphabet alph){
		myOpAlph = alph;
	}
	
	/**
	 * Checks to see if this DeExpressionifier should be applied
	 * to the input transition, i.e. is the input for the transition
	 * flanked by parenthesis? does it end in a kleene star? etc.
	 * 
	 * @param trans
	 * @return
	 */
	public boolean isApplicable(FSATransition trans){
		SymbolString input = new SymbolString(trans.getInput());
		SymbolString first = RegularExpression.getFirstOperand(input, myOpAlph);
		return isApplicable(first, input.subList(first.size()));
	}
	
	/**
	 * Checks if the deExpressionify is applicable to the input when split
	 * into the first operand and remaining symbols.
	 * 
	 * @param first
	 * @param rest
	 * @return
	 */
	protected abstract boolean isApplicable(SymbolString first, SymbolString rest);

	
	/**
	 * Adjusts the GTG to apply the deExpressionifying of the transition
	 * and returns the transitions necessary to complete the connectivity
	 * of the resulting GTG;
	 * 
	 * @param trans
	 * @param transSet
	 * @return
	 */
	public abstract List<FSATransition> adjustTransitionSet(
											FSATransition trans,
											GeneralizedTransitionGraph gtg);
	
	

	protected OperatorAlphabet getOperatorAlphabet() {
		return myOpAlph;
	}
}

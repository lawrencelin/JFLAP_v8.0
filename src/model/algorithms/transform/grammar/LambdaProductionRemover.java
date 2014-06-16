package model.algorithms.transform.grammar;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.grammar.Grammar;
import model.grammar.GrammarUtil;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Variable;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Algorithm for removing lambda productions from a CFG. Broken
 * down into three primary steps:
 * 
 * 		1. Identify all lambda transitions, i.e. transitions of
 * 			the form: A->lambda
 * 		2. Identify all other productions which derive lambda
 * 				A->X1 X2 ... XN,   XI is a variable, XI derives lambda
 * 		3. 
 * 
 * @author Julian
 *
 */
public class LambdaProductionRemover extends ProductionIdentifyAlgorithm {

	private Set<Variable> myLambdaVariables;

	public LambdaProductionRemover(Grammar g) {
		super(g);
	}

	@Override
	public String getDescriptionName() {
		return "Lambda Production Remover";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	 @Override
	public boolean reset() throws AlgorithmException {
		if (!super.reset())
			return false;
		myLambdaVariables = new TreeSet<Variable>();
		return true;
	}
	 
	@Override
	public boolean isOfTargetForm(Production p) {
		return GrammarUtil.derivesLambda(p, this.getOriginalGrammar());
	}

	@Override
	public Set<Production> getProductionsToAddForRemoval(Production p) {
		Set<Production> toAdd = new TreeSet<Production>();
		Symbol start = this.getOriginalGrammar().getStartVariable();
		Symbol lhs = p.getLHS()[0];
		if (lhs.equals(start))
			return toAdd;
		
		ProductionSet prods = this.getTransformedGrammar().getProductionSet();
		Set<Production> varOnRHS = prods.getProductionsWithSymbolOnRHS(lhs);
		for (Production pRHS : varOnRHS) {

				toAdd.addAll(doAllPossibleSubs(pRHS,lhs));
		}

		return toAdd;
	}



	private Set<Production> doAllPossibleSubs(Production pRHS,
			Symbol target) {
		Set<Production> toAdd = new TreeSet<Production>();
		for (int i: getIndeciesOfTarget(pRHS.getRHS(),target)){
			SymbolString subInto = new SymbolString(pRHS.getRHS());
			subInto.remove(i);
			Production substituted = new Production(
											new SymbolString(pRHS.getLHS()),
											subInto);
			if (!substituted.isLambdaProduction())
				toAdd.add(substituted);
			toAdd.addAll(doAllPossibleSubs(substituted, target));
		}
		return toAdd;
	}
	
	private int[] getIndeciesOfTarget(Symbol[] symbols, Symbol target) {
		int[] index = new int[symbols.length];
		int j = 0;
		for (int i = 0; i < symbols.length; i++){
			if (symbols[i].equals(target)){
				index[j++] = i;
			}
		}
		return Arrays.copyOfRange(index, 0, j);
	}

	@Override
	protected boolean shouldRemove(Production p) {
		return p.isLambdaProduction();
	}
	
	@Override
	public String getIdentifyStepName() {
		return "Identify all Lambda Productions";
	}
	
	@Override
	public BooleanWrapper identifyProductionToBeRemoved(Production p) {
		BooleanWrapper bw = super.identifyProductionToBeRemoved(p) ;
		if (!bw.isError())
			myLambdaVariables.add((Variable) p.getLHS()[0]);
		return bw;
	}

}

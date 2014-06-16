package model.algorithms.transform.grammar;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import oldnewstuff.main.JFLAP;

import debug.JFLAPDebug;

import errors.BooleanWrapper;

import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmExecutingStep;
import model.algorithms.steppable.AlgorithmStep;
import model.formaldef.components.SetComponent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.grammar.typetest.matchers.CNFChecker;
import model.grammar.typetest.matchers.GrammarChecker;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class CNFConverter extends GrammarTransformAlgorithm {

	private CNFChecker myChecker;
	private Map<Terminal, Variable> myTermToVarMap;

	public CNFConverter(Grammar g) {
		super(g);
	}
	
	@Override
	public boolean reset() throws AlgorithmException {
		super.reset();
		myChecker = new CNFChecker();
		myTermToVarMap = new TreeMap<Terminal, Variable>();
		return updateProductionsTo(getOriginalGrammar());
	}
	private boolean updateProductionsTo(Grammar gram) {
		ProductionSet p = getTransformedGrammar().getProductionSet();
		p.clear();
		return p.addAll(gram.getProductionSet());
	}

	@Override
	public String getDescriptionName() {
		return "CNF Converter";
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{
				new LambdaRemovalStep(),
				new UnitRemovalStep(),
				new UselessRemovalStep(),
				new ConvertToCNFStep()};
	}
	
	public Production[] getNonCNFProductions() {
		
		Set<Production> nonCNF = new TreeSet<Production>();
		for (Production p : this.getTransformedGrammar().getProductionSet()){
			if (!isCNF(p))
				nonCNF.add(p);
		}
		return nonCNF.toArray(new Production[0]);
	}

	public void convertAnotherProduction() {
		Production p = getNonCNFProductions()[0];
		doCNFStep(p);
	}

	public BooleanWrapper doCNFStep(Production p) {
		if (isCNF(p))
			return new BooleanWrapper(false, "The production selected is already in " +
					"Chomsky Normal Form (CNF).");

		if (!p.getTerminalsOnRHS().isEmpty())
			doTerminalSubstitution(p);
		else
			doVariableSplit(p);
		return new BooleanWrapper(true);
	}

	private void doVariableSplit(Production p) {
		SymbolString oldRHS = new SymbolString(p.getRHS());
		SymbolString newRHS = oldRHS.subList(0,oldRHS.size()-2);
		
		Variable newVar = getAndAddNextDVar();
		
		newRHS.add(newVar);
		p.setRHS(newRHS);
		
		SymbolString removed = oldRHS.subList(oldRHS.size()-2);
		SymbolString lhs = new SymbolString(newVar);
		
		Production newProd = new Production(lhs, removed);
		
		this.getTransformedGrammar().getProductionSet().add(newProd);
	}

	private Variable getAndAddNextDVar() {
		Grammar g = getTransformedGrammar();
		VariableAlphabet vars = g.getVariables();
		int i = 0;
		Variable D = null;
		do {
			String var = "D" + i++;
			if (g.usingGrouping()){
				var = g.getOpenGroup() + var + g.getCloseGroup();
			}
			D = new Variable(var);
		}while (!vars.add(D));
		return D;
	}

	private void doTerminalSubstitution(Production p) {
		Symbol[] rhs = p.getRHS();
		for (int i = 0; i < rhs.length; i++ ){
			Symbol curr = rhs[i];
			if (Grammar.isTerminal(curr)){
				Variable var = myTermToVarMap.get(curr);
				if (var == null)
					var = createAndAddNextTermVar((Terminal) curr);
				rhs[i] = var;
				Production newProd = new Production(var, curr);
				getTransformedGrammar().getProductionSet().add(newProd);
			}
				
		}
		p.setRHS(new SymbolString(rhs));
	}

	private Variable createAndAddNextTermVar(Terminal curr) {
		Grammar g = getTransformedGrammar();
		VariableAlphabet vars = g.getVariables();
		int i = 0;
		Variable Ba = null;
		do {
			//TODO: BAD HARDCODING
			String var = (char)((int)'B' + i++)+ "(" + curr.toString() +")";
			if (g.usingGrouping()){
				var = g.getOpenGroup() + var + g.getCloseGroup();
			}
			Ba = new Variable(var);
		}while (!vars.add(Ba));
		myTermToVarMap.put(curr, Ba);
		return Ba;
	}

	private boolean isCNF(Production p) {
		return myChecker.matchesProduction(p);
	}

	private abstract class ChomskyStep extends AlgorithmExecutingStep<GrammarTransformAlgorithm>{
		@Override
		public void updateDataInMetaAlgorithm() {
			GrammarTransformAlgorithm alg = getAlgorithm();
			updateProductionsTo(alg.getTransformedGrammar());

		}
	}
	
	private class LambdaRemovalStep extends ChomskyStep{

		@Override
		public LambdaProductionRemover initializeAlgorithm() {
			return new LambdaProductionRemover(getTransformedGrammar());
		}
		
	}
	
	private class UnitRemovalStep extends ChomskyStep{

		@Override
		public GrammarTransformAlgorithm initializeAlgorithm() {
			return new UnitProductionRemover(getTransformedGrammar());
		}
		
	}

	private class UselessRemovalStep extends ChomskyStep{

		@Override
		public GrammarTransformAlgorithm initializeAlgorithm() {
			return new UselessProductionRemover(getTransformedGrammar());
		}
		
	}
	
	private class ConvertToCNFStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Convert Productions to CNF form";
		}

		@Override
		public String getDescription() {
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			convertAnotherProduction();
			return true;
		}

		@Override
		public boolean isComplete() {
			return getNonCNFProductions().length == 0;
		}
		
	}
}

package model.regex;

import java.util.Collection;

import oldnewstuff.main.JFLAP;
import util.UtilFunctions;
import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.grammar.Terminal;
import model.grammar.TerminalAlphabet;
import model.grammar.Variable;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.symbols.Symbol;
import model.symbols.SymbolString;

/**
 * Creates a basic Regular Expression grammar with
 * Productions as follows:
 * 	START --> EXPRESSION
 * 	EXPRESSION 	--> ( EXPRESSION )
 * 	EXPRESSION 	--> EXPRESSION + EXPRESSION
 * 	EXPRESSION  --> EXPRESSION *
 * 	EXPRESSION 	--> EXPRESSION EXPRESSION
 * 	EXPRESSION	--> a 
 * 
 * 		where a is any terminal.
 * 
 * I imagine this could be optimized.
 * 
 * @author Julian
 *
 */
public class RegularExpressionGrammar extends Grammar {

	private final Variable EXPRESSION = new Variable("<Expression>");
	private final Variable START = new Variable("<Start>");
	private InputAlphabet myInputAlph;
	private OperatorAlphabet myOperatorAlph;

	public RegularExpressionGrammar(InputAlphabet alph,
			OperatorAlphabet ops) {
		myInputAlph = alph;
		myInputAlph.addListener(this);
		myOperatorAlph = ops;
		this.setVariableGrouping(new GroupingPair('<', '>'));
		this.setStartVariable(START);
		this.getProductionSet().add(new Production(START, EXPRESSION));
		addProduction(ops.getOpenGroup().copy(), EXPRESSION, ops.getCloseGroup().copy());
		addProduction(EXPRESSION, ops.getUnionOperator().copy(), EXPRESSION);
		addProduction(EXPRESSION, ops.getKleeneStar().copy());
		addProduction(EXPRESSION, EXPRESSION);
		addProduction(ops.getEmptySub().copy());
		inputSymbolsAdded(alph);
	}

	private boolean addProduction(Symbol ... rhs) {

		Production p = new Production(new SymbolString(EXPRESSION),
				new SymbolString(rhs));
		return this.getProductionSet().add(p);
	}

	@Override
	public String getDescriptionName() {
		return "Regular Expression " + super.getDescriptionName();
	}

	/**
	 * Removes the input symbol s from the terminal alphabet
	 * and from the production set. This is to ensure that the 
	 * {@link RegularExpressionGrammar} has alphabets and productions
	 * which mirror that of the {@link RegularExpression} object itself
	 * 
	 * @param symbols
	 * @return
	 */
	public boolean inputSymbolsRemoved(Collection<Symbol> symbols){
		boolean changed = false;
		for (Symbol s: symbols){
			if (removeProductionForSymbol(s) &&
					this.getTerminals().remove(s))
				changed = true;
		}
		return changed;

	}

	/**
	 * Removes the production associated with the given input
	 * symbol s that has been removed from the input alphabet.
	 * This means any production of the form:
	 * 
	 * 		EXPRESSION ---> s
	 * 
	 * @param s
	 * @return
	 */
	private boolean removeProductionForSymbol(Symbol s) {
		ProductionSet productions = this.getProductionSet();
		for (Production p: productions){
			if (UtilFunctions.contains(p.getRHS(),s)){
				return productions.remove(p);
			}
		}
		return false;
	}

	/**
	 * Adds the input symbol s to the terminal alphabet
	 * and add the correct production to the production set. 
	 * is to ensure that the {@link RegularExpressionGrammar} 
	 * has alphabets and productions which mirror that of the 
	 * {@link RegularExpression} object itself.
	 * 
	 * @param collection
	 * @return
	 */
	public boolean inputSymbolsAdded(Collection<Symbol> symbols){
		TerminalAlphabet terms = this.getTerminals();
		boolean changed = false;
		for (Symbol s: symbols){
			if(addProductionForSymbol(new Terminal(s.getString())))
				changed = true;
		}
		return true;

	}

	private boolean addProductionForSymbol(Symbol s) {
		return addProduction(s);
	}

	@Override
	public Grammar alphabetAloneCopy() {

		return new RegularExpressionGrammar(myInputAlph, myOperatorAlph);
	}

	@Override
	public RegularExpressionGrammar copy(){
		RegularExpressionGrammar g = 
				new RegularExpressionGrammar(myInputAlph, myOperatorAlph);
		ProductionSet prods = g.getProductionSet();
		prods.clear();
		prods.addAll(this.getProductionSet().toCopiedSet());
		return g;
	}

	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		if (event.comesFrom(InputAlphabet.class)){
			switch (event.getType()){
			case ITEM_MODIFIED: break;
			//propagate modify to regex grammar if need be.
			case ITEM_ADDED:
				this.inputSymbolsAdded((Collection<Symbol>)event.getArg(0));
				break;
			case ITEM_REMOVED:
				this.inputSymbolsRemoved((Collection<Symbol>)event.getArg(0));
				break;
			}
		}
		super.componentChanged(event);
	}
}

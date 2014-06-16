package model.regex;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import oldnewstuff.view.Updateable;
import universe.preferences.JFLAPPreferences;
import util.UtilFunctions;


import debug.JFLAPDebug;
import errors.BooleanWrapper;

import model.algorithms.testinput.parse.brute.RestrictedBruteParser;
import model.algorithms.transform.grammar.CNFConverter;
import model.automata.InputAlphabet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.formaldef.FormalDefinitionException;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Grammar;
import model.regex.operators.OpenGroup;
import model.regex.operators.Operator;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class RegularExpression extends FormalDefinition {

	private RegularExpressionGrammar myGrammar;
	private OperatorAlphabet myOperatorAlphabet;
	private ExpressionComponent myExpression;

	public RegularExpression(InputAlphabet alph){
		this(alph, new ExpressionComponent());
	}


	public RegularExpression(InputAlphabet alph,
			ExpressionComponent expression) {
		super(alph, expression);
		myExpression = expression;
		myOperatorAlphabet = new OperatorAlphabet();
		myGrammar = new RegularExpressionGrammar(alph, myOperatorAlphabet);
	}
	
	public RegularExpression(){
		this(new InputAlphabet());
	}


	public InputAlphabet getInputAlphabet(){
		return this.getComponentOfClass(InputAlphabet.class);
	}

	@Override
	public String getDescriptionName() {
		return "Regular Expression";
	}

	@Override
	public String getDescription() {
		return "A regular expression.";
	}

	public OperatorAlphabet getOperators() {
		return myOperatorAlphabet;
	}


	@Override
	public RegularExpression alphabetAloneCopy() {
		return new RegularExpression(getInputAlphabet());
	}

	//	public void setTo(String in){
	//		in = in.replaceAll(JFLAPPreferences.getEmptyStringSymbol(),
	//				myOperatorAlphabet.getEmptySub().getString());
	//
	//		if (!SymbolString.canBeParsed(in, myGrammar)){
	//			throw new RegularExpressionException("Invalid input. This string contains symbols" +
	//					" that are neither input symbols or operators");
	//			
	//		}
	//		
	//		SymbolString s = SymbolString.createFromString(in, myGrammar);
	//		
	//		setTo(s);
	//		
	//
	//		
	//	}


	public boolean setTo(SymbolString s) {
		BooleanWrapper format = correctFormat(s);
		if (format.isError()){
			throw new RegularExpressionException(format.getMessage());
		}
		return myExpression.setTo(s);

	}
	
	@Override
	public void componentChanged(AdvancedChangeEvent event) {
		super.componentChanged(event);
//		if(event.getSource() instanceof InputAlphabet){
//			if(event.getType() == ITEM_MODIFIED){
//				Symbol from = (Symbol) event.getArg(0);
//				Symbol to = (Symbol) event.getArg(1);
//				myExpression.applySymbolMod(from.getString(), to.getString());
//			}
//		}
		distributeChange(event);
	}
	
	@Override
	public Alphabet[] getParsingAlphabets() {
		return UtilFunctions.combine(super.getAlphabets(),myOperatorAlphabet);
	}

	//	/**
	//	 * Ideal version of the Correct format method. Uses the JFLAP 
	//	 * Infrastructure to parse the input.
	//	 * 
	//	 * @param exp
	//	 * @return
	//	 */
	//	private BooleanWrapper correctFormat(SymbolString exp){
	//		RestrictedBruteParser parser = new RestrictedBruteParser(myGrammar);
	//		parser.parse(exp);
	//		parser.start();
	//		while(parser.isActive()){
	//		}
	//		boolean isInLanguage = parser.getAnswer() != null;
	//		return new BooleanWrapper(isInLanguage, "The input regex, " + exp + " is invalid. " +
	//				"This means it is not in the language of the JFLAP regular expression grammar.");
	//	}

	/**
	 * A temporary solution to the inefficiency of the Brute parser
	 * and non-optimized expression grammar.
	 * 
	 * @param exp
	 * @return true if the exp is properly formatted
	 */
	public BooleanWrapper correctFormat(SymbolString exp){
		if (exp == null || exp.size() == 0)
			return new BooleanWrapper(false,
					"The expression must be nonempty.");
		if (!isGroupingBalanced(exp))
			return new BooleanWrapper(false,
					"The parentheses are unbalanced!");

		Symbol star = myOperatorAlphabet.getKleeneStar();
		Symbol open = myOperatorAlphabet.getOpenGroup();
		Symbol close = myOperatorAlphabet.getCloseGroup();
		Symbol union = myOperatorAlphabet.getUnionOperator();
		Symbol empty = myOperatorAlphabet.getEmptySub();
		BooleanWrapper poorFormat = new BooleanWrapper(false,
				"Operators are poorly formatted.");
		BooleanWrapper badLambda = new BooleanWrapper(false,
				"Lambda character must not cat with anything else.");
		
		Symbol c = exp.getFirst();
		if (c.equals(star))
			return poorFormat;
		if (c.equals(empty) && exp.size() > 1 && !exp.get(1).equals(union))
			return badLambda;
		
		Symbol p = c;
		for (int i = 1; i < exp.size(); i++) {
			c = exp.get(i);
			
			if (c.equals(union) && i == exp.size()-1){
				return poorFormat;
			}
			else if( c.equals(star) &&
					(p.equals(open)|| p.equals(union))){
				return poorFormat;
			}
			else if(c.equals(empty)){
				if (!(p.equals(open) || p.equals(union)))
					return badLambda;
				if (i == exp.size() - 1)
					continue;
				p = exp.get(i+1);
				if (!(p.equals(close) || p.equals(union) || p.equals(star)))
					return badLambda;
			}
			p=c;
		}
		return new BooleanWrapper(true);
	}

	/**
	 * Checks if the parentheses are balanced in a string.
	 * 
	 * @param string
	 *            the string to check
	 * @return if the parentheses are balanced
	 */
	private boolean isGroupingBalanced(SymbolString exp) {
		int count = 0;
		for (int i = 0; i < exp.size(); i++) {
			if (exp.get(i).equals(myOperatorAlphabet.getOpenGroup()))
				count++;
			else if (exp.get(i).equals(myOperatorAlphabet.getCloseGroup()))
				count--;
			if (count < 0)
				return false;
		}
		return count == 0;
	}


	public String getExpressionString(){
		return myExpression.getExpression().toString();
	}

	public boolean matches(String in){
		Pattern p = convertToPattern();
		return p.matcher(in).matches();
	}


	public Pattern convertToPattern() {

		return RegularExpression.convertToPattern(this);
	}

	public static Pattern convertToPattern(RegularExpression regEx) {

		return Pattern.compile(convertToStringPattern(regEx));
	}


	/**
	 * Will convert a regular expression object into a Java String object
	 * with the correct syntax to be converted into a Pattern object.
	 * @param regEx
	 * @return
	 */
	public static String convertToStringPattern(RegularExpression regEx) {
		OperatorAlphabet ops = regEx.getOperators();
		SymbolString temp = regEx.getExpression();
		temp = temp.replaceAll(ops.getUnionOperator(),new Symbol("|"));
		return temp.toNondelimitedString();

	}


	public SymbolString getExpression() {
		return getComponentOfClass(ExpressionComponent.class).getExpression();
	}

	@Override
	public BooleanWrapper[] isComplete() {

		BooleanWrapper[] comp = super.isComplete();

		if (comp.length == 0){
			BooleanWrapper bw = new BooleanWrapper(derivesSomething(), 
					"This regular Expression does not derive any strings.");
			if (bw.isError())
				comp = new BooleanWrapper[]{bw};
		}


		return comp;
	}


	private boolean derivesSomething() {
		SymbolString exp = getExpression();
		exp.removeAll(getOperators());
		return !exp.isEmpty();
	}

	/**
	 * Retrieves the first operand from the input SymbolString.
	 * 
	 * @param input
	 * @return
	 */
	public static SymbolString getFirstOperand(SymbolString input,
			OperatorAlphabet ops) {
		if (input.isEmpty())
			return input;
		int n = 0;
		int i = 0;
		for (; i < input.size(); i++){
			Symbol s = input.get(i);
			if (ops.getOpenGroup().equals(s))
				n++;
			else if(ops.getCloseGroup().equals(s)) 
				n--;

			if (n == 0)
				break;
		}

		if (i < input.size()-1 && 
				input.get(i+1).equals(ops.getKleeneStar())){
			i++;
		}
		return input.subList(0,i+1);
	}

	@Override
	public RegularExpression copy() {
		return new RegularExpression(getInputAlphabet().copy(), 
									myExpression.copy());
	}


	@Override
	public InputAlphabet getLanguageAlphabet() {
		return getInputAlphabet();
	}

	@Override
	public Symbol createSymbol(String sym) {
		Symbol ret = null;
		if (sym.length() == 1) 
			ret = myOperatorAlphabet.getSymbolForString(sym);
		if (ret == null && noOperators(sym))
			ret = super.createSymbol(sym);
		return ret;
	}


	private boolean noOperators(String sym) {
		for (Symbol s: myOperatorAlphabet){
			if (sym.contains(s.getString()))
				return false;
		}
		return true;
	}


}

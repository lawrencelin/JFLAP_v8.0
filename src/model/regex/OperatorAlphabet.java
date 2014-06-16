package model.regex;

import debug.JFLAPDebug;
import oldnewstuff.main.JFLAP;
import universe.preferences.JFLAPPreferences;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.regex.operators.CloseGroup;
import model.regex.operators.KleeneStar;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;
import model.symbols.Symbol;

public class OperatorAlphabet extends Alphabet {

	private UnionOperator myUnionOperator;
	private OpenGroup myOpenGroup;
	private KleeneStar myKleeneStar;
	private CloseGroup myCloseGroup;
	private EmptySub myEmptySub;

	public OperatorAlphabet(){
		super();
		myUnionOperator = JFLAPPreferences.getUnionOperator();
		myKleeneStar = new KleeneStar();
		myOpenGroup = JFLAPPreferences.getCurrentRegExOpenGroup();
		myCloseGroup = JFLAPPreferences.getCurrentRegExCloseGroup();
		myEmptySub = JFLAPPreferences.getSubForEmptyString();
		this.addAll(myUnionOperator, myKleeneStar, myOpenGroup, myCloseGroup, myEmptySub);
	}
	
	public OpenGroup getOpenGroup(){
		return myOpenGroup;
	}
	
	public CloseGroup getCloseGroup(){
		return myCloseGroup;
	}
	
	public KleeneStar getKleeneStar(){
		return myKleeneStar;
	}
	
	public UnionOperator getUnionOperator(){
		return myUnionOperator;
	}
	
	@Override
	public String getDescriptionName() {
		return "Operators";
	}

	@Override
	public String getDescription() {
		return "The set of all operators possible in a regular expression.";
	}

	@Override
	public String getSymbolName() {
		return "operator";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'O';
	}

	public EmptySub getEmptySub() {
		return myEmptySub;
	}
	
	@Override
	public OperatorAlphabet copy() {
		return (OperatorAlphabet) super.copy();
	}
	
	@Override
	public Symbol getSymbolForString(String cur) {
		for (Symbol s: this){
			if (s.getString().equals(cur) || s.toString().equals(cur))
				return s;
		}
		return null;
	}

}

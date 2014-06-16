package model.lsystem.formaldef;

import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.TerminalAlphabet;
import model.grammar.VariableAlphabet;
import model.lsystem.CommandAlphabet;
import errors.BooleanWrapper;

public class FormalAlph extends FormalDefinitionComponent {
	private Alphabet sigma;
	
	public FormalAlph(VariableAlphabet var, TerminalAlphabet term){
		sigma = new CommandAlphabet();
		sigma.addAll(term);
		sigma.addAll(var);
	}
	
	public FormalAlph(Alphabet alph){
		sigma = new CommandAlphabet();
		sigma.addAll(alph);
	}
	
	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return "LSystem Alphabet";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "LSystem Alphabet";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return '\u03A3';
	}

	@Override
	public BooleanWrapper isComplete() {
		// TODO Auto-generated method stub
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent copy() {
		// TODO Auto-generated method stub
		return new FormalAlph(sigma);
	}

	@Override
	public void clear() {
		sigma = new CommandAlphabet();
	}

}

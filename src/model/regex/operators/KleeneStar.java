package model.regex.operators;

import model.symbols.Symbol;

public class KleeneStar extends Operator {

	public KleeneStar() {
		super("*");
	}

	@Override
	public String getDescriptionName() {
		return "Kleene Star";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public KleeneStar copy() {
		return new KleeneStar();
	}

}

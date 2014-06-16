package model.lsystem.formaldef;

import errors.BooleanWrapper;
import model.formaldef.components.FormalDefinitionComponent;
import model.grammar.ProductionSet;

public class FormalRewritingRules extends FormalDefinitionComponent {

	private ProductionSet myProductions;

	public FormalRewritingRules(ProductionSet p){
		myProductions = p;
	}
	
	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return "Rewriting Rules";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "LSystem Rewriting Rules";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return 'R';
	}

	@Override
	public BooleanWrapper isComplete() {
		// TODO Auto-generated method stub
		return new BooleanWrapper(true);
	}

	@Override
	public FormalDefinitionComponent copy() {
		// TODO Auto-generated method stub
		return new FormalRewritingRules(myProductions);
	}

	@Override
	public void clear() {
		myProductions.clear();
	}

}

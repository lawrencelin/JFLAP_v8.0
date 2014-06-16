package model.lsystem.formaldef;

import view.lsystem.helperclasses.Axiom;
import model.formaldef.components.FormalDefinitionComponent;
import errors.BooleanWrapper;

public class FormalAxiom extends FormalDefinitionComponent {
	private Axiom axiom;	
	
	public FormalAxiom(Axiom axiom){
		this.axiom = axiom;
	}
	
	@Override
	public String getDescriptionName() {
		// TODO Auto-generated method stub
		return "Axiom";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return "Axiom";
	}

	@Override
	public Character getCharacterAbbr() {
		// TODO Auto-generated method stub
		return 'A';
	}

	@Override
	public BooleanWrapper isComplete() {
		// TODO Auto-generated method stub
		return new BooleanWrapper(axiom.size() > 0);
	}

	@Override
	public FormalDefinitionComponent copy() {
		// TODO Auto-generated method stub
		return new FormalAxiom(axiom);
	}

	@Override
	public void clear() {
		axiom.clear();
	}

}

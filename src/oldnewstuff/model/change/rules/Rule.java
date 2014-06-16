package oldnewstuff.model.change.rules;

import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.ChangeRelated;
import errors.BooleanWrapper;
import model.formaldef.Describable;

public abstract class Rule extends ChangeRelated implements Describable{

	public Rule(int type) {
		super(type);
	}

	public abstract BooleanWrapper checkRule(ChangeEvent event);
	
	public String toString(){
		return this.getDescriptionName() + ": " + this.getDescription();
	}
	
}

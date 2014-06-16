package oldnewstuff.model.change.rules;

import java.lang.Character.Subset;

import oldnewstuff.model.change.ChangeEvent;

import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import errors.BooleanWrapper;

public abstract class SetComponentRule<T extends SetSubComponent<T>> extends Rule {

	public SetComponentRule(int type) {
		super(type);
	}


	@Override
	public BooleanWrapper checkRule(ChangeEvent event) {
		if (event.isOfType(ITEM_MODIFY)){
			return checkModify((SetComponentModifyEvent<T>) event);
		}
		else if (event.isOfType(ITEM_ADD)){
			return checkAdd((SetComponentEvent<T>) event);
		}
		else if (event.isOfType(ITEM_REMOVE)){
			return checkRemove((SetComponentEvent<T>) event);
		}
		return null;
	}


	public abstract BooleanWrapper checkRemove(SetComponentEvent<T> event);

	public abstract BooleanWrapper checkAdd(SetComponentEvent<T> event);

	public abstract BooleanWrapper checkModify(SetComponentModifyEvent<T> event);

}

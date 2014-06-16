package oldnewstuff.model.change.rules.applied;

import java.util.Collection;

import oldnewstuff.model.change.rules.IdenticalItemRule;

import errors.BooleanWrapper;
import model.change.events.SetComponentEvent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;

public class SelfIdenticalRule<T extends SetSubComponent<T>> extends IdenticalItemRule<T> {


	public SelfIdenticalRule(int type, SetComponent<T> base) {
		super(type, base, base);
	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<T> event) {
		Collection<T> items = event.getItems();
		ComparisonWrapper wrap = checkSimilar(items);

		if (wrap != null) return new BooleanWrapper(true);
		return new BooleanWrapper(false, "The " + wrap.item.getDescriptionName() + " " +
				wrap.item.toString() + " must already be in this set to be removed.");
	}
	
	@Override
	public String getDescriptionName() {
		return "Self-Identical Symbol Rule";
	}

	@Override
	public String getDescription() {
		return "Any single alphabet may not contain 2 identical symbols.";
	}

}

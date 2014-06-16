package oldnewstuff.model.change.rules;

import java.util.Collection;


import model.change.events.SetComponentEvent;
import model.change.events.SetComponentModifyEvent;
import model.formaldef.components.SetComponent;
import model.formaldef.components.SetSubComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import errors.BooleanWrapper;

public abstract class IdenticalItemRule<T extends SetSubComponent<T>> extends SetComponentRule<T> {

	private SetComponent<T>[] mySets;
	private SetComponent<T> myBase;

	public IdenticalItemRule(int type, SetComponent<T> base, SetComponent<T> ... alphabets) {
		super(type);
		myBase = base;
		mySets = alphabets;
	}

	@Override
	public BooleanWrapper checkModify(SetComponentModifyEvent<T> event) {
		return checkAdd(event);
	}

	//	@Override
	//	public BooleanWrapper checkRemove(SetComponentEvent<T> event) {
	//		T item = event.getItem();
	//		return new BooleanWrapper(!myBase.contains(item), "You may not remove the " + item.getDescriptionName()
	//				+ " from the " + myBase.getDescriptionName() + " because it is a not part of the set.");
	//	}

	@Override
	public BooleanWrapper checkRemove(SetComponentEvent<T> event) {
		return new BooleanWrapper(true);
	}

	@Override
	public BooleanWrapper checkAdd(SetComponentEvent<T> event) {
		Collection<T> items = event.getItems();
		ComparisonWrapper wrap = checkSimilar(items);

		if (wrap == null) return new BooleanWrapper(true);

		return new BooleanWrapper(false, 
				"The " + wrap.item.getDescription() + " " + wrap.item.toString() + 
				" is to similar to the  " + wrap.other.getDescription() + " " + wrap.item.toString() +
				" in the " + wrap.set.getDescriptionName() + ".");
	}

	public ComparisonWrapper checkSimilar(Collection<T> items) {
		for (T item: items){
			for (SetComponent<T> set: mySets){
				for (T other: set){
					if (areTooSimilar(item, other))
						return new ComparisonWrapper(set, item, other);
				}
			}
		}
		return null;
	}

	private boolean areTooSimilar(T item, T other) {
		return item.equals(other);
	}

	protected class ComparisonWrapper {
		public T other;
		public T item;
		public SetComponent<T> set;
		public ComparisonWrapper(SetComponent<T> set, T item, T other) {
			this.set = set;
			this.item = item;
			this.other = other;
		}

	}

}

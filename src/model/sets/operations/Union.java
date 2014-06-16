package model.sets.operations;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.InfiniteSet;
import model.sets.elements.Element;

public class Union extends SetOperation {


	@Override
	protected FiniteSet getFiniteAnswer() {
		Set<Element> elements = new TreeSet<Element>();
		for (AbstractSet set : myOperands) {
			for (Element e : set.getSet()) {
				elements.add(e);
			}
		}
		return new FiniteSet(getDescription(), elements);
	}

	@Override
	protected InfiniteSet getInfiniteAnswer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Union";
	}

	@Override
	public String getDescription() {
		return "The union of " + myOperands.get(0).getName() + " and " 
				+ myOperands.get(1).getName();
	}

}

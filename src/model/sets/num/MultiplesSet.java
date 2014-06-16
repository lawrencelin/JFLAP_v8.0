package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.elements.Element;

public class MultiplesSet extends PredefinedNumberSet {
	
	private int myFactor;
	private int myIndex;
	
	public MultiplesSet (int factor) {
		super();
		
		myFactor = factor;
		myIndex = 0;
		generateMore(DEFAULT_NUMBER_TO_GENERATE);
	}

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNthElement(int n) {
		return new Element(n * myFactor);
	}

	@Override
	protected Element getNext() {
		int next = myIndex * myFactor;
		myIndex++;
		return new Element(next);
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return "Multiples of " + myFactor;
	}

	@Override
	public String getDescription() {
		return myDescription;
	}

	@Override
	public boolean contains(Element e) {
		try {
			return Integer.parseInt(e.getValue()) % myFactor == 0;
		} catch (NumberFormatException arg0) {
			return false;
		}
	}

	@Override
	public Object copy() {
		return new MultiplesSet(myFactor);
	}

	
}

package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.elements.Element;

public class EvensSet extends PredefinedNumberSet {
	
	private Set<Element> myElements;
	private int myIndex;
	
	public EvensSet () {
		myElements = new TreeSet<Element>();
		myIndex = 0;
		generateMore(DEFAULT_NUMBER_TO_GENERATE);
	}
	
	

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		Set<Element> values = new TreeSet<Element>();
		
		min = min % 2 == 0 ? min : min + 1;
		for (int i = min/2; i <= max/2; i++) {
			values.add(new Element(i * 2));
		}

		String name = "Even numbers between " + min + " and " + max;
		return new FiniteSet(name, values);
	}

	@Override
	public Element getNthElement(int n) {
		return new Element(2 * n);
	}

	@Override
	public void generateMore(int numToGenerate) {
		if (!shouldContinue)
			return;
		
		for (int i = 0; i < numToGenerate; i++) {	
			myElements.add(new Element(myIndex * 2));
			myIndex++;
		}
		
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return "Even numbers";
	}

	@Override
	public String getDescription() {
		return "The set of non-negative even integers";
	}

	@Override
	public boolean contains(Element e) {
		return ((Integer.parseInt(e.getValue()))%2 == 0);
	}



	@Override
	public Object copy() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	protected Element getNext() {
		// TODO Auto-generated method stub
		return null;
	}

}

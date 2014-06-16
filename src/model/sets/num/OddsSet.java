package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.elements.Element;

public class OddsSet extends PredefinedNumberSet {
	
	private Set<Element> myElements;
	private int myIndex;
	
	public OddsSet() {
		myElements = new TreeSet<Element>();
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
		return new Element(2*n - 1);
	}


	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return "Odd Numbers";
	}

	@Override
	public String getDescription() {
		return "Positive odd integers";
	}

	@Override
	public boolean contains(Element e) {
		try {
			return Integer.parseInt(e.getValue()) % 2 == 1;
		} catch (NumberFormatException arg0) {
			return false;
		}
	}


	@Override
	public Object copy() {
		return new OddsSet();
	}


	@Override
	protected Element getNext() {
		int next = myIndex * 2 + 1;
		myIndex++;
		return new Element(next);
	}

}

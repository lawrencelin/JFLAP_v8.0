package model.sets;

import java.util.Set;
import java.util.TreeSet;

import model.sets.elements.Element;



public abstract class InfiniteSet extends AbstractSet {

	protected static final int DEFAULT_NUMBER_TO_GENERATE = 100;
	
	/**
	 * Indicates whether more numbers can be generated
	 * Set to false once integer overflow occurs
	 */
	protected boolean shouldContinue = true;
	
	protected String myName;
	protected String myDescription;
	protected Set<Element> myElements;
	
	public InfiniteSet() {
		this("", "", new TreeSet<Element>());
	}
	
	public InfiniteSet(String name, String description, Set<Element> elements) {
		myName = name;
		myDescription = description;
		myElements = elements;
	}
	
	
	@Override
	public final boolean isFinite () {
		return false;
	}
	
	public void generateMore(int numToGenerate) {
		if (!shouldContinue)
			return;
		for (int i = 0; i < numToGenerate; i++) {
			Element next = getNext();
			if (overflow(next.getValue())) {
				shouldContinue = false;
				break;
			}
			myElements.add(next);
		}
		
	}
	
	
	protected abstract Element getNext();
	
	
	public boolean overflow (String s) {
		try {
			int n = Integer.parseInt(s);
			return Integer.toBinaryString(n).length() > 31;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	
	public String getOverflowMessage () {
		return "Infinite set: unable to show more elements due to integer overflow";
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return myName;
	}

	@Override
	public String getDescription() {
		return myDescription;
	}


	@Override
	public String getSetAsString() {
		return super.getSetAsString() + " ...";
	}

	@Override
	public void setName(String name) {
		myName = name;
	}

	@Override
	public void setDescription(String description) {
		myDescription = description;
	}
	
}

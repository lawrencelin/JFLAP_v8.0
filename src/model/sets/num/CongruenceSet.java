package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.elements.Element;

public class CongruenceSet extends PredefinedNumberSet {
	
	private int myOriginalStart;
	
	private int myWrappedStart;
	private int myModulus;
	private int myIndex;
	
	/**
	 * Constructor for all numbers i such that i mod modulus = startValue mod modulus
	 * 
	 * @param modulus
	 *            the modulus of the congruence relation
	 * @param startValue
	 *            first value in the set
	 */
	public CongruenceSet (int start, int modulus) {
		super();
		myOriginalStart = start;
		
		myIndex = 0;
		myModulus = modulus;
		myWrappedStart = wrap(start);
		
		generateMore(DEFAULT_NUMBER_TO_GENERATE);
	}
	
	private int wrap(int num) {
		while (num >= myModulus) {
			num -= myModulus;
		}
		return num;
	}


	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Element getNthElement(int n) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Element> getSet() {
		return myElements;
	}

	@Override
	public String getName() {
		return "Congruence Set of " + myOriginalStart + " mod " + myModulus;
	}

	@Override
	public String getDescription() {
		return myDescription;
	}

	@Override
	public boolean contains(Element e) {
		try {
			int n = Integer.parseInt(e.getValue());
			return (myOriginalStart - n) % myModulus == 0;
		} catch (NumberFormatException arg0) {
			return false;
		}
	}

	@Override
	public Object copy() {
		return new CongruenceSet(myOriginalStart, myModulus);
	}

	@Override
	protected Element getNext() {
		int next = myModulus * myIndex + myWrappedStart;
		myIndex++;
		return new Element(next);
	}
	


}

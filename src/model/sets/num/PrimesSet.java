package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.elements.Element;

public class PrimesSet extends PredefinedNumberSet {
	
	private Set<Element> myElements;
	/**
	 * The last number that was tested (and possibly added)
	 */
	private int myCurrentEndpoint;
	
	public PrimesSet() {
		super();
		myElements = new TreeSet<Element>();
		myCurrentEndpoint = 2;
		
		generateMore(DEFAULT_NUMBER_TO_GENERATE);
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
		return "Prime Numbers";
	}

	@Override
	public String getDescription() {
		return "Set of prime numbers";
	}

	@Override
	public boolean contains(Element e) {
		try {
			return isPrime(Integer.parseInt(e.getValue()));
		} catch (NumberFormatException arg0) {
			return false;
		}
	}
	
	
	private boolean isPrime (int n) {
		for (int i = 2; i <= Math.sqrt(n); i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	@Override
	protected Element getNext() {
		while (!isPrime(myCurrentEndpoint)){
			myCurrentEndpoint++;
		}
		int prime = myCurrentEndpoint;
		myCurrentEndpoint++;
		return new Element(prime);
	}


	@Override
	public AbstractSet copy() {
		return new PrimesSet();
	}

}

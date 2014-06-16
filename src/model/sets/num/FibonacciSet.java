package model.sets.num;

import java.util.Set;
import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.elements.Element;


public class FibonacciSet extends PredefinedNumberSet {

	/**
	 * Equivalent to f(n)
	 */
	private int current;

	/**
	 * Equivalent to f(n-1)
	 */
	private int previous;

	/**
	 * Equivalent to f(n-2)
	 */
	private int last;

	public FibonacciSet () {
		super();
		last = 0;
		previous = 1;
		
		generateMore(DEFAULT_NUMBER_TO_GENERATE);
	}


	@Override
	public Set<Element> getSet() {
		return myElements;
	}


	@Override
	public String getName() {
		return "Fibonacci";
	}

	@Override
	public String getDescription() {
		return "The set of numbers in the Fibonacci sequence from 0, 1, 1...";
	}

	@Override
	public boolean contains (Element e) {
		try {
			return contains(Integer.parseInt(e.getValue()));
		} catch (NumberFormatException arg0) {
			return false;
		}
	}


	public boolean contains(int n) {
		int a = (int) (5 * Math.pow(n, 2) + 4);
		int b = (int) (5 * Math.pow(n, 2) - 4);
		return (Math.sqrt(a) == (int) (Math.sqrt(a)) || 
				Math.sqrt(b) == (int) (Math.sqrt(b)));
	}

	@Override
	public AbstractSet getNumbersInRange(int min, int max) {
		if (min < 0)	
			min = 0;
		if (max > Integer.MAX_VALUE) 
			max = Integer.MAX_VALUE;

		Set<Element> range = new TreeSet<Element>();
		for (Element e : myElements) {
			range.add(e);
		}

		return new FiniteSet("Fibonacci between " + min + " and " + max, range);

	}

	@Override
	public Element getNthElement(int n) {
		return new Element(fibonacci(n));
	}


	public int fibonacci (int n) {
		if (n <= 1) 
			return n;
		return fibonacci(n-2) + fibonacci(n-1);
	}

	@Override
	public Object copy() {
		return new FibonacciSet();
	}

	@Override
	protected Element getNext() {
		if (myElements.size() == 0) 
			return new Element(last);
		else if (myElements.size() == 1) 
			return new Element(previous);

		current = previous + last;
		previous = current;
		last = previous;
		return new Element(current);
	}


}

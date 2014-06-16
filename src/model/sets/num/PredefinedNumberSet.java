package model.sets.num;

import java.util.TreeSet;

import model.sets.AbstractSet;
import model.sets.InfiniteSet;
import model.sets.elements.Element;

/*
 * Use this class to create specific concrete infinite sets 
 * (e.g. not 'general' or abstract sets like Finite or Finite,
 * but not Customizable ones that require user input to build;
 * currently subclasses include PrimesSet, FibonacciSet, etc.)
 */

public abstract class PredefinedNumberSet extends InfiniteSet {
	
	public PredefinedNumberSet() {
		super();
		setName(getName());
		setDescription(getDescription());
	}

	public PredefinedNumberSet(String name, String description,
			TreeSet<Element> elements) {
		super(name, description, elements);
	}

	public abstract AbstractSet getNumbersInRange(int min, int max);

	public abstract Element getNthElement(int n);

}

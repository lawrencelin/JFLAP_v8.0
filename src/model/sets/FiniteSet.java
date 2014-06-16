package model.sets;

import java.util.ArrayList;
import java.util.Set;

import model.sets.elements.Element;

public class FiniteSet extends AbstractSet {

	private String myName;
	private String myDescription;
	private Set<Element> myElements;

	/**
	 * Creates a new finite set with the specified name, description, and elements
	 * @param name
	 * @param description
	 * @param elements
	 */
	public FiniteSet(String name, String description, Set<Element> elements) {
		myName = name;
		myDescription = description;
		myElements = elements;
	}

	/**
	 * Creates a new finite set with the specified name and elements
	 * 
	 * If no description is provided, the description field will be initialized
	 * to an empty string by default
	 * @param name
	 * @param elements
	 */
	public FiniteSet (String name, Set<Element> elements) {
		this(name, "", elements);
	}

	
	public int getCardinality() {
		return myElements.size();
	}

	
	public Set<Element> getSet() {
		return myElements;
	}

	
	public boolean contains(Element e) {
		return myElements.contains(e);
	}

	
	public String getName() {
		return myName;
	}

	
	public String getDescription() {
		return myDescription;
	}


	@Override
	public FiniteSet copy() {
		return new FiniteSet(this.getName(), 
				this.getDescription(),
				this.getSet());
	}

	@Override
	public final boolean isFinite() {
		return true;
	}

	@Override
	public void setName(String name) {
		myName = name;
	}

	@Override
	public void setDescription(String description) {
		myDescription = description;
	}
	
	public void setElements(Set<Element> elements) {
		myElements = elements;
	}
}

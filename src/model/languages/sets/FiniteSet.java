package model.languages.sets;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.languages.SetComparator;
import model.symbols.SymbolString;

/**
 * 
 * @author Peggy Li
 */

public class FiniteSet implements AbstractJflapSet {
	
	private String myDescription;
	private Set<SymbolString> myElements;
	
	public FiniteSet () {
		myElements = new TreeSet<SymbolString>(new SetComparator());
	}
	
	
	public void add (SymbolString element) {
		myElements.add(element);
	}
	
	public void allAll (SymbolString... elements) {
		for (SymbolString e : elements) {
			this.add(e);
		}
	}
	
	public Collection<SymbolString> getElements () {
		return myElements;
	}
	

	/**
	 * Returns the cardinality of the set
	 * 
	 * @return number of elements in the set
	 */
	public int getCardinality() {
		return myElements.size();
	}

	
	@Override
	public void setDescription(String description) {
		myDescription = description;
	}

	@Override
	public String getDescription() {
		if (myDescription == null) 
			return "No description available.";
		return myDescription;
	} 
	
}

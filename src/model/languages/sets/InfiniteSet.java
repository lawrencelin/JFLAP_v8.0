package model.languages.sets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import model.languages.SetComparator;
import model.symbols.SymbolString;

/**
 * 
 * @author Peggy Li
 *
 */

public class InfiniteSet implements AbstractJflapSet {

	private Set<SymbolString> myElements;
	private String myDescription;
	
	
	public InfiniteSet () {
		myElements = new TreeSet<SymbolString>(new SetComparator());
	}
	
	
	public Collection<SymbolString> getElements (int numberOfElements) {
		ArrayList<SymbolString> elements = new ArrayList<SymbolString>();
		for (int i = 0; i < numberOfElements; i++) {
			
		}
		return elements;
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


	@Override
	public void add(SymbolString stringToAdd) {
		myElements.add(stringToAdd);
		
	}


	@Override
	public void allAll(SymbolString... stringsToAdd) {
		for (SymbolString s : stringsToAdd) {
			this.add(s);
		}
		
	}
	
	

}

package model.languages.sets;

import model.symbols.SymbolString;

/**
 * Provides a generic interface for set classes in JFLAP to implement
 * 
 * Note regarding naming: Set, AbstractSet, etc. are names of existing
 * Java classes. The inclusion of "Jflap" in the class name serves to
 * distinguish this as a uniquely JFLAP class.
 * 
 * @author Peggy Li
 *
 */

public interface AbstractJflapSet {
	
	
	public void add(SymbolString stringToAdd);
	
	public void allAll(SymbolString... stringsToAdd);
	
	/**
	 * Allows a user to provide a brief description of the set
	 * 
	 * example: 	"Set of even numbers less than 40
	 * 				"Strings from alphabet {a, b} where n(a) > 2*n(b)"
	 */
	public void setDescription(String description);
	
	
	/**
	 * Returns a brief description of the elements in the set, if one exists
	 * 
	 * @return description of the set
	 */
	public String getDescription();
	

	
	

}

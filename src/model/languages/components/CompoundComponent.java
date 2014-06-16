package model.languages.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import model.symbols.Symbol;

public class CompoundComponent {
	
	/**
	 * Maps all symbols to the components that use them
	 * 
	 * For example, for a^n b^n, the map would look like:
	 * 			{	a -> [exponentComponentA],
	 * 				n -> [exponentComponentA, exponentComponentB],
	 * 				b -> [exponentComponentB]
	 * 			}
	 */
	private static Map<Symbol, Set<AbstractComponent>> myUsedSymbols;
	
	/**
	 * Maps each exponent symbol to the exponent components using it
	 * 
	 * For example, for a^n b^n, the map would look like:
	 * 			{ 	n -> [exponentComponentA, exponentComponentB]	}
	 */
	private static Map<Symbol, Set<ExponentComponent>> myExponentMap;
	
	/**
	 * Tracks the components in the order they were added
	 * 
	 * For example, for a^n b^n, the set would look like:
	 * 			{ exponentComponent(a, n), exponentComponent(b, n) 	}
	 */
	private LinkedHashSet<AbstractComponent> myFormula;
	
	public CompoundComponent () {
		myUsedSymbols = new HashMap<Symbol, Set<AbstractComponent>>();
		myExponentMap = new HashMap<Symbol, Set<ExponentComponent>>();
		
		myFormula = new LinkedHashSet<AbstractComponent>();
	}
	
	public void addComponent (AbstractComponent comp) {
		myFormula.add(comp);
		
		for (Symbol used : comp.getSymbolsUsed()) {
			if (!myUsedSymbols.containsKey(used)) 
				myUsedSymbols.put(used, new HashSet<AbstractComponent>());
			myUsedSymbols.get(used).add(comp);
		}
		
		if (comp instanceof ExponentComponent) {
			myExponentMap.put(null, null);
		}
	}
	
	public void removeComponent (AbstractComponent comp) throws Exception {
		myFormula.remove(comp);
		
		for (Symbol s : comp.getSymbolsUsed()) {
			// cannot find s as key, or comp as a value associated with s
			if (!myUsedSymbols.containsKey(s) || myUsedSymbols.get(s) == null)
				throw new Exception("Cannot find symbol");
			
			// remove comp from set associated with s, and remove s if now empty
			myUsedSymbols.get(s).remove(comp);
			if (myUsedSymbols.get(s).size() == 0)	
				myUsedSymbols.remove(s);
		}
	}
	
	
	public String toString () {
		StringBuilder build = new StringBuilder();
		for (AbstractComponent comp : myFormula) {
			build.append(comp.toString());
		}
		return build.toString();
	}
	
	
	
	public static void main (String[] args) throws Exception {
		
		CompoundComponent all = new CompoundComponent();
		
		ExponentComponent e1 = new ExponentComponent(new Symbol("a"), new Symbol("n"));
		all.addComponent(e1);
		
		ExponentComponent e2 = new ExponentComponent(new Symbol("b"), new Symbol("n"));
		all.addComponent(e2);
		
		System.out.println(all);
		
		all.removeComponent(e1);
		
		System.out.println(all);
		
		//System.out.println(e1.expand(5));
	}

	
	
}

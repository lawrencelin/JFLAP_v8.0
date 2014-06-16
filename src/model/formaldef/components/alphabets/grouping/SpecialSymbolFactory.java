package model.formaldef.components.alphabets.grouping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import model.automata.acceptors.pda.BottomOfStackSymbol;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.acceptors.pda.StackAlphabet;
import model.symbols.Symbol;



public class SpecialSymbolFactory {
	
	private static final String DELIMITER = "\\.";
	//Sorted maps to preserve priority/ordering in properties file
	private static TreeMap<Integer, GroupingPair> GROUPINGS;
	private static TreeMap<Integer, Symbol> BOTTOM_OF_STACK;
	static {
		GROUPINGS = new TreeMap<Integer, GroupingPair>();
		BOTTOM_OF_STACK = new TreeMap<Integer, Symbol>();
		ResourceBundle rb = ResourceBundle.getBundle("model.formaldef.components.alphabets.grouping.special");
		for (String key: rb.keySet()){
			String groups = rb.getString(key);
			if (key.startsWith("grouping"))
				GROUPINGS.put(Integer.parseInt(key.split(DELIMITER)[1]), new GroupingPair(groups.charAt(0), groups.charAt(1)));
			else if(key.startsWith("bottomstack"))
				BOTTOM_OF_STACK.put(Integer.parseInt(key.split(DELIMITER)[1]), new Symbol(groups));
		}
	}
	
	public static GroupingPair getBestGrouping(Collection<Character> invalid){
		for (Integer key: GROUPINGS.keySet()){
			GroupingPair gp = GROUPINGS.get(key);
			if (!invalid.contains(gp.getOpenGroup()) && !invalid.contains(gp.getCloseGroup())){
				return gp;
			}
		}
		
		return null;
	}
	
	public static Symbol getReccomendedBOSSymbol(StackAlphabet stackAlphabet){
		Set<Character> invalid = stackAlphabet.getUniqueCharacters();
		for (Integer key: BOTTOM_OF_STACK.keySet()){
			Symbol  t = BOTTOM_OF_STACK.get(key);
			if (!invalid.contains(t.toString().charAt(0))){
				return new Symbol(t.getString());
			}
		}
		
		return null;
	}

	public static Collection<GroupingPair> getAllGroupingOptions() {
		return GROUPINGS.values();
	}

	public static GroupingPair getBestGrouping(Set<Symbol> symbolsUsed) {
		Set<Character> chars = new HashSet<Character>();
		for (Symbol s: symbolsUsed){
			for (char c: s.toString().toCharArray()){
				chars.add(c);
			}
			
		}
		return getBestGrouping(chars);
	}
	
}

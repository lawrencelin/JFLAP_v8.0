package model.languages.sets;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ComparatorFactory {
	
	private static Map<String, Class> myOptions;
	
	static {
		myOptions = new HashMap<String, Class>();
		
		myOptions.put("Sort alphabetically", AlphabeticalComparator.class);
		myOptions.put("Sort by length", LengthComparator.class);
		myOptions.put("Sort by order added", OrderAddedComparator.class);
	}
	
	
	public static Comparator getComparator() {
		return null;
	}
	
	static class AlphabeticalComparator {
		
	}
	
	class LengthComparator {
		
	}
	
	class OrderAddedComparator {
		
	}

}

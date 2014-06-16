package model.languages;

import java.util.*;

/**
 * A collection of useful operations that can be done on generic sets (though for some, the items in the sets
 * must extend Comparable.)
 * @author Ian McMahon
 *
 */
public class SetOperators {



	/**
	 * Returns a sorted set of sets consisting of all possible subsets of the original set.
	 */
	public static <T extends Comparable<T>> Set<Set<T>> powerSet (Set<T> originalSet) {
		SortedSet<Set<T>> sets = new TreeSet<Set<T>>(new Comparator<Set<T>>(){
			@Override
			public int compare(Set<T> o1, Set<T> o2) {
				if(o1.size()==o2.size()){
					List<T> list1 = new ArrayList<T>(o1);
					List<T> list2 = new ArrayList<T>(o2);
					for(int i=0;i<list1.size();i++){
						if(!list1.get(i).equals(list2.get(i)))
							return list1.get(i).compareTo(list2.get(i));
					}
				}
				return o1.size()-o2.size();
			}
			
		});
	    if (originalSet.isEmpty()) {
	        sets.add(new HashSet<T>());
	        return sets;
	    }
	    List<T> list = new ArrayList<T>(originalSet);
	    T head = list.get(0);
	    Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
	    for (Set<T> set : powerSet(rest)) {
	        Set<T> newSet = new HashSet<T>();
	        newSet.add(head);
	        newSet.addAll(set);
	        sets.add(newSet);
	        sets.add(set);
	    }
		return sets;
	}
	
	/**
	 * Returns a new set with all unique items from set1 or set2 (set1 | set2)
	 */
	public static<T extends Object> Set<T> union(Set<T> set1, Set<T> set2){
		Set<T> combinedSet = new TreeSet<T>();
		combinedSet.addAll(set1);
		combinedSet.addAll(set2);
		return combinedSet;
	}
	
	/**
	 * Returns a new set of all unique items in BOTH set1 and set 2 (set1 & set2)
	 */
	public static<T extends Object> Set<T> intersection(Set<T> set1, Set<T> set2){
		Set<T> combinedSet = new TreeSet<T>();
		for(T object : set1){
			if(set2.contains(object)){
				combinedSet.add(object);
			}
		}
		return combinedSet;
	}
	
	/**
	 * Returns a new set of all unique items in set1 that are not present in set2 (set1 - set2)
	 */
	public static<T extends Object> Set<T> difference(Set<T> set1, Set<T> set2){
		Set<T> combinedSet = new TreeSet<T>();
		for(T object : set1){
			if(!set2.contains(object)){
				combinedSet.add(object);
			}
		}
		return combinedSet;
	}
	
	/**
	 * Returns the set of all elements NOT in S, based off the specified <i>Universal Set</i> U (
	 */
	public static<T extends Object> Set<T> complementation(Set<T> U, Set<T> S){
		return difference(U,S);
	}
	
	/**
	 * Returns the sorted set consisting of ordered pairs of elements
	 */
	public static<T extends Comparable<T>, S extends Comparable<S>> Set<Tuple<T,S>> cartesianProduct(Set<T> set1, Set<S> set2){
		Set<Tuple<T,S>> newSet = new TreeSet<Tuple<T,S>>();
		for(T t : set1){
			for(S s : set2){
				Tuple<T,S> tuple = new Tuple<T,S>(t,s);
				newSet.add(tuple);
			}
		}
		return newSet;
	}
	
	/**
	 * Returns true if <i>subset</i> is truly a subset of <i>superset</i>
	 */
	public static <T extends Object> boolean isSubset(Set<T> subset, Set<T> superset){
		return superset.containsAll(subset);
	}
	
	/**
	 * Returns true if <i>subset</i> is a proper subset of <i>superset</i>
	 */
	public static <T extends Object> boolean isProperSubset(Set<T> subset, Set<T> superset) {
		return subset.size() < superset.size() && isSubset(subset, superset);
	}
	
	/**
	 * Returns true if set1 and set2 have no common elements
	 */
	public static <T extends Object> boolean isDisjoint(Set<T> set1, Set<T> set2){
		return intersection(set1, set2).size()==0;
	}
	
	public static<T extends Object> Set<T> symmetricDifference(Set<T> set1, Set<T> set2){
		return difference(union(set1,set2),intersection(set1,set2));
	}
}

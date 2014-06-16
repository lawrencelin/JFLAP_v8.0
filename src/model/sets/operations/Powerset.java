package model.sets.operations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import model.languages.SetOperators;
import model.sets.FiniteSet;
import model.sets.InfiniteSet;
import model.sets.elements.Element;
import universe.preferences.JFLAPPreferences;

public class Powerset extends SetOperation {

	
	@Override
	protected FiniteSet getFiniteAnswer() {
		return new FiniteSet(getDescription(), powerset(myOperands.get(0).getSet()));
	}


	@Override
	protected InfiniteSet getInfiniteAnswer() {
		// TODO Auto-generated method stub
		return null;
	}


	@SuppressWarnings("unchecked")
	private Set<Element> powerset (Set<Element> original) {
		Set<Set<Element>> intermediate = SetOperators.powerSet(original);
		Set<Element> answer = new TreeSet<Element>();
		for (Set<Element> e : intermediate) {
			answer.add(new Element(getSetToString(e)));
		}
		return answer;
	}
	
	@SuppressWarnings("unchecked")
	private String getSetToString (Set<Element> set) {
		if (set.size() == 0)
			return JFLAPPreferences.getEmptySetString();
		StringBuilder sb = new StringBuilder();
		ArrayList<Element> list = new ArrayList<Element>(set);
		Collections.sort(list);
		sb.append("{");
		for (int i = 0; i < set.size() - 1; i++) {
			sb.append(list.get(i) + ", ");
		}
		sb.append(list.get(list.size()-1) + "}");
		return sb.toString();
	}
	

	@Override
	public int getNumberOfOperands() {
		return 1;
	}

	@Override
	public String getName() {
		return "Powerset";
	}

	@Override
	public String getDescription() {
		return "The powerset of " + myOperands.get(0).getName();
	}
	
	

}

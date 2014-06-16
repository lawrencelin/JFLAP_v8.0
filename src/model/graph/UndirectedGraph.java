package model.graph;

import java.util.Set;

import model.automata.State;

public class UndirectedGraph<T> extends Graph<T> {

	@Override
	public boolean isDirected() {
		return false;
	}
}

package model.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.automata.Automaton;
import model.automata.State;
import model.automata.SingleInputTransition;

/**
 * A helper class that finds simple paths in any automaton
 * graph. This is a very basic implementation, and this
 * class could be made much more potent/useful!
 * 
 * @author Julian Genkins
 *
 */
public class PathFinder {

	private Graph myGraph;
	private Set<Object> myVisited;

	public PathFinder(Graph g) {
		myGraph = g;
		myVisited = new TreeSet<Object>();
	}

	public PathFinder(Automaton m) {
		this(new TransitionGraph(m));
	}

	public <T> List<T> findPath(T from, T to) {
		List<T> path = recurseForPath(from, to);
		clear();
		if (path.size() < 2 && !from.equals(to)) return null;
		return path;
	}

	private void clear() {
		myVisited.clear();
	}

	private <T> List<T> recurseForPath(T from, T to) {
		if (myVisited.contains(from))
			return new ArrayList<T>();
		
		myVisited.add(from);
		List<T> path = new ArrayList<T>();
		path.add(from);
		if (from.equals(to)){
			return path;
		}
		

		for (Object v: myGraph.adjacent(from))
		{
			List<T> nextPath = recurseForPath((T)v, to);
			
			if (!nextPath.isEmpty()){
				path.addAll(nextPath);
				return path;
			}
		}
		
		return new ArrayList<T>();
	}
	
	

}

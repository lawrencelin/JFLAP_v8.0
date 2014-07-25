package model.algorithms.transform.grammar;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import util.JFLAPConstants;

import debug.JFLAPDebug;
import errors.BooleanWrapper;

import model.automata.State;
import model.automata.StateSet;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.PathFinder;
import model.graph.layout.CircleLayoutAlgorithm;
import model.symbols.Symbol;
/*
public class DependencyGraph extends DirectedGraph<Variable> implements JFLAPConstants{

	
	public DependencyGraph(VariableAlphabet vars){
		Set<Variable> set = new HashSet<Variable>();
		for (Symbol v: vars){
			addVertex((Variable) v, new Point2D.Double());
			set.add((Variable) v);
		}
		
	}
	
	public void layout(Dimension d) {
		CircleLayoutAlgorithm alg = new CircleLayoutAlgorithm(d, 
				new Dimension(JFLAPConstants.STATE_RADIUS, JFLAPConstants.STATE_RADIUS), 
				200);
		alg.layout(this, new HashSet<Variable>());
	}
	
	public boolean addDependency(Variable from, Variable to){
		if (from.equals(to))
			return false;
		
		return super.addEdge(from, to);

	}
	
	public boolean removeDependence(Variable from, Variable to){
		return removeEdge(from, to);
	}

	public int getNumberDependencies() {
		return super.totalDegree();
	}
	
	public Variable[] getAllDependencies(Variable from){
        PathFinder finder = new PathFinder(this);
        Set<Variable> dep = new TreeSet<Variable>();
        for (Variable to: this.vertices()){
            if (finder.findPath(from, to) != null &&
                            !from.equals(to))
                    dep.add(to);
        }
        return dep.toArray(new Variable[0]);
	}
	
	@Override
	public String toString() {
		String out = "";
		for (Variable v: vertices()){
			out += v + ": " + adjacent(v) + "\n";
		}
		return out;
	}
	
}
*/

public class DependencyGraph extends DirectedGraph<State> implements JFLAPConstants{
	
	private Map<State, Set<State>> myDependencies;
	private Grammar myGrammar;
	private StateSet myStates;
	
	public DependencyGraph (StateSet set, Grammar g) {
		myGrammar = g;
		myStates = set;
		for (State s: set){
			super.addVertex((State) s, new Point2D.Double());
		}
		myDependencies = generateDependencyMap();
	}

	private Map<State, Set<State>> generateDependencyMap() {
		Map<State, Set<State>> map = new HashMap<State, Set<State>>();
		for (Production p : myGrammar.getProductionSet()) {
			Set<Variable> l = p.getVariablesOnLHS();
			Set<Variable> r = p.getVariablesOnRHS();
			State left= null;
			Set<State> right = new HashSet<State>();
			for (State s : myStates) {
				for (Variable v : l) {
					if (s.getName().equals(v.getString())) {
						left = s;
					}
				}
				for (Variable v : r) {
					if (s.getName().equals(v.getString()) && !s.equals(left)) {
						right.add(s);
					}
				}
				if (left != null && !right.isEmpty()) {
					map.put(left, right);
					for (State temp : right) {
						super.addEdge(left, temp);
					}
				}
			}
		}
		return map;
	}
	
	public Variable[] getAllDependencies(Variable v){
		State from = null;
		for (State s : myStates) {
			if (s.toString().equals(v.toString())) {
				from = s;
			}
		}
		if (from == null) return null;
        PathFinder finder = new PathFinder(this);
        Set<Variable> dep = new TreeSet<Variable>();
        for (State to: this.vertices()){
//        	System.out.println(to.toDetailedString());
            if (finder.findPath(from, to) != null &&
                            !from.equals(to)) {
            	dep.add(new Variable(to.toString()));
            }
                    
        }
//        System.out.println(dep.toString());
        return dep.toArray(new Variable[0]);
	}
	
	public Set<State> getDependency(State s) {
		if (myDependencies.containsKey(s)) {
			return myDependencies.get(s);
		}
		return null;
	} 
	
	public int getTotalDependencies() {
		int total = 0;
		for (State s : myDependencies.keySet()) {
			total+=myDependencies.get(s).size();
		}
		return total;
	}
	
	public BooleanWrapper addDependency(State from, State to){
		Set<State> dep = myDependencies.get(from);
		if (dep == null || !dep.contains(to)){
			return new BooleanWrapper(true, "There is no valid " +
					"dependency from " + from + " to " + to);
		}
		return new BooleanWrapper(false, 
				"The dependency from " + from + " to " + to + " has already been added.");
	}
	
	@Override
	public String toString() {
		String out = "";
		for (State s: this.vertices()){
			out += s + ": " + adjacent(s) + "\n";
		}
		return out;
	}
}
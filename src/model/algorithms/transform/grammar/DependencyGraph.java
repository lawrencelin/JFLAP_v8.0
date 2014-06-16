package model.algorithms.transform.grammar;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.grammar.Variable;
import model.grammar.VariableAlphabet;
import model.graph.DirectedGraph;
import model.graph.Graph;
import model.graph.PathFinder;
import model.graph.layout.CircleLayoutAlgorithm;
import model.symbols.Symbol;

public class DependencyGraph extends DirectedGraph<Variable>{

	
	public DependencyGraph(VariableAlphabet vars){
		for (Symbol v: vars){
			addVertex((Variable) v, new Point2D.Double());
		}
		CircleLayoutAlgorithm alg = new CircleLayoutAlgorithm();
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

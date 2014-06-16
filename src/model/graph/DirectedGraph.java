package model.graph;

import java.util.Set;

import model.automata.State;

public class DirectedGraph<T> extends Graph<T> {

	
	/**
	 * Returns the number of vertices that point from this object.  
	 * 
	 * @param from the object to get the degree for.
	 * @param excludeSameVertexEdges boolean that if <i>true</i> will exclude edges
	 * leading from and to the same vertex from consideration.
	 */
	public int fromDegree(T from, boolean excludeSameVertexEdges) {
		if (!excludeSameVertexEdges)
			return super.degree(from);		
		int count = 0;
		Set<T>	vertices = vertices();
		for (T v: vertices)			
			if (hasEdge(from, v) && !v.equals(from))
				count++;
		return count;
	}
	
	/**
	 * Returns the number of vertices that point to this object.  
	 * 
	 * @param to the object to get the degree for.
	 * @param excludeSameVertexEdges boolean that if <i>true</i> will exclude edges
	 * leading from and to the same vertex from consideration.
	 */
	public int toDegree(T to, boolean excludeSameVertexEdges) {				
		int count = 0;
		Set<T> vertices = vertices();
		for (T v: vertices)			
			if (hasEdge(v, to) && (!v.equals(to) || !excludeSameVertexEdges))
				count++;
		return count;
	}

	@Override
	public boolean isDirected() {
		return true;
	}
}

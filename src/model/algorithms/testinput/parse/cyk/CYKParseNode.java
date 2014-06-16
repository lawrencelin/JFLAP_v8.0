package model.algorithms.testinput.parse.cyk;

import model.grammar.Production;
import model.grammar.Variable;
import model.symbols.SymbolString;

/**
 * The <CODE>CYKParseNode</CODE> is a class that keeps track of a single 
 * production that can derive a specific string and at what index in the 
 * string the two RHS variables are separated.
 * 
 * @author Ian McMahon
 *
 */
public class CYKParseNode {
	private Variable LHS;
	private SymbolString RHS;
	private int k;
	
	/**
	 * This will instantiate a new <CODE>CKYParseNode</CODE> with a 
	 * production and index for which a specific string can be derived
	 * from.
	 * @param p
	 * 		the <CODE>Production</CODE> that derives the specific string.
	 * @param k
	 * 		the index at which the two RHS variables are split.
	 */
	public CYKParseNode(Production p, int k){
		LHS = (Variable) p.getLHS()[0];
		RHS = new SymbolString(p.getRHS());
		this.k = k;
	}
	
	/**
	 * @return
	 * 		The single variable on the LHS of the production.
	 */
	public Variable getLHS(){
		return LHS;
	}
	
	/**
	 * @return
	 * 		The two variables or single terminal on the RHS of the production.
	 */
	public SymbolString getRHS(){
		return RHS;
	}
	
	/**
	 * @return
	 * 		The leftmost variable on the RHS of the production.
	 */
	public Variable getFirstRHSVariable(){
		return (Variable) RHS.getFirst();
	}
	
	/**
	 * @return
	 * 		The rightmost variable on the RHS of the production.
	 */
	public Variable getSecondRHSVariable(){
		return (Variable) RHS.getLast();
	}
	
	/**
	 * @return
	 * 		The index of the string that splits the two RHS variables. 
	 * The first variable derives the string up to and including 
	 * index k, and the second variable derives the remainder (k+1 through end of string).
	 */
	public int getK(){
		return k;
	}
	
	
	/**
	 * Returns a string representation of the node
	 */
	public String toString() {
		if (LHS == null)
			return "[ ]";
		return LHS.toString();
	}
}

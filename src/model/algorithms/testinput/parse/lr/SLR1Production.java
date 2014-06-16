package model.algorithms.testinput.parse.lr;

import universe.preferences.JFLAPPreferences;
import model.grammar.Production;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class SLR1Production extends Production {

	private int myMarkIndex;

	public SLR1Production(Production p, int i){
		super(p.getLHS(), constructRHS(p.getRHS()));
			myMarkIndex = i;
	}

	private static Symbol[] constructRHS(Symbol[] rhs) {
		if (rhs.length == 0){
			rhs = new Symbol[]{JFLAPPreferences.getSubForEmptyString()};
		}
		return rhs;
	}

	public SLR1Production(Production p){
		this(p, p.isLambdaProduction() ? 1 : 0);
	}
	
	@Override
	public Symbol[] getRHS() {
		SymbolString newRHS = new SymbolString(super.getRHS());
		newRHS.add(myMarkIndex, JFLAPPreferences.SLR_MARKER);
		return newRHS.toArray(new Symbol[0]);
	}
	
	public Production createNormalProduction(){
		SymbolString lhs = new SymbolString(this.getLHS());
		SymbolString rhs = getMarkerFreeRHS();
		return new Production(lhs, rhs);
	}

	private SymbolString getMarkerFreeRHS() {
		SymbolString rhs = new SymbolString(getRHS());
		rhs.remove(JFLAPPreferences.SLR_MARKER);
		rhs.remove(JFLAPPreferences.getSubForEmptyString());
		return rhs;
	}
	
	public Symbol getSymbolAfterMarker(){
		if (isReduceProduction())
			return null;

		return super.getRHS()[myMarkIndex];	
	}

	public boolean isReduceProduction() {
		return myMarkIndex == super.getRHS().length;
	}

	public void shiftMarker() {
		myMarkIndex++;
	}
	
	@Override
	public SLR1Production copy() {
		return new SLR1Production(this.createNormalProduction(), myMarkIndex);
	}
	
	@Override
	public int compareTo(Production o) {
		int compare = super.compareTo(o);
		if (compare == 0){
			compare = myMarkIndex - ((SLR1Production)o).myMarkIndex;
		}
		return compare;
	}
	
	@Override
	public String toString() {
		return new SymbolString(getLHS()) + "->" + new SymbolString(getRHS());
	}
	
}

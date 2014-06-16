package view.grammar.productions;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import model.change.events.AddEvent;
import model.change.events.RemoveEvent;
import model.change.events.SetToEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import model.grammar.ProductionSet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.CompoundUndoRedo;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import errors.BooleanWrapper;



public class ProductionDataHelper extends ArrayList<Object[]> 
									implements JFLAPConstants{

	private static final Object[] EMPTY = new Object[]{"",
		PRODUCTION_ARROW,
	""};
	private LinkedList<BooleanWrapper> myWrappers;
	private ArrayList<Production> myOrderedProductions;
	private ProductionSet myProductions;
	private UndoKeeper myKeeper;
	protected Grammar myGrammar;

	public ProductionDataHelper(Grammar model, UndoKeeper keeper){
		myKeeper = keeper;
		myWrappers = new LinkedList<BooleanWrapper>();
		myGrammar = model;
		myProductions = myGrammar.getProductionSet();
		myOrderedProductions = 
				new ArrayList<Production>(myProductions);
	}

	@Override
	public void add(int index, Object[] input) {
		Production p = this.objectToProduction(input);
		if(isValid(p)){
			TableAddProdEvent add2 = new TableAddProdEvent(p, index);
			myKeeper.applyAndListen(add2);
		}
	}

	@Override
	public Object[] set(int index, Object[] input) {
		Object[] old = this.get(index);
		if (index >= this.myOrderedProductions.size())
			this.add(input); 
		else {
			Production to = this.objectToProduction(input);
			if (isValid(to)){
				Production from = myOrderedProductions.get(index);
				SetToEvent<Production> set = 
						new SetToEvent<Production>(from, from.copy(), to);
				if (!myKeeper.applyAndListen(set));
			}
			else{
				remove(index);
			}
		}

		return old;
	}

	public boolean isValid(Production p) {
		return !(p == null || p.isEmpty());
	}




	@Override
	public boolean add(Object[] input) {
		int s = this.size()-1;
		this.add(s, input);
		return s < this.size();
	}

	@Override
	public void clear() {
		myProductions.clear();
		myOrderedProductions.clear();
	}


	@Override
	public Object[] get(int index) {
		if (index >= myOrderedProductions.size()) return EMPTY;
		return myOrderedProductions.get(index).toArray();
	}

	@Override
	public Object[] remove(int index) {
		if (index >= myOrderedProductions.size()) return EMPTY;
		Production remove = myOrderedProductions.get(index);
		RemoveOrderedProdEvent event =
				new RemoveOrderedProdEvent(remove, index);
		myKeeper.applyAndListen(event);
		return remove.toArray();
	}

	@Override
	public int size() {
		return myOrderedProductions.size() + 1;
	}

	@Override
	public Iterator<Object[]> iterator() {
		ArrayList<Object[]> converted = new ArrayList<Object[]>();
		for (Production p: myOrderedProductions)
			converted.add(p.toArray());
		return converted.iterator();
	}


	protected Production objectToProduction(Object[] input){

//		if(!SymbolString.canBeParsed((String) input[0], getGrammar())){
//			checkAndAddError(new BooleanWrapper(false, 
//					"The LHS of this production has a bad character at index " + LHS.toString().length() + "."));
//			return null;
//		}
//		if(!SymbolString.canBeParsed((String) input[2], getGrammar())){
//			checkAndAddError(new BooleanWrapper(false, 
//					"The RHS of this production has a bad character at index " + RHS.toString().length() + "."));
//			return null;
//		}
		if (isEmptyString((String) input[0]))
			input[0] = "";
		if (isEmptyString((String) input[2]))
			input[2] = "";
		SymbolString LHS = Symbolizers.symbolize((String) input[0], myGrammar),
				RHS = Symbolizers.symbolize((String) input[2], myGrammar);
		return new Production(LHS, RHS);
	}

	protected boolean isEmptyString(String object) {
		return object.equals(JFLAPPreferences.getEmptyString());
	}


	public Production[] getOrderedProductions() {
		return myOrderedProductions.toArray(new Production[0]);
	}
	
	protected void addProduction(int i, Production p){
		myOrderedProductions.add(i, p);
	}
	
	public boolean applyOrdering(Production[] order){
		for (int i = 0; i< order.length;i++){
			Production p = order[i];
			boolean applied = myOrderedProductions.remove(p) &&
								myOrderedProductions.add(p);
			if (!applied)
				return applied;
		}
		return true;
	}

	
	
	private class TableAddProdEvent extends CompoundUndoRedo{

		private Production myProduction;
		private int myIndex;

		public TableAddProdEvent(Production p, int i) {
			super(new AddEvent<Production>(myProductions, p));
			myProduction = p;
			myIndex = i;
		}

		@Override
		public boolean undo() {
			return super.undo() && myOrderedProductions.remove(myIndex) != null;
		}

		@Override
		public boolean redo() {
			boolean redone = super.redo();
			if (redone)
				myOrderedProductions.add(myIndex, myProduction);
			return redone;
		}

	}
	
	private class RemoveOrderedProdEvent extends CompoundUndoRedo{

		private Production myProduction;
		private int myIndex;

		public RemoveOrderedProdEvent(Production p, int i) {
			super(new RemoveEvent<Production>(myProductions, p));
			myProduction = p;
			myIndex = i;
		}


		@Override
		public boolean undo() {
			boolean undone = super.undo();
			if (undone)
				myOrderedProductions.add(myIndex, myProduction);
			return undone;
		}

		@Override
		public boolean redo() {
			return myOrderedProductions.remove(myIndex) != null && super.redo();
		}

	}

}

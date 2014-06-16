package model.automata.acceptors.pda;

import java.util.Set;

import util.UtilFunctions;

import model.automata.State;
import model.automata.SingleInputTransition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class PDATransition extends SingleInputTransition<PDATransition> {

	private SymbolString myPop;
	private SymbolString myPush;

	public PDATransition(State from, State to) {
		this(from, to, new SymbolString(),new SymbolString(), new SymbolString());
	}
	
	public PDATransition(State from, State to, Symbol in, Symbol pop, Symbol push) {
		this(from, to, new SymbolString(in),new SymbolString(pop), new SymbolString(push));
	}
	
	public PDATransition(State from, State to, SymbolString input, SymbolString pop, SymbolString push) {
		super(from, to, input);
		setPop(pop);
		setPush(push);
	}

	
	
	@Override
	public String getDescriptionName() {
		return "Pushdown Automaton Transition";
	}

	@Override
	public String getDescription() {
		return "This is a transition reserved for basic PDAs and any variation thereof. " +
				"This transition function maps an input SymbolString, State, and stack symbol to " +
				"a next state and symbols to push onto the stack.";
	}

	public Symbol[] getPop() {
		return myPop.toArray(new Symbol[0]);
	}



	public void setPop(SymbolString pop) {
		this.myPop = pop;
	}



	public Symbol[] getPush() {
		return myPush.toArray(new Symbol[0]);
	}



	public void setPush(SymbolString push) {
		this.myPush = push;
	}

	@Override
	public int compareTo(PDATransition o) {
		int comp;
		if ((comp = super.compareTo(o)) != 0) return comp;
		return UtilFunctions.metaCompare(new Comparable[]{myPop, myPush}, 
											new Comparable[]{o.myPop, o.myPush});
	}
	
	@Override
	public PDATransition copy() {
		return copy(this.getFromState().copy(), this.getToState().copy());
	}
	
	@Override
	public PDATransition copy(State from, State to) {
		return new PDATransition(from, to, 
									new SymbolString(getInput()),
									new SymbolString(this.getPop()), 
									new SymbolString(this.getPush()));
	}



	@Override
	public String getLabelText() {
		return super.getLabelText() + ", " + myPop + "; " + myPush;
	}



	
	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof StackAlphabet){
			return new SymbolString[]{myPop, myPush};
		}
		return super.getPartsForAlphabet(a);
	}

	
	@Override
	protected void applySetTo(PDATransition other) {
		super.applySetTo(other);
		this.myPop.setTo(other.myPop);
		this.myPush.setTo(other.myPush);
	}



	@Override
	public SymbolString[] getAllParts() {
		return UtilFunctions.combine(super.getAllParts(),myPop, myPush);
	}
	
	


}

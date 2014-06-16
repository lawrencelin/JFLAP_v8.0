package model.automata.turing;

import java.util.ArrayList;
import java.util.List;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.UtilFunctions;
import model.automata.AutomatonException;
import model.automata.InputAlphabet;
import model.automata.State;
import model.automata.Transition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class MultiTapeTMTransition extends Transition<MultiTapeTMTransition> {

	private Symbol[] myWrites;
	private TuringMachineMove[] myMoves;
	private Symbol[] myReads;

	public MultiTapeTMTransition(State from, State to){
		this(from,to,
				JFLAPPreferences.getTMBlankSymbol(),
				JFLAPPreferences.getTMBlankSymbol(),
				TuringMachineMove.RIGHT);
	}
	
	public MultiTapeTMTransition(State from, 
										State to,
										Symbol read,
										Symbol write,
										TuringMachineMove move){
		this(from, to, 
				new Symbol[]{read},
				new Symbol[]{write},
				new TuringMachineMove[]{move} );
	}
	
	public MultiTapeTMTransition(State from, 
							State to, 
							Symbol[] read, 
							Symbol[] write, 
							TuringMachineMove[] move) {
		super(from, to);
		
		if (!(read.length == write.length && 
				write.length == move.length))
			throw new AutomatonException("The turing machine transition cannot" +
					" be created with unequal numbers of reads/writes/moves.");
		
		myReads = read;
		myWrites = write;
		myMoves = move;
	}
	
	public Symbol getWrite(int tape){
		return myWrites[tape];
	}
	
	public Symbol getRead(int tape){
		return myReads[tape];
	}
	
	public TuringMachineMove getMove(int tape){
		return myMoves[tape];
	}
	
	public boolean setMove(TuringMachineMove move, int tape){
		MultiTapeTMTransition copy = this.copy();
		copy.myMoves[tape] = move;
		return setTo(copy);
	}
	
	public boolean setRead(Symbol read, int tape){
		MultiTapeTMTransition copy = this.copy();
		copy.myReads[tape] = read;
		return setTo(copy);
	}
	
	public boolean setWrite(Symbol write, int tape){
		MultiTapeTMTransition copy = this.copy();
		copy.myWrites[tape] = write;
		return setTo(copy);	}

	@Override
	public String getDescriptionName() {
		return "Multi-tape Turing Machine Transition";
	}

	@Override
	public String getDescription() {
		return "The transition for a multi-tape turing machine";
	}

	@Override
	public MultiTapeTMTransition copy() {
		return copy(this.getFromState().copy(), this.getToState().copy());
	}


	@Override
	public MultiTapeTMTransition copy(State from, State to) {
		return new MultiTapeTMTransition(from, 
										to, 
										copy(myReads), 
										copy(myWrites), 
										myMoves);
	}
	

	private Symbol[] copy(Symbol[] in) {
		Symbol[] newAr = new Symbol[in.length];
		for (int i = 0; i< in.length; i++){
			newAr[i] = in[i].copy();
		}
		return newAr;
	}


	@Override
	public String getLabelText() {
		String label = "";
		for (int i = 0; i < this.getNumTapes(); i++){
			label += this.getRead(i) + "; " + 
					this.getWrite(i) + ", " + 
					this.getMove(i) + " | ";
		}
		return label.substring(0,label.length()-3);
	}

	public int getNumTapes() {
		return myReads.length;
	}

	@Override
	public int compareTo(MultiTapeTMTransition o) {
		int compare = super.compareTo(o);
		if (compare == 0)
			compare = new Integer(this.getNumTapes()).compareTo(o.getNumTapes());
		if (compare == 0){
			for (int i = 0; i < this.getNumTapes(); i++){
				compare = this.getRead(i).compareTo(o.getRead(i));
				if (compare != 0) return compare;
				compare = this.getWrite(i).compareTo(o.getWrite(i));
				if (compare != 0) return compare;
				compare = this.getMove(i).compareTo(o.getMove(i));
				if (compare != 0) return compare;
			}
		}
		return compare;
	}

	@Override
	protected void applySetTo(MultiTapeTMTransition other) {
		super.applySetTo(other);
		for (int i = 0; i < this.getNumTapes(); i++){
			myReads[i] = other.getRead(i);
			myWrites[i] = other.getWrite(i);
			myMoves[i] = other.getMove(i);
			
		}
	}
	
	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		if (a instanceof TapeAlphabet || a instanceof InputAlphabet){
			SymbolString[] parts =  getAllParts();
			if (a instanceof InputAlphabet){
				for (SymbolString s: parts){
					s.removeEach(JFLAPPreferences.getTMBlankSymbol());
				}
			}
			return parts;
		}
		return new SymbolString[0];
	}

	@Override
	public boolean isLambdaTransition() {
		return false;
	}

	@Override
	public SymbolString[] getAllParts() {
		List<SymbolString> all = new ArrayList<SymbolString>();
		for( Symbol s: UtilFunctions.combine(myReads,myWrites)){
			all.add(new SymbolString(s));
		}
		return all.toArray(new SymbolString[0]);
	}

}

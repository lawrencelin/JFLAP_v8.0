package model.automata.turing.buildingblock;

import java.util.Set;

import model.automata.AutomatonException;
import model.automata.InputAlphabet;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;

public class BlockTransition extends SingleInputTransition<BlockTransition> implements JFLAPConstants {

	public BlockTransition(Block from, Block to, SymbolString input){
		super(from, to, input);
		checkInput(input);
	}

	public BlockTransition(Block from, Block to, Symbol input) {
		this(from,to,new SymbolString(input));
	}

	public BlockTransition(Block from, Block to) {
		this(from,to,new SymbolString());
	}
	
	@Override
	public String getDescriptionName() {
		return "Building Block Transition";
	}

	@Override
	public String getDescription() {
		return "The transition for a Building Block Turing Machine";
	}

	@Override
	public Block getFromState(){
		return (Block) super.getFromState();
	}

	@Override
	public Block getToState(){
		return (Block) super.getToState();
	}

	@Override
	public BlockTransition copy() {
		return copy(this.getFromState(), this.getToState());
	}
	
	@Override
	public BlockTransition copy(State from, State to) {
		return new BlockTransition((Block) from, (Block) to, new SymbolString(this.getInput()));
	}

	@Override
	public boolean setInput(SymbolString input) {
		if (input.isEmpty())
			input.add(JFLAPPreferences.getTMBlankSymbol());
		
		checkInput(input);
		
		return super.setInput(input);
	}

	private void checkInput(SymbolString input) {
		Symbol not = new Symbol(NOT);
		int i = input.startsWith(not) ? 1 : 0;
		SymbolString symbol = input.subList(i);
		
		if (symbol.isEmpty())
			input.add(JFLAPPreferences.getTMBlankSymbol());
		
		if (symbol.size() > 1)
			throw new AutomatonException("You may not set the input on a Turing machine block transition " +
					"to a string of more than one symbols.");
		if (input.get(i).equals(not))
			throw new AutomatonException("You may not set the input on a Turing machine block transition " +
					"to a string consisting of multiple negations.");
		if (input.startsWith(not) && input.get(i).equals(new Symbol(TILDE)))
			throw new AutomatonException("Tilde (~) represents any symbol.\nYou may not set the input on a Turing machine block transition " +
					"to a string that represents nothing.");
	}

	@Override
	public boolean setTo(BlockTransition other) {
		return super.setTo(other);
	}

	@Override
	public SymbolString[] getPartsForAlphabet(Alphabet a) {
		SymbolString[] strings = super.getPartsForAlphabet(a);
		if (a instanceof InputAlphabet){
			for (int i =0; i< strings.length; i++){
				SymbolString temp = new SymbolString(strings[i]);
				temp.remove(JFLAPPreferences.getTMBlankSymbol());
				strings[i] = temp;
			}
		}
		return strings;
	}

	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> symbols = super.getSymbolsUsedForAlphabet(a);
		symbols.remove(new Symbol(TILDE));
		symbols.remove(new Symbol(NOT));
		return symbols;
	}
}

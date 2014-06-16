package model.automata.turing.buildingblock;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.StateSet;
import model.automata.turing.MultiTapeTuringMachine;
import model.formaldef.UsesSymbols;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;

/**
 * StateSet replacement for Block TMs, using Blocks instead of 
 * States.
 * 
 * @author Ian McMahon
 *
 */
public class BlockSet extends StateSet implements UsesSymbols{
	
	@Override
	public Block getStateWithID(int id) {
		return (Block) super.getStateWithID(id);
	}

	@Deprecated
	public Block createAndAddState() {
		throw new RuntimeException("Don't use this method, use createAndAddState(TuringMachine tm) instead");
	}
	
	public Block createAndAddState(MultiTapeTuringMachine tm, String name){
		int id = this.getNextUnusedID();
		Block s = new Block(tm, name, id);
		this.add(s);
		return s;
	}
	
	@Override
	public BlockSet copy() {
		return (BlockSet) super.copy();
	}
	
	@Override
	public Set<Symbol> getSymbolsUsedForAlphabet(Alphabet a) {
		Set<Symbol> symbolAlphabet = new TreeSet<Symbol>();
		for(State block : this){
			symbolAlphabet.addAll(((Block) block).getSymbolsUsedForAlphabet(a));
		}
		return symbolAlphabet;
	}

	@Override
	public boolean applySymbolMod(String from, String to) {
		boolean changed = false;
		for(State block : this){
			if(((Block) block).applySymbolMod(from, to)){
				changed = true;
			}
		}
		return changed;
	}

	@Override
	public boolean purgeOfSymbols(Alphabet a, Collection<Symbol> s) {
		boolean changed = false;
		for(State block : this){
			if(((Block) block).purgeOfSymbols(a, s)){
				changed = true;
			}
		}
		return changed;
	}
}

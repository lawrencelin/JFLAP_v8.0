package model.automata.turing.universal;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.library.BlockTMUpdatingBlock;
import model.symbols.Symbol;
import model.symbols.SymbolString;
import universe.preferences.JFLAPPreferences;

public abstract class MappingBlock extends BlockTMUpdatingBlock {

	public MappingBlock(TapeAlphabet alph, String name,
			int id, Object ... args) {
		super(alph, name, id, args);
	}

	@Override
	public void updateTuringMachine(TapeAlphabet tape) {
		TapeAlphabet alph = getTuringMachine().getTapeAlphabet();

		alph.retainAll(tape);
		alph.addAll(tape);
		alph.add(new Symbol(TM_MARKER));
		
		Set<Symbol> set = tape.toCopiedSet();
		set.remove(new Symbol(TM_MARKER)); 
		set.remove(new Symbol("0"));
		set.remove(new Symbol("1"));
		set.remove(new BlankSymbol().getSymbol());
		updateTuringMachine(createMap(set));
	}

	
	public abstract void updateTuringMachine(Map<Symbol, SymbolString> encodingMap);

	public static Map<Symbol, SymbolString> createMap(Set<Symbol> alph){
		Map<Symbol, SymbolString> map = new TreeMap<Symbol, SymbolString>();
		Symbol[] symbols = alph.toArray(new Symbol[0]);
		Symbol one = new Symbol("1");
		for (int i = 0; i < alph.size(); i++){
			SymbolString ones = new SymbolString();
			
			for (int j = 0; j<i+2; j++)
				ones.add(one);
			
			map.put(symbols[i], ones);
		}
		
		map.put(JFLAPPreferences.getTMBlankSymbol(), new SymbolString(one));
		return map;
	}
}

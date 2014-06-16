package file.xml.formaldef.components.functions.transitions.tm;

import java.util.Arrays;
import java.util.Map;

import model.automata.InputAlphabet;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.Transition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockTransition;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

import util.JFLAPConstants;

import file.xml.XMLTransducer;
import file.xml.formaldef.components.functions.transitions.InputTransitionTransducer;
import file.xml.formaldef.components.symbols.SymbolStringTransducer;

public class BlockTransitionTransducer extends InputTransitionTransducer<BlockTransition> {

	public BlockTransitionTransducer(InputAlphabet alph, TapeAlphabet tape) {
		super(alph, tape);
	}

	@Override
	public BlockTransition createTransition(State from, State to,
			SymbolString input, Map<String, Object> remaining) {
		
		return new BlockTransition(toBlock(from), toBlock(to), input);
	}

	private Block toBlock(State from) {
		return new Block(null, from.getName(), from.getID());
	}

@Override
public String getInputTag() {
	return TM_READ_TAG;
}	
	
	@Override
	public XMLTransducer getSymbolStringTransducer(String tag, Alphabet[] alphs) {
		return new BlockSymbolStringTransducer(tag, alphs);
	}
	
	
	private class BlockSymbolStringTransducer extends SymbolStringTransducer{

		public BlockSymbolStringTransducer(String tag, Alphabet[] alphs) {
			super(tag, alphs);
		}

		@Override
		public SymbolString createInstance(String text) {
			SymbolString base = new SymbolString();
			if (text == null) {
				return base;
			}
			if (text.equals(JFLAPConstants.TILDE)){
				base.add(new Symbol(text));
				return base;
			}
			else if(text.startsWith(JFLAPConstants.NOT)){
				base.add(new Symbol(JFLAPConstants.NOT));
				text = text.substring(1);
			}
				
			base.addAll(super.createInstance(text));
			return base;
		}
		
		
		
	}
}

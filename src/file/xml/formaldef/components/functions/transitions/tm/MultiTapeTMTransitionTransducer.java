package file.xml.formaldef.components.functions.transitions.tm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.automata.InputAlphabet;
import model.automata.SingleInputTransition;
import model.automata.State;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachineMove;
import model.formaldef.components.alphabets.Alphabet;
import model.formaldef.components.functionset.function.LanguageFunction;
import model.symbols.Symbol;
import model.symbols.SymbolString;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import debug.JFLAPDebug;

import file.xml.XMLTransducer;
import file.xml.formaldef.components.SingleNodeTransducer;
import file.xml.formaldef.components.functions.transitions.TransitionTransducer;
import file.xml.formaldef.components.symbols.SymbolTransducer;

public class MultiTapeTMTransitionTransducer extends TransitionTransducer<MultiTapeTMTransition> {


	private static final Pattern p = Pattern.compile("("+READ_TAG + "|" + WRITE_TAG + ")([0-9]+)");

	public MultiTapeTMTransitionTransducer(InputAlphabet input, TapeAlphabet tape) {
		super(input, tape);
	}

	@Override
	public Map<String, Object> addOtherLabelComponentsToMap(
			Map<String, Object> base, MultiTapeTMTransition trans) {
		for(int i = 0; i < trans.getNumTapes(); i++){
			base.put(READ_TAG + i, trans.getRead(i));
			base.put(WRITE_TAG + i, trans.getWrite(i));
			base.put(MOVE_TAG + i, trans.getMove(i));
		}
		base.put(TAPE_NUM, trans.getNumTapes());
		return base;
	}

	@Override
	public XMLTransducer getTransducerForTag(String tag) {
		if (p.matcher(tag).find())
			return new SymbolTransducer(tag);
		else if (tag.equals(TAPE_NUM))
			return new IntegerTransducer(tag);
		else if (tag.startsWith(MOVE_TAG))
			return new TMMoveTransducer(Integer.parseInt(tag.substring(tag.length()-1)));
		return super.getTransducerForTag(tag);
	}
	
	@Override
	public MultiTapeTMTransition createTransition(State from, State to,
			Map<String, Object> remaining) {
		
		int tapes = (Integer) remaining.get(TAPE_NUM);
		List<Symbol> read = retrieveArray(READ_TAG, remaining, Symbol.class, tapes);
		List<Symbol> write = retrieveArray(WRITE_TAG, remaining, Symbol.class, tapes);
		List<TuringMachineMove> move = 
				retrieveArray(MOVE_TAG, remaining, TuringMachineMove.class, tapes);

		
		
		return new MultiTapeTMTransition(from, to, 
				read.toArray(new Symbol[0]), 
				write.toArray(new Symbol[0]), 
				move.toArray(new TuringMachineMove[0]));
	}

	private <T> List<T> retrieveArray(String tag,
			Map<String, Object> remaining, Class<T> class1,
			int tapes) {
		List<T> toReturn = new ArrayList<T>();
		for (int i = 0; i< tapes; i++){
			String tag2 = tag+i;
			toReturn.add((T) remaining.remove(tag2));
		}

		return toReturn;
	}



}

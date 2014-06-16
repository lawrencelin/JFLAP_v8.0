package model.languages;

import model.algorithms.conversion.ConversionAlgorithm;
import model.algorithms.conversion.autotogram.FSAtoRegGrammarConversion;
import model.algorithms.conversion.autotogram.PDAtoCFGConverter;
import model.algorithms.conversion.autotogram.TMtoGrammarConversion;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.algorithms.transform.turing.StayOptionRemover;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.automata.turing.MultiTapeTuringMachine;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.regex.RegularExpression;

public class GrammarFactory {

	public static Grammar createGrammar(FormalDefinition formalDef) {
		if (formalDef instanceof Grammar)
			return (Grammar) formalDef;
		ConversionAlgorithm convert = null;

		if (formalDef instanceof FiniteStateAcceptor) {
			convert = new FSAtoRegGrammarConversion(
					(FiniteStateAcceptor) formalDef);
		}

		else if (formalDef instanceof PushdownAutomaton) {
			convert = new PDAtoCFGConverter((PushdownAutomaton) formalDef);
		}

		else if (formalDef instanceof MultiTapeTuringMachine) {
			StayOptionRemover remover = new StayOptionRemover(
					(MultiTapeTuringMachine) formalDef);
			remover.stepToCompletion();
			convert = new TMtoGrammarConversion(
					remover.getTransformedDefinition());
		}

		else if (formalDef instanceof RegularExpression) {
			RegularExpressionToNFAConversion toFSA = new RegularExpressionToNFAConversion(
					(RegularExpression) formalDef);
			toFSA.stepToCompletion();
			convert = new FSAtoRegGrammarConversion(
					toFSA.getConvertedDefinition());
		}

		else
			return null;
		convert.stepToCompletion();
		return (Grammar) convert.getConvertedDefinition();
	}
}

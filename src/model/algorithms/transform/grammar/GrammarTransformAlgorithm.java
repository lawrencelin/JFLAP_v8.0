package model.algorithms.transform.grammar;

import java.util.ArrayList;
import java.util.List;

import debug.JFLAPDebug;

import errors.BooleanWrapper;
import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.transform.FormalDefinitionTransformAlgorithm;
import model.formaldef.FormalDefinition;
import model.grammar.Grammar;
import model.grammar.typetest.GrammarType;

public abstract class GrammarTransformAlgorithm extends FormalDefinitionTransformAlgorithm<Grammar> {



	public GrammarTransformAlgorithm(Grammar g){
		super(g);
	}

	public Grammar getOriginalGrammar(){
		return super.getOriginalDefinition();
	}
	
	public Grammar getTransformedGrammar(){
		return this.getTransformedDefinition();
	}
	
	@Override
	public BooleanWrapper[] checkOfProperForm(Grammar g) {
		List<BooleanWrapper> bw = new ArrayList<BooleanWrapper>();
		if (!g.isType(GrammarType.CONTEXT_FREE))
			bw.add(new BooleanWrapper(false,"The grammar must be restriced on the " +
					"left hand side."));
		return bw.toArray(new BooleanWrapper[0]);
	}

}

package model.algorithms.testinput.simulate.configurations;

import java.util.AbstractCollection;

import model.algorithms.testinput.simulate.SingleInputSimulator;
import model.automata.State;
import model.automata.acceptors.pda.PDATransition;
import model.automata.acceptors.pda.PushdownAutomaton;
import model.symbols.Symbol;
import model.symbols.SymbolString;

public class PDAConfiguration extends
		SingleSecondaryConfiguration<PushdownAutomaton, PDATransition> {

	public PDAConfiguration(PushdownAutomaton a, State s, int pos, SymbolString input,
			SymbolString stack) {
		super(a, s, pos, input, stack);
	}

	@Override
	public String getName() {
		return "PDA Configuration";
	}

	public SymbolString getStack(){
		return this.getStringForIndex(0);
	}

	private boolean canPop(Symbol[] symbols) {
		return this.getStack().startsWith(symbols);
	}

	@Override
	protected boolean canMoveAlongTransition(
			PDATransition trans) {
		return super.canMoveAlongTransition(trans) && this.canPop(trans.getPop());
	}

	@Override
	protected boolean isInFinalState() {
		if (this.getSpecialCase() == SingleInputSimulator.DEFAULT)
			return super.isInFinalState();
		return this.getStack().isEmpty();
	}

	

	@Override
	protected boolean shouldFindValidTransitions() {
		return true;
	}

	@Override
	protected String getSecondaryName() {
		return "Stack";
	}

	@Override
	protected int getPositionChange(PDATransition trans) {
		return trans.getPush().length-trans.getPop().length;
	}

	@Override
	protected SymbolString createUpdatedSecondary(SymbolString clone,
			PDATransition trans) {
		SymbolString stack = clone;
		Symbol[] pop = trans.getPop();
		SymbolString push = new SymbolString(trans.getPush());
		return push.concat(stack.subList(pop.length));
	}

}

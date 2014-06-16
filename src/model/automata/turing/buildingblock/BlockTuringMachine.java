package model.automata.turing.buildingblock;

import universe.preferences.JFLAPPreferences;
import model.automata.InputAlphabet;
import model.automata.StartState;
import model.automata.State;
import model.automata.TransitionSet;
import model.automata.acceptors.FinalStateSet;
import model.automata.turing.BlankSymbol;
import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.formaldef.FormalDefinition;

public class BlockTuringMachine extends TuringMachine<BlockTransition> {

	public BlockTuringMachine(BlockSet states, 
								TapeAlphabet tapeAlph,
								BlankSymbol blank, 
								InputAlphabet inputAlph,
								TransitionSet<BlockTransition> functions, 
								StartState start,
								FinalStateSet finalStates) {
		super(states, tapeAlph, blank, inputAlph, functions, start, finalStates);
	}
	
	public BlockTuringMachine(){
		super(new BlockSet(),
				new TapeAlphabet(),
				new BlankSymbol(),
				new InputAlphabet(),
				new TransitionSet<BlockTransition>(),
				new StartState(),
				new FinalStateSet());
	}

	@Override
	public BlockSet getStates(){
		return (BlockSet) super.getStates();
	}

	@Override
	public String getDescriptionName() {
		return "Block Turing Machine";
	}

	@Override
	public String getDescription() {
		return "Turing machine utilizing building blocks";
	}
	
	@Override
	public void setStartState(State s) {
		super.setStartState((Block)s);
	}

	@Override
	public Block getStartState() {
		return (Block) super.getStartState();
	}
	
	@Override
	public BlockTuringMachine copy() {
		State start = getStartState();
		StartState newStart = new StartState(start == null ? null : start.copy());
		return new BlockTuringMachine(this.getStates().copy(),
				this.getTapeAlphabet().copy(),
				new BlankSymbol(),
				this.getInputAlphabet().copy(), 
				this.getTransitions().copy(), 
				newStart, 
				this.getFinalStateSet().copy());
	}
	
	
	@Override
	public FormalDefinition alphabetAloneCopy() {
		return new BlockTuringMachine(new BlockSet(),
				this.getTapeAlphabet(), 
				new BlankSymbol(),
				this.getInputAlphabet(), 
				new TransitionSet<BlockTransition>(), 
				new StartState(), 
				new FinalStateSet());
	}

	@Override
	public BlockTransition createBlankTransition(State from, State to) {
		return new BlockTransition((Block) from, (Block) to, JFLAPPreferences.getTMBlankSymbol());
	}

}

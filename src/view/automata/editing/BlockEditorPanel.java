package view.automata.editing;

import java.awt.geom.Point2D;

import model.automata.State;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachine;
import model.automata.turing.buildingblock.Block;
import model.automata.turing.buildingblock.BlockSet;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.graph.BlockTMGraph;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;

public class BlockEditorPanel extends
		AutomatonEditorPanel<BlockTuringMachine, BlockTransition> {

	public BlockEditorPanel(BlockTuringMachine m, UndoKeeper keeper,
			boolean editable) {
		super(m, keeper, editable);
		setGraph(new BlockTMGraph(m));
	}
	
	public Block addBlock(Block b, Point2D p){
		BlockSet blocks = getAutomaton().getStates();
		if(blocks.getStateWithID(b.getID()) != null)
			b = b.copy(blocks.getNextUnusedID());
		blocks.add(b);
		moveState(b, p);
		
		TuringMachine machine = b.getTuringMachine();
		TransitionGraph graph = machine instanceof BlockTuringMachine ? new BlockTMGraph((BlockTuringMachine) machine) : 
			new TransitionGraph<MultiTapeTMTransition>((MultiTapeTuringMachine) machine);
		setGraph(machine, graph);
		return b;
	}
	
	public TransitionGraph getGraph(Block b){
		return ((BlockTMGraph) getGraph()).getGraph(b);
	}


	public void setGraph(Block block, TransitionGraph graph) {
		((BlockTMGraph) getGraph()).setGraph(block, graph);
	}
	
	public void setGraph(TuringMachine machine, TransitionGraph graph){
		for(State s : getAutomaton().getStates()){
			Block b = (Block) s;
			if(b.getTuringMachine().equals(machine)){
				setGraph(b, graph);
			}
		}
	}
}

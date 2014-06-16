package view.automata.views;

import java.awt.Component;

import javax.swing.JComponent;

import debug.JFLAPDebug;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.BlockEditorPanel;
import view.automata.tools.BlockStateTool;
import view.automata.tools.StateTool;
import model.automata.turing.buildingblock.BlockTransition;
import model.automata.turing.buildingblock.BlockTuringMachine;
import model.undo.UndoKeeper;

public class BlockTMView extends TuringMachineView<BlockTuringMachine, BlockTransition>{

	public BlockTMView(BlockTuringMachine model) {
		super(model);
	}
	
	@Override
	public BlockEditorPanel getCentralPanel() {
		return (BlockEditorPanel) super.getCentralPanel();
	}
	
	@Override
	public JComponent createCentralPanel(BlockTuringMachine model,
			UndoKeeper keeper, boolean editable) {
		// TODO Auto-generated method stub
		return new BlockEditorPanel(model, keeper, editable);
	}

	
	@Override
	public StateTool<BlockTuringMachine, BlockTransition> createStateTool(
			AutomatonEditorPanel<BlockTuringMachine, BlockTransition> panel,
			BlockTuringMachine def) {
		// TODO Auto-generated method stub
		return new BlockStateTool((BlockEditorPanel) panel, def);
	}
}

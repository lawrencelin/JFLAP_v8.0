package view.automata.views;

import javax.swing.JComponent;

import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.moore.MooreMachine;
import model.automata.transducers.moore.MooreOutputFunction;
import model.undo.UndoKeeper;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.MooreEditorPanel;
import view.automata.tools.ArrowTool;
import view.automata.tools.MooreArrowTool;
import view.automata.tools.MooreStateTool;
import view.automata.tools.StateTool;

public class MooreView extends TransducerView<MooreMachine, MooreOutputFunction>{

	public MooreView(MooreMachine model) {
		super(model);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ArrowTool<MooreMachine, FSATransition> createArrowTool(
			AutomatonEditorPanel<MooreMachine, FSATransition> panel,
			MooreMachine def) {
		return new MooreArrowTool((MooreEditorPanel) panel, def);
	}
	
	@Override
	public StateTool<MooreMachine, FSATransition> createStateTool(
			AutomatonEditorPanel<MooreMachine, FSATransition> panel,
			MooreMachine def) {
		return new MooreStateTool((MooreEditorPanel) panel, def);
	}
	
	@Override
	public JComponent createCentralPanel(MooreMachine model, UndoKeeper keeper,
			boolean editable) {
		// TODO Auto-generated method stub
		return new MooreEditorPanel(model, keeper, editable);
	}

}

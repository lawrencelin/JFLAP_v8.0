package view.automata.views;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import file.xml.graph.AutomatonEditorData;
import model.automata.Automaton;
import model.automata.Transition;
import model.graph.TransitionGraph;
import model.undo.UndoKeeper;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ArrowTool;
import view.automata.tools.DeleteTool;
import view.automata.tools.StateTool;
import view.automata.tools.ToolBar;
import view.automata.tools.TransitionTool;
import view.automata.undoing.AutomataRedoAction;
import view.automata.undoing.AutomataUndoAction;
import view.formaldef.BasicFormalDefinitionView;
import view.undoing.redo.RedoAction;
import view.undoing.redo.RedoButton;
import view.undoing.undo.UndoButton;

public class AutomatonView<T extends Automaton<S>, S extends Transition<S>>
		extends BasicFormalDefinitionView<T> {
	private static final Dimension AUTOMATON_SIZE = new Dimension(500, 600);

	public AutomatonView(T model) {
		this(model, new UndoKeeper(), true);
	}
	
	public AutomatonView(T model, UndoKeeper keeper, boolean editable) {
		super(model, keeper, editable);
		setPreferredSize(AUTOMATON_SIZE);
		
		JScrollPane pane = getScroller();
		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);		
		pane.revalidate();
		repaint();
	}

	@Override
	public JComponent createCentralPanel(T model, UndoKeeper keeper,
			boolean editable) {
		return new AutomatonEditorPanel<T, S>(model, keeper, editable);
	}

	@Override
	public String getName() {
		return "Automaton Editor";
	}

	@Override
	public void repaint() {
		super.repaint();
	}
	
	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		repaint();
	}

	@Override
	public Component createToolbar(T definition, UndoKeeper keeper) {
		AutomatonEditorPanel<T, S> panel = (AutomatonEditorPanel<T, S>) getCentralPanel();

		ArrowTool<T, S> arrow = createArrowTool(panel, definition);
		StateTool<T, S> state = createStateTool(panel, definition);
		TransitionTool<T, S> trans = new TransitionTool<T, S>(panel);
		DeleteTool<T, S> delete = new DeleteTool<T, S>(panel);

		panel.setTool(arrow);
		ToolBar bar = new ToolBar(arrow, state, trans, delete);
		bar.addToolListener(panel);
		
		AutomataUndoAction undo = new AutomataUndoAction(keeper, panel);
		RedoAction redo = new AutomataRedoAction(keeper, panel);
		
		bar.add(new UndoButton(undo, true));
		bar.add(new RedoButton(redo, true));
		return bar;
	}
	
	public ArrowTool<T, S> createArrowTool (AutomatonEditorPanel<T, S> panel, T def){
		return new ArrowTool<T, S>(panel, def);
	}
	
	public StateTool<T, S> createStateTool(AutomatonEditorPanel<T, S> panel, T def){
		return new StateTool<T, S>(panel, def);
	}
	
	@Override
	public T getDefinition() {
		// TODO Auto-generated method stub
		return super.getDefinition();
	}
	
	public AutomatonEditorData<T, S> createData() {
		return new AutomatonEditorData<T, S>((AutomatonEditorPanel<T, S>) getCentralPanel());
	}

}

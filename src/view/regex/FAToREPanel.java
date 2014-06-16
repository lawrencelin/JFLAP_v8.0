package view.regex;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import model.algorithms.conversion.fatoregex.DFAtoRegularExpressionConverter;
import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.FAtoREStateTool;
import view.automata.tools.algorithm.FAtoRETransitionTool;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.tools.algorithm.StateCollapseTool;
import view.automata.tools.algorithm.TransitionCollapseTool;

public class FAToREPanel extends AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition>{

	private DFAtoRegularExpressionConverter myAlg;
	private JLabel myMainLabel;
	private JLabel myDetailLabel;
	private FAToREController myController;
	
	public FAToREPanel(AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor, DFAtoRegularExpressionConverter convert) {
		super(editor, editor.getAutomaton(), "Convert FA to RE");
		myAlg = convert;
		updateSize();
		initView();
	}

	private void initView() {
		JPanel labels = new JPanel(new BorderLayout());
		myMainLabel = new JLabel(" ");
		myDetailLabel = new JLabel(" ");
		myController = new FAToREController(this);
		
		labels.add(myMainLabel, BorderLayout.NORTH);
		labels.add(myDetailLabel,  BorderLayout.SOUTH);
		add(labels, BorderLayout.NORTH);
		
		ToolBar tools = createToolBar();
		JScrollPane scroll = new JScrollPane(getEditorPanel());
		scroll.revalidate();
		JPanel center = new JPanel(new BorderLayout());
		
		center.add(tools, BorderLayout.NORTH);
		center.add(scroll, BorderLayout.CENTER);
		add(center, BorderLayout.CENTER);
		
		Dimension size = getPreferredSize();
		int height = size.height;
		height += tools.getPreferredSize().height;
		height += labels.getPreferredSize().height;
		
		setPreferredSize(new Dimension(size.width, height));
	}

	private ToolBar createToolBar() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		
		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(panel, panel.getAutomaton());
		FAtoREStateTool state = new FAtoREStateTool(panel, myController);
		FAtoRETransitionTool trans = new FAtoRETransitionTool(panel, myController);
		TransitionCollapseTool tCollapse = new TransitionCollapseTool(panel, myController);
		StateCollapseTool sCollapse = new StateCollapseTool(panel, myController);
		
		ToolBar tools = new ToolBar(arrow, state, trans, tCollapse, sCollapse);
		tools.addToolListener(panel);
		panel.setTool(arrow);
		
		tools.addSeparator();
		tools.add(new AbstractAction("Step") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myController.step();
			}
		});
		
		tools.add(new AbstractAction("Step to Completion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				myController.step();
				
				if(myAlg.hasSingleFinal())
					while(myAlg.canStep())
						myController.step();
			}
		});
		tools.add(new JButton(new AbstractAction("Export") {
			public void actionPerformed(ActionEvent e) {
				myController.export();
			}
		}));
		return tools;
	}

	public DFAtoRegularExpressionConverter getAlgorithm() {
		return myAlg;
	}

	public void clearSelection() {
		getEditorPanel().clearSelection();
	}

	public void setMainText(String string) {
		myMainLabel.setText(string);
	}
	
	public void setDetailText(String string) {
		myDetailLabel.setText(string);
	}



	public void deselect(State from) {
		getEditorPanel().deselectObject(from);
	}



	public void selectAll(Object... objs) {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		panel.clearSelection();
		panel.selectAll(Arrays.asList(objs));
	}

}

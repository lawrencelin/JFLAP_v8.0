package view.algorithms.conversion.regextofa;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import file.xml.graph.AutomatonEditorData;
import model.algorithms.conversion.regextofa.RegularExpressionToNFAConversion;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.acceptors.fsa.FiniteStateAcceptor;
import universe.JFLAPUniverse;
import util.Point2DAdv;
import view.ViewFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.simulate.TooltipAction;
import view.automata.tools.ToolBar;
import view.automata.tools.algorithm.DeexpressionTransitionTool;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.tools.algorithm.REtoFATransitionTool;
import view.environment.JFLAPEnvironment;

public class RegularExpressionToFAPanel extends AutomatonDisplayPanel<FiniteStateAcceptor, FSATransition>{

	private RegularExpressionToNFAConversion myAlg;
	private JLabel mainLabel;
	private JLabel detailLabel;

	public RegularExpressionToFAPanel(
			AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> editor,
			RegularExpressionToNFAConversion convert) {
		super(editor, editor.getAutomaton(), "Convert RE to NFA");
		myAlg = convert;
		updateSize();
		initView();
	}

	private void initView() {
		JPanel labels = new JPanel(new BorderLayout());
		mainLabel = new JLabel(" ");
		detailLabel = new JLabel(" ");
		labels.add(mainLabel, BorderLayout.NORTH);
		labels.add(detailLabel, BorderLayout.SOUTH);

		add(labels, BorderLayout.NORTH);
		
		ToolBar tools = createToolbar();
		JScrollPane scroll = new JScrollPane(getEditorPanel());
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(tools, BorderLayout.NORTH);
		panel.add(scroll, BorderLayout.CENTER);
		
		add(panel, BorderLayout.CENTER);
	}

	private ToolBar createToolbar() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FiniteStateAcceptor fsa = panel.getAutomaton();
		
		NonTransitionArrowTool<FiniteStateAcceptor, FSATransition> arrow = new NonTransitionArrowTool<FiniteStateAcceptor, FSATransition>(panel, fsa );
		REtoFATransitionTool trans = new REtoFATransitionTool(panel, myAlg);
		DeexpressionTransitionTool deex = new DeexpressionTransitionTool(this);
		
		ToolBar tools = new ToolBar(arrow, trans, deex);
		tools.addToolListener(panel);
		panel.setTool(arrow);
		
		tools.addSeparator();
		tools.add(new AbstractAction("Step") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(myAlg.canStep()){
					step();
				}
			}
		});
		
		tools.add(new AbstractAction("Step to Completion") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				while(myAlg.canStep())
					step();
			}
		});
		
		tools.add(new AbstractAction("Export") {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				export();
			}
		});
		return tools;
	}

	private void export() {
		JFLAPEnvironment env = JFLAPUniverse.getActiveEnvironment();
		if(myAlg.isRunning())
			JOptionPane.showMessageDialog(env, "The conversion is not completed yet!");
		else{
			AutomatonEditorData<FiniteStateAcceptor, FSATransition> data = new AutomatonEditorData<FiniteStateAcceptor, FSATransition>(getEditorPanel());
			
			JFLAPUniverse.registerEnvironment(ViewFactory.createAutomataView(data));
		}
	}
	
	public void replaceTransition(FSATransition transition,
			Set<FSATransition> added) {
		// Compose the transform.
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		
		AffineTransform at = new AffineTransform();
		Point2D pStart = panel.getPointForVertex(transition.getFromState());
		Point2D pEnd = panel.getPointForVertex(transition.getToState());
		at.translate(pStart.getX(), pStart.getY());
		at.scale(pStart.distance(pEnd), pStart.distance(pEnd));
		at.rotate(Math.atan2(pEnd.getY() - pStart.getY(), pEnd.getX() - pStart.getX()));

		Point2D.Double ps = new Point2D.Double(0.2, 0.0);
		Point2D.Double pe = new Point2D.Double(0.8, 0.0);

		int i = 0;
		for (FSATransition trans : added) {
			pStart = new Point();
			pEnd = new Point();
			double y = added.size() > 1 ? ((double) i
					/ ((double) added.size() - 1.0) - 0.5) * 0.5 : 0.0;
			pe.y = ps.y = y;
			at.transform(ps, pStart);
			at.transform(pe, pEnd);
			// Clamp bounds.
			pStart = new Point2DAdv(Math.max(pStart.getX(), 20), Math.max(pStart.getY(), 20));
			pEnd = new Point2DAdv(Math.max(pEnd.getX(), 20), Math.max(pEnd.getY(), 20));

			panel.moveState(trans.getFromState(), pStart);
			panel.moveState(trans.getToState(), pEnd);
			
			panel.moveCtrlPoint(trans.getFromState(), trans.getToState(), panel.getGraph().getDefaultControlPoint(trans.getFromState(), trans.getToState()));

			i++;
		}

	}

	public void beginDeExpressionify(FSATransition o) {
		myAlg.beginDeExpressionify(o);
	}

	private void step() {
		AutomatonEditorPanel<FiniteStateAcceptor, FSATransition> panel = getEditorPanel();
		FiniteStateAcceptor fsa = panel.getAutomaton();
		Set<FSATransition> existingTransitions = fsa.getTransitions().toCopiedSet();

		FSATransition trans = myAlg.getExpressionTransitions().get(0);
		myAlg.beginDeExpressionify(trans);
		
		Set<FSATransition> addedT = fsa.getTransitions().toCopiedSet();
		addedT.removeAll(existingTransitions);
		
		replaceTransition(trans, addedT);
		myAlg.addAllRemainingLambdaTransitions();
	}

}

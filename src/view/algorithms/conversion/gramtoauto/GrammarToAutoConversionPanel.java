package view.algorithms.conversion.gramtoauto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import debug.JFLAPDebug;
import model.algorithms.conversion.gramtoauto.GrammarToAutomatonConverter;
import model.automata.Automaton;
import model.automata.SingleInputTransition;
import model.change.events.AddEvent;
import model.grammar.Grammar;
import model.grammar.Production;
import universe.JFLAPUniverse;
import util.JFLAPConstants;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.SizeSlider;
import view.ViewFactory;
import view.automata.AutomatonDisplayPanel;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.tools.ToolBar;
import view.automata.tools.TransitionTool;
import view.automata.tools.algorithm.NonTransitionArrowTool;
import view.automata.views.AutomatonView;
import file.xml.graph.AutomatonEditorData;

public class GrammarToAutoConversionPanel<T extends Automaton<S>, S extends SingleInputTransition<S>>
		extends AutomatonDisplayPanel<T, S> {

	private GrammarToAutomatonConverter<T, S> myAlg;
	private GrammarConversionTable myTable;

	public GrammarToAutoConversionPanel(
			GrammarToAutomatonConverter<T, S> convert,
			AutomatonEditorPanel<T, S> editor, String name) {
		super(editor, editor.getAutomaton(), name);
		myAlg = convert;

		updateSize();
		initView();
	}

	private void initView() {
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		Grammar g = myAlg.getOriginalDefinition();

		myTable = new GrammarConversionTable(g);
		MagnifiableScrollPane tScroll = new MagnifiableScrollPane(myTable);
		tScroll.setMinimumSize(myTable.getMinimumSize());

		SizeSlider slider = new SizeSlider(tScroll);
		slider.distributeMagnification();
		add(slider, BorderLayout.SOUTH);

		ToolBar tools = initToolbar();

		MagnifiablePanel right = new MagnifiablePanel(new BorderLayout());
		MagnifiableScrollPane aScroll = new MagnifiableScrollPane(panel);

		right.add(tools, BorderLayout.NORTH);
		right.add(aScroll);

		initListeners();
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tScroll,
				right);
		add(split, BorderLayout.CENTER);

		Dimension size = getPreferredSize(), tSize = tScroll.getMinimumSize(), sSize = slider
				.getPreferredSize(), toSize = tools.getPreferredSize();
		int width = size.width - tSize.width - split.getDividerSize() - JFLAPConstants.STATE_RADIUS - 5;
		int height = size.height - sSize.height - toSize.height - PADDING;
		panel.resizeGraph(new Rectangle(width, height));
	}

	private ToolBar initToolbar() {
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		T auto = panel.getAutomaton();

		NonTransitionArrowTool<T, S> arrow = new NonTransitionArrowTool<T, S>(
				panel, auto);
		TransitionTool<T, S> trans = new TransitionTool<T, S>(panel);
		ToolBar tools = new ToolBar(arrow, trans);
		tools.addToolListener(panel);

		tools.addSeparator();
		tools.add(new AbstractAction("Step to Completion") {

			@Override
			public void actionPerformed(ActionEvent e) {
				myTable.clearSelection();
				getEditorPanel().clearSelection();
				myAlg.stepToCompletion();
			}
		});

		tools.add(new AbstractAction("Create Selected") {

			@Override
			public void actionPerformed(ActionEvent e) {
				AutomatonEditorPanel<T, S> panel = getEditorPanel();
				panel.clearSelection();

				Production[] prods = myTable.getSelected();
				for (Production p : prods) {
					if (myAlg.getUnconvertedProductions().contains(p)) {
						myAlg.convertAndAddProduction(p);
						panel.selectObject(myAlg.convertProduction(p));
					}
				}
			}
		});

		tools.add(new AbstractAction("Done?") {

			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Production> unconverted = myAlg.getUnconvertedProductions();
				int size = unconverted.size();

				String message = unconverted.isEmpty() ? "The conversion is finished!"
						: size + " more transition" + (size == 1 ? "" : "s")
								+ " must be added.";
				JOptionPane.showMessageDialog(
						JFLAPUniverse.getActiveEnvironment(), message);
			}
		});

		tools.add(new AbstractAction("Export") {

			@Override
			public void actionPerformed(ActionEvent e) {
				boolean done = myAlg.allProductionsConverted();
				if (!done) {
					JOptionPane.showMessageDialog(
							JFLAPUniverse.getActiveEnvironment(),
							"The conversion is not completed yet!");
					return;
				}
				AutomatonView<T, S> view = ViewFactory
						.createAutomataView(new AutomatonEditorData<T, S>(
								getEditorPanel()));
				JFLAPUniverse.registerEnvironment(view);
			}
		});
		panel.setTool(arrow);
		return tools;
	}

	private void initListeners() {
		AutomatonEditorPanel<T, S> panel = getEditorPanel();
		T auto = panel.getAutomaton();

		auto.getTransitions().addListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (e instanceof AddEvent) {
					Collection<S> transitions = ((AddEvent) e).getToAdd();

					for (S trans : transitions) {
						Production converted = null;

						for (Production p : myAlg.getUnconvertedProductions())
							if (myAlg.convertProduction(p).equals(trans))
								converted = p;
						if (converted == null) {
							myAlg.getConvertedAutomaton().getTransitions()
									.remove(trans);
							JOptionPane.showMessageDialog(
									JFLAPUniverse.getActiveEnvironment(),
									"That transition is not correct");
						} else {
							myAlg.addConvertedProduction(converted);
							myTable.setChecked(converted, true);
						}
					}
				}
			}
		});

		myTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent e) {
						ListSelectionModel model = myTable.getSelectionModel();
						AutomatonEditorPanel<T, S> panel = getEditorPanel();
						panel.clearSelection();

						int min = model.getMinSelectionIndex(), max = model
								.getMaxSelectionIndex();
						if (min < 0)
							return;
						for (int i = min; i <= max; i++)
							if (model.isSelectedIndex(i)) {
								Production p = (Production) myTable.getValueAt(
										i, 0);
								if (myAlg.getConvertedProductions().contains(p))
									panel.selectObject(myAlg
											.convertProduction(p));
							}
					}
				});
	}

}

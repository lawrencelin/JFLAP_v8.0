package view.grammar.parsing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Grammar;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableToolbar;
import view.algorithms.InputtingPanel;
import view.formaldef.FormalDefinitionView;
import view.grammar.productions.ProductionTable;

public abstract class ParserView<S extends Parser> 
							extends FormalDefinitionView<S, Grammar> 
							implements ChangeListener{

	public static final String SET_INPUT = "Input a string and click \"Set\" or press Enter.";
	private S myAlgorithm;
	private MagnifiableLabel myStatusLabel;

	public ParserView(S alg) {
		super(alg, new UndoKeeper(), false);
		myAlgorithm = alg;
		alg.addListener(this);
	}

	@Override
	public JComponent createCentralPanel(S alg, UndoKeeper keeper,
			boolean editable) {
		Component parseView = createParseView(alg);
		Component prodView = createProductionView(alg);
		InputtingPanel input = new InputtingPanel(alg, false);
		MagnifiableToolbar toolbar = createToolbar(alg);
		myStatusLabel = new MagnifiableLabel(JFLAPPreferences.getDefaultTextSize());
		
		MagnifiableSplitPane split = 
				new MagnifiableSplitPane(JSplitPane.HORIZONTAL_SPLIT,
											prodView, 
											parseView);
		
		MagnifiablePanel p1 = new MagnifiablePanel();
		p1.setLayout(new GridLayout(0,1));
		p1.add(input);
		p1.add(toolbar);
		p1.add(myStatusLabel);
		setStatus(SET_INPUT);
		
		MagnifiablePanel p2 = new MagnifiablePanel();
		p2.setLayout(new BorderLayout());
		p2.add(p1, BorderLayout.NORTH);
		p2.add(split, BorderLayout.CENTER);
		return p2;
	}

	public abstract MagnifiableToolbar createToolbar(S alg);

	public abstract Component createParseView(S alg);

	public Component createProductionView(S alg) {
		return new ProductionTable(alg.getGrammar(), null,false);
	}

	@Override
	public String getName() {
		return myAlgorithm.getDescriptionName();
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof AdvancedChangeEvent &&
				((AdvancedChangeEvent)e).comesFrom(myAlgorithm))
			updateStatus((AdvancedChangeEvent)e);
		distributeMagnifiation();
		this.repaint();
	}

	public abstract void updateStatus(AdvancedChangeEvent e);
	
	public S getAlgorithm(){
		return myAlgorithm;
	}
	
	@Override
	public Grammar getDefinition() {
		return getModel().getGrammar();
	}
	
	/**
	 * Updates the status label to the specified text.
	 */
	public void setStatus(String statusText){
		myStatusLabel.setText(statusText);
	}
	
}
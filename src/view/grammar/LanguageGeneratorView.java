package view.grammar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.grammar.Grammar;
import model.languages.LanguageGenerator;
import model.symbols.SymbolString;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableList;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;
import view.formaldef.BasicFormalDefinitionView;
import view.grammar.productions.ProductionTable;

/**
 * View for language generation. Current design only holds for Grammar views,
 * but should be expanded to be capable to hold anything that is equivalent to a
 * grammar.
 * 
 * @author Ian McMahon
 * 
 */

public class LanguageGeneratorView extends BasicFormalDefinitionView<Grammar> {

	private static final int RECOMMENDED_LIMIT = 1000;

	private LanguageGenerator myGenerator;
	private MagnifiableList myList;

	public LanguageGeneratorView(Grammar g) {
		super(g, new UndoKeeper(), false);
		setPreferredSize(new Dimension(550, 600));
		myGenerator = LanguageGenerator.createGenerator(g);
	}

	@Override
	public JComponent createCentralPanel(Grammar model, UndoKeeper keeper,
			boolean editable) {
		// When expanded upon, prodView will need to change. No need to show a
		// production table for FSAs, regexs, etc.
		Component prodView = new ProductionTable(getDefinition(), getKeeper(),
				false);
		Component langView = new MagnifiableScrollPane(
				myList = new MagnifiableList(new DefaultListModel(),
						JFLAPPreferences.getDefaultTextSize()));
		MagnifiableSplitPane split = new MagnifiableSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, prodView, langView);

		LanguageInputtingPanel inputPanel = new LanguageInputtingPanel();

		MagnifiablePanel panel = new MagnifiablePanel(new BorderLayout());
		panel.add(inputPanel, BorderLayout.NORTH);
		panel.add(split, BorderLayout.CENTER);

		return panel;
	}

	@Override
	public String getName() {
		return "Language Generator";
	}

	private class LanguageInputtingPanel extends MagnifiableToolbar {

		private MagnifiableLabel myLabel;
		private MagnifiableTextField myTextField;
		private MagnifiableButton myLengthButton;
		private MagnifiableButton myGenerateButton;

		public LanguageInputtingPanel() {
			setFloatable(false);
			int size = JFLAPPreferences.getDefaultTextSize();

			myLabel = new MagnifiableLabel("Generate: ", size);
			myTextField = new MagnifiableTextField(size);
			myGenerateButton = new MagnifiableButton("# of Strings", size);
			myLengthButton = new MagnifiableButton("String Length", size);

			this.add(myLabel);
			this.add(myTextField);
			this.add(myGenerateButton);
			this.add(myLengthButton);

			myList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			setupInteractions();
		}

		private void setupInteractions() {
			myGenerateButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int numToGen = getNumberToGenerate();
					if (verifyNumberToGenerate(numToGen))
						setList(myGenerator.getStrings(numToGen));
				}

			});

			myLengthButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					int numToGen = getNumberToGenerate();
					int terminalSize = myGenerator.getGrammar().getTerminals()
							.size();
					if (verifyNumberToGenerate((int) Math.pow(terminalSize,
							numToGen)))

						setList(myGenerator.getStringsOfLength((numToGen)));
				}
			});

		}

		private int getNumberToGenerate() {
			String input = myTextField.getText();

			try {
				int numToGen = Integer.parseInt(input);
				return numToGen;
			} catch (Exception e) {
				throw new AlgorithmException(
						"The entered string is not a numeric value!");
			}
		}

	}

	private boolean verifyNumberToGenerate(int numberToGenerate) {
		int n = JOptionPane.YES_OPTION;
		int terminalSize = myGenerator.getGrammar().getTerminals().size();

		if (numberToGenerate > RECOMMENDED_LIMIT) {
			n = JOptionPane
					.showConfirmDialog(
							this,
							"Warning: Generation of this size may cause JFLAP to slow down drastically or freeze."
									+ "\nWould you like to continue?",
							"Generation Warning", JOptionPane.YES_NO_OPTION,
							JOptionPane.WARNING_MESSAGE);
		}
		return n == JOptionPane.YES_OPTION;
	}

	private void setList(List<SymbolString> generatedStrings) {
		SymbolString[] array = generatedStrings.toArray(new SymbolString[0]);
		DefaultListModel model = (DefaultListModel) myList.getModel();
		model.clear();
		for (SymbolString string : generatedStrings) {
			model.addElement(string);
		}
		repaint();
	}

}

package view.regex;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;
import model.automata.InputAlphabet;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.components.ChangeTypes;
import model.regex.RegularExpression;
import model.regex.RegularExpressionException;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableTextField;
import view.EditingPanel;
import errors.BooleanWrapper;

public class RegexPanel extends EditingPanel {

	private RegularExpression myExpression;
	private MagnifiableTextField myField;

	public RegexPanel(RegularExpression regex, UndoKeeper keeper,
			boolean editable) {
		super(keeper, editable);
		setLayout(new BorderLayout());

		myExpression = regex;
		myField = new MagnifiableTextField(
				JFLAPPreferences.getDefaultTextSize());
		SymbolString symbols = myExpression.getExpression();
		myField.setText(symbols == null ? "" : symbols.toString());
		initView();
	}

	private void initView() {
		MagnifiablePanel regexPanel = new MagnifiablePanel(new BorderLayout());
		regexPanel.add(
				new MagnifiableLabel("Expression: ", JFLAPPreferences
						.getDefaultTextSize()), BorderLayout.WEST);
		regexPanel.add(myField, BorderLayout.CENTER);

		add(regexPanel, BorderLayout.CENTER);
		add(new MagnifiableLabel("Edit the regular expression above. "
				+ JFLAPPreferences.getEmptySubLiteral()
				+ " is the empty string sub.", JFLAPPreferences.getDefaultTextSize()), BorderLayout.SOUTH);
		initListener();
	}
	
	

	private void initListener() {
		myField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				update();
			}
		});
		
		myExpression.addListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof InputAlphabet){
//					if(((AdvancedChangeEvent) e).getType() == ChangeTypes.ITEM_MODIFIED){
						myField.setText(myExpression.getExpressionString());
//					}
				}
			}
		});
	}

	private void update() {
		String text = myField.getText();
		text = text.trim();
		text = text.replaceAll("\\s+", " ");

		SymbolString current = myExpression.getExpression();
		SymbolString symbols = Symbolizers.symbolize(text, myExpression);

		BooleanWrapper format = myExpression.correctFormat(symbols);
		if (format.isError()) {
			myField.setText(current == null ? null : current.toString());
			throw new RegularExpressionException(format.getMessage());
		}

		if (current == null) {
			myField.setText(symbols.toString());
			myExpression.setTo(symbols);
		} else if (!current.equals(symbols))
			getKeeper().applyAndListen(new RegexSetToEvent(symbols));

	}

	private class RegexSetToEvent implements IUndoRedo {

		private SymbolString myOldString;
		private SymbolString myString;

		public RegexSetToEvent(SymbolString stringTo) {
			myOldString = myExpression.getExpression();
			myString = stringTo;
		}

		@Override
		public boolean undo() {
			if (myExpression.setTo(myOldString)) {
				myField.setText(myOldString.toString());
				return true;
			}
			return false;
		}

		@Override
		public boolean redo() {
			if (myExpression.setTo(myString)) {
				myField.setText(myString.toString());
				return true;
			}
			return false;
		}

		@Override
		public String getName() {
			return "Regex Set to Event";
		}
	}
}

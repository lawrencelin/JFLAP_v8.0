package view.algorithms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import model.algorithms.AlgorithmException;
import model.algorithms.testinput.InputUsingAlgorithm;
import model.formaldef.FormalDefinition;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.SymbolString;
import model.symbols.symbolizer.Symbolizers;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;

public class InputtingPanel extends MagnifiableToolbar {

	private InputUsingAlgorithm myAlgorithm;
	private MagnifiableLabel myLabel;
	private MagnifiableTextField myTextField;
	private MagnifiableButton mySetButton;
	private MagnifiableButton myChangeButton;

	public InputtingPanel(InputUsingAlgorithm alg, boolean floatable){
		this.setFloatable(floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		
		myAlgorithm = alg;
		myLabel = new MagnifiableLabel("Input: ", size);
		myTextField = new MagnifiableTextField(size);
		mySetButton = new MagnifiableButton("Set", size);
		myChangeButton = new MagnifiableButton("Change", size);
		
		this.add(myLabel);
		this.add(myTextField);
		this.add(mySetButton);
		this.add(myChangeButton);
		setupInteractions();
	}

	private void setupInteractions() {
		ActionListener set = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				doSetAction();
			}
		};
		mySetButton.addActionListener(set);
		myTextField.addActionListener(set);
		
		myChangeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				doChangeAction();
			}
		});
	}

	private void doChangeAction() {
		myAlgorithm.reset();
		myTextField.setEditable(true);
		mySetButton.setEnabled(true);
		myChangeButton.setEnabled(false);
	}

	private void doSetAction() {
		FormalDefinition def = myAlgorithm.getOriginalDefinition();
		SymbolString s = Symbolizers.symbolize(myTextField.getText(),def);
		Alphabet langAlph = def.getLanguageAlphabet();
		
		boolean contains = langAlph.containsAll(s);
		if (!contains)
			throw new AlgorithmException("The input " + s + " contains symbols not in the " + 
											langAlph.getDescriptionName());
		
		myAlgorithm.setInput(s);
		mySetButton.setEnabled(false);
		myTextField.setEditable(false);
		myChangeButton.setEnabled(true);
	}
	
}

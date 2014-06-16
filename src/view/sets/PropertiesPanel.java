package view.sets;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.elements.Element;
import model.sets.num.MultiplesSet;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;

public class PropertiesPanel extends MagnifiablePanel {
	
	private AbstractSet mySet;
	
	public PropertiesPanel (AbstractSet set) {
		mySet = set;

		this.setLayout(new GridLayout(0, 1));
		constructProperties();
	}
	
	public void constructProperties() {
		this.add(createFinitePanel());
		this.add(createCardinalityPanel());
		this.add(new MembershipPanel());
		
	}

	private JComponent createCardinalityPanel() {
		
		int size = JFLAPPreferences.getDefaultTextSize();
		MagnifiableTextField textField = new MagnifiableTextField(size);
		textField.setEditable(false);
		
		MagnifiablePanel panel = new MagnifiablePanel();
		panel.add(new MagnifiableLabel("Cardinality: ", size));
		
		if (mySet.isFinite()) {
			int cardinality = ( (FiniteSet) mySet).getCardinality();
			textField.setText(Integer.toString(cardinality));
		}
		else {
			textField.setText("Undefined for infinite set");
		}
		panel.add(textField);
		
		return panel;
	}

	private JComponent createFinitePanel() {
		MagnifiablePanel panel = new MagnifiablePanel();
		
		JCheckBox finite = new JCheckBox("Finite");
		finite.setEnabled(false);
		JCheckBox infinite = new JCheckBox("Infinite");
		infinite.setEnabled(false);
		
		finite.setSelected(mySet.isFinite());
		infinite.setSelected(!mySet.isFinite());
		
		panel.add(finite);
		panel.add(infinite);
		return panel;
	}


	
	private class MembershipPanel extends MagnifiableToolbar {
		
		private MagnifiableTextField myInputTextField;
		private MagnifiableTextField myAnswerTextField;
		
		public MembershipPanel() {
			setFloatable(false);
			myInputTextField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
			myAnswerTextField = new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize());
			myAnswerTextField.setEditable(false);
			
			add(myInputTextField);
			add(new MagnifiableLabel( " member of \"" + mySet.getName() + "\"?\t", 
					JFLAPPreferences.getDefaultTextSize()));
			add(myAnswerTextField);
			
			myInputTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void changedUpdate(DocumentEvent arg0) {
					updateAnswer();
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					updateAnswer();
					
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					updateAnswer();
				}

				private void updateAnswer() {
					String element = myInputTextField.getText().trim();
					element.replaceAll("\\s+", " ");
					boolean contains = mySet.contains(new Element(element));
					myAnswerTextField.setText(Boolean.toString(contains));
				}
				
				
			});
		}
	}
	
}

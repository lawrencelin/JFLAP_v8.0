package view.sets;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;
import util.view.thinscroller.ThinScrollBarScrollPane;
import view.EditingPanel;
import debug.JFLAPDebug;

public abstract class SetDefinitionView extends EditingPanel {

	protected UndoKeeper myKeeper;
	
	protected MagnifiableTextField myNameTextField;
	protected MagnifiableTextField myDescriptionTextField;
	protected MagnifiableTextField myElementsTextField;
	
	private JComponent myCentralPanel;
	
	public SetDefinitionView(UndoKeeper keeper) {
		super(keeper, true);
		myKeeper = keeper;
		
		int size = JFLAPPreferences.getDefaultTextSize();
		myNameTextField = new MagnifiableTextField(size);
		myDescriptionTextField = new MagnifiableTextField(size);
		myElementsTextField = new MagnifiableTextField(size);
		
		myCentralPanel = new MagnifiablePanel();
		myCentralPanel.setLayout(new GridLayout(0, 1));
		myCentralPanel.add(makeAttributePanel("Name", myNameTextField));
		myCentralPanel.add(makeAttributePanel("Description", myDescriptionTextField));
		myCentralPanel.add(makeAttributePanel("Elements", myElementsTextField));
		
		this.setLayout(new BorderLayout());
		add(myCentralPanel, BorderLayout.CENTER);
	
	}
	
	private JComponent makeAttributePanel(String title, JTextComponent field) {		
		MagnifiableToolbar panel = new MagnifiableToolbar();
		panel.setFloatable(false);
		panel.add(new MagnifiableLabel(title, JFLAPPreferences.getDefaultTextSize()));
		panel.add(field);
		return panel;
	}
	
	public abstract void updateDefinitionPanel();
	
	public void setFieldsEditable(boolean editable) {
		myNameTextField.setEditable(editable);
		myDescriptionTextField.setEditable(editable);
		myElementsTextField.setEditable(editable);
	}
	
	
	public String getNameOfSet() {
		return myNameTextField.getText().trim();
	}
	
	public String getDescriptionOfSet() {
		return myDescriptionTextField.getText().trim();
	}
	
	public String getElements() {
		return myElementsTextField.getText().trim();
	}
	
}

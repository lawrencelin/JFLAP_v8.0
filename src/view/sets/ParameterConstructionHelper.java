package view.sets;

import java.awt.GridLayout;

import javax.swing.JComponent;

import model.sets.num.CongruenceSet;
import model.sets.num.MultiplesSet;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.MagnifiableToolbar;

public class ParameterConstructionHelper {
	
	public static JComponent createInputPanel(Class c) {
		MagnifiablePanel params = new MagnifiablePanel();
		params.setLayout(new GridLayout(0, 1));
		if (c.equals(MultiplesSet.class)) {
			params.add(createField("Factor (multiples of __)"));
			return params;
		}
		if (c.equals(CongruenceSet.class)) {
			params.add(createField("Dividend (a in a mod n"));
			params.add(createField("Modulus (n in a mod n"));
			return params;
		}
		
		return null;
	}
		
	private static JComponent createField(String label) {
		MagnifiableToolbar bar = new MagnifiableToolbar();
		bar.setFloatable(false);
		bar.add(new MagnifiableLabel(label, JFLAPPreferences.getDefaultTextSize()));
		bar.add(new MagnifiableTextField(JFLAPPreferences.getDefaultTextSize()));
		return bar;
	}

}

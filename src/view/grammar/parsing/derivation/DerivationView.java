package view.grammar.parsing.derivation;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import model.algorithms.testinput.parse.Derivation;
import model.grammar.Production;
import model.grammar.Terminal;
import model.grammar.Variable;

import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableTabbedPane;

public class DerivationView extends MagnifiableTabbedPane{

	public DerivationView(DerivationPanel ... panels){
		for (DerivationPanel p: panels){
			this.add(p);
		}
	}

	/**
	 * Creates a default {@link DerivationView} which will contain a
	 * non-inverted derivation tree and a derivation table.
	 * @param d
	 */
	public DerivationView(Derivation d){
		this(new DerivationTreePanel(d, false), new DerivationTable(d));
	}


	public void setDerivation(Derivation d){
		for(Component c: this.getComponents()){
			if (c instanceof DerivationPanel)
				((DerivationPanel)c).setDerivation(d);
		}
	}

}

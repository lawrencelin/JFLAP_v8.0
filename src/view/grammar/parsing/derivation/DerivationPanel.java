package view.grammar.parsing.derivation;

import model.algorithms.testinput.parse.Derivation;
import util.view.magnify.MagnifiablePanel;

public class DerivationPanel extends MagnifiablePanel {

	private Derivation myDerivation;
	
	public DerivationPanel(String name){
		setName(name);
	}
	
	public void setDerivation(Derivation d){
		myDerivation = d;
		repaint();
	}
	
	public Derivation getDerivation(){
		return myDerivation;
	}
	
	public void reset() {
		// do nothing
	}
	
	public void undo() {
		// do nothing
	}
}

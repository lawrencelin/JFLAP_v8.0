package view.action.algorithm;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.steppable.SteppableAlgorithm;

public abstract class AlgorithmAction<T extends SteppableAlgorithm> extends AbstractAction {

	
	private T myAlgorithm;

	public AlgorithmAction(String name, T alg){
		super(name);
		myAlgorithm = alg;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		actionPerformed(myAlgorithm);
	}


	public abstract void actionPerformed(T alg);

}

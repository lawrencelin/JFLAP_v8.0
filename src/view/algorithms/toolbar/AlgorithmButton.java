package view.algorithms.toolbar;

import javax.swing.JButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.steppable.SteppableAlgorithm;
import model.change.events.AdvancedChangeEvent;

public abstract class AlgorithmButton extends JButton implements ChangeListener{

	
	public AlgorithmButton(SteppableAlgorithm alg){
		alg.addListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (!(e instanceof AdvancedChangeEvent)) return;
		
		AdvancedChangeEvent event = (AdvancedChangeEvent) e;
		if (event.comesFrom(SteppableAlgorithm.class))
			this.algorithmChanged(event);
	}

	public abstract void algorithmChanged(AdvancedChangeEvent e);
}

package view.algorithms.toolbar;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.algorithms.steppable.SteppableAlgorithm;
import model.change.events.AdvancedChangeEvent;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiableToolbar;
import view.action.StepAction;
import view.action.algorithm.AlgorithmCompleteAction;
import view.action.algorithm.AlgorithmResetAction;

public class SteppableToolbar extends MagnifiableToolbar implements ChangeListener{

	private MagnifiableButton myStepButton;
	private MagnifiableButton myCompleteButton;
	private MagnifiableButton myResetButton;

	public SteppableToolbar(SteppableAlgorithm alg, boolean floatable){
		setFloatable(floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		myStepButton = new MagnifiableButton(new StepAction(alg), size);
		myCompleteButton = new MagnifiableButton(new AlgorithmCompleteAction(alg), size);
		myResetButton = new MagnifiableButton(new AlgorithmResetAction(alg), size);
		alg.addListener(this);
		this.add(myStepButton);
		this.add(myCompleteButton);
		this.add(myResetButton);
		updateDefaultButtons(alg);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof AdvancedChangeEvent)
			updateButtons((SteppableAlgorithm) e.getSource());
	}
	
	
	private void updateDefaultButtons(SteppableAlgorithm alg){
		myStepButton.setEnabled(alg.canStep());
		myCompleteButton.setEnabled(alg.canStep());
		myResetButton.setEnabled(alg.isRunning());
	}

	public void updateButtons(SteppableAlgorithm alg) {
		updateDefaultButtons(alg);
	}
	
}

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
import view.action.algorithm.AlgorithmUndoAction;
import view.grammar.parsing.derivation.DerivationController;

public class SteppableToolbar extends MagnifiableToolbar implements ChangeListener{

	private MagnifiableButton myStepButton;
	private MagnifiableButton myCompleteButton;
	private MagnifiableButton myResetButton;
	private MagnifiableButton myUndoButton;

	public SteppableToolbar(SteppableAlgorithm alg, boolean floatable){
		setFloatable(floatable);
		int size = JFLAPPreferences.getDefaultTextSize();
		myStepButton = new MagnifiableButton(new StepAction(alg), size);
		myCompleteButton = new MagnifiableButton(new AlgorithmCompleteAction(alg), size);
		myResetButton = new MagnifiableButton(new AlgorithmResetAction(alg), size);
		myUndoButton = new MagnifiableButton(new AlgorithmUndoAction(alg), size);
		
		alg.addListener(this);
		this.add(myStepButton);
		if (alg instanceof DerivationController) {
			this.add(myUndoButton);
		}
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
		myUndoButton.setEnabled(alg.canUndo());
	}

	public void updateButtons(SteppableAlgorithm alg) {
		updateDefaultButtons(alg);
	}
	
}

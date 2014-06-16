package model.algorithms.steppable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.JFLAPConstants;

import debug.JFLAPDebug;

import model.algorithms.AlgorithmException;
import model.change.ChangingObject;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.Describable;

public abstract class SteppableAlgorithm extends ChangingObject implements Describable,
																				JFLAPConstants, 
																				Steppable {

	
	public static final int ALG_STEP = 0;
	private AlgorithmStep[] mySteps;
	private List<SteppableAlgorithmListener> myListeners;

	
	public SteppableAlgorithm() {
		mySteps = initializeAllSteps();
		myListeners = new ArrayList<SteppableAlgorithmListener>();
	}
	
	
	/**
	 * Initialize the sequence of {@link AlgorithmStep} for this
	 * {@link SteppableAlgorithm}
	 * @return
	 */
	public abstract AlgorithmStep[] initializeAllSteps();



	/**
	 * Progresses this algorithm to the next step, and returns
	 * a boolean as to whether or not the step was successful.
	 * 
	 * @return
	 */
	@Override
	public AlgorithmStep step() throws AlgorithmException{

		AlgorithmStep current = getCurrentStep();
		if (current != null){
			boolean occurred = current.execute();
			if (occurred) distributeChange(new AdvancedChangeEvent(this, ALG_STEP, current));
		}
		return current;
	}

	public AlgorithmStep getCurrentStep() {
		for (AlgorithmStep step : mySteps){
			if (step.isComplete()) continue;
			return step;
		}
		return null;
	}
	
	public boolean stepToCompletion(){
		while (this.step() != null);
		return !this.canStep();
		
	}
	
	public boolean canStep(){
		return this.getCurrentStep() != null;
	}
	
	public boolean isRunning(){
		return this.canStep();
	}
	
	public abstract boolean reset() throws AlgorithmException;
	
	
	
}

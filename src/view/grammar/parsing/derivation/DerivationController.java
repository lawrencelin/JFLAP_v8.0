package view.grammar.parsing.derivation;

import debug.JFLAPDebug;
import model.algorithms.AlgorithmException;
import model.algorithms.steppable.AlgorithmStep;
import model.algorithms.steppable.SteppableAlgorithm;
import model.algorithms.testinput.parse.Derivation;
import model.change.events.AdvancedChangeEvent;
import model.grammar.Production;
import model.symbols.SymbolString;

public class DerivationController extends SteppableAlgorithm {
	private final int RESET = 0;
	
	private Derivation myDerivation;
	private DerivationView myView;
	int step;
	
	public DerivationController (DerivationView view, Derivation finalDerivation){
		myView = view;
		setDerivation(finalDerivation);
	}
	
	public void setDerivation(Derivation finalDerivation){
		myDerivation = finalDerivation;
		reset();
	}

	@Override
	public String getDescriptionName() {
		return "Derivation Controller";
	}

	@Override
	public String getDescription() {
		return "Algorithm for displaying Derivaitons step-by-step";
	}

	@Override
	public AlgorithmStep[] initializeAllSteps() {
		return new AlgorithmStep[]{ new NextDerivationStep() };
	}

	@Override
	public boolean reset() throws AlgorithmException {
		step = 0;
		myView.setDerivation(myDerivation.getSubDerivation(step));
		myView.reset();
		distributeChange(new AdvancedChangeEvent(this, RESET, myDerivation));
		myView.repaint();
		return true;
	}
	
	@Override
	public boolean canUndo() {
		// Undo button can be pressed if it's not the initial step
		return step > 0;
	}
	
	public void undo() throws AlgorithmException {
		step--;
		myView.setDerivation(myDerivation.getSubDerivation(step));
		myView.undo();
		distributeChange(new AdvancedChangeEvent(this, step, myDerivation));
//		myView.repaint();
	}
	
	private class NextDerivationStep implements AlgorithmStep{

		@Override
		public String getDescriptionName() {
			return "Next Derivation Step";
		}

		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean execute() throws AlgorithmException {
			return nextDerivation();
		}

		@Override
		public boolean isComplete() {
			return myDerivation.getSubDerivation(step).equals(myDerivation);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see model.algorithms.steppable.SteppableAlgorithm#isRunning()
	 * Always return true so that the parse tree can be redone even if the derivation
	 * is complete, in case people want to watch it again. 
	 */
	@Override
	public boolean isRunning(){
		return true;
	}
	
	private boolean nextDerivation(){
		step++;
		myView.setDerivation(myDerivation.getSubDerivation(step));
		
		return true;
	}

}

package model.algorithms.testinput;

import model.algorithms.AlgorithmException;
import model.algorithms.FormalDefinitionAlgorithm;
import model.algorithms.testinput.parse.ParserException;
import model.change.events.AdvancedChangeEvent;
import model.formaldef.FormalDefinition;
import model.symbols.SymbolString;
import errors.BooleanWrapper;

public abstract class InputUsingAlgorithm<T extends FormalDefinition> extends FormalDefinitionAlgorithm<T> {

	public static final int INPUT_SET = 1;
	private SymbolString myInput;
	
	public InputUsingAlgorithm(T fd) {
		super(fd);
	}

	public boolean setInput(SymbolString string){
		BooleanWrapper isValid = isValidInput(string);
		if (isValid.isError())
			throw new ParserException(isValid.getMessage());
		myInput = string;
		distributeChange(new AdvancedChangeEvent(this, INPUT_SET, myInput));	
		return resetInternalStateOnly();
	}

	public BooleanWrapper isValidInput(SymbolString string){
		if (string == null) return new BooleanWrapper(true);
		return checkValid(string);
	}
	
	public abstract BooleanWrapper checkValid(SymbolString string);
	
	public abstract boolean resetInternalStateOnly();
	
	public SymbolString getInput(){
		return myInput;
	}
	
	private boolean hasInput(){
		return myInput != null;
	}
	
	@Override
	public boolean canStep() {
		if(!hasInput()) return false;
		return super.canStep();
	}
	
	@Override
	public boolean isRunning() {
		return this.hasInput() && super.isRunning();
	}
	@Override
	public boolean reset() throws AlgorithmException {
		return setInput(null);
	}
}

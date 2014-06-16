package model.sets.operations;

import java.util.ArrayList;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.InfiniteSet;


public abstract class SetOperation {
	
	protected ArrayList<AbstractSet> myOperands;
	
	public SetOperation () { }
	
	public void setOperands (ArrayList<AbstractSet> operands) {
		if (operands.size() != getNumberOfOperands()) {
			throw new SetOperationException("Wrong number of operands!\n" +
					"The " + getName() + " operation takes " + getNumberOfOperands() + " operand(s), " +
					"but you supplied " + operands.size() + ".");
		}
		myOperands = operands;
	}
	
	public abstract int getNumberOfOperands();

		
	public AbstractSet evaluate() {
		if (answerIsFinite()) {
			return getFiniteAnswer();
		}
		return getInfiniteAnswer();
	}

	
	protected boolean answerIsFinite() {
		for (AbstractSet set : myOperands) {
			if (!set.isFinite())
				return false;
		}
		return true;
	}

	protected abstract FiniteSet getFiniteAnswer();

	protected abstract InfiniteSet getInfiniteAnswer();

	public abstract String getName();
	
	public abstract String getDescription();
	

}

package model.algorithms.testinput.parse.lr.rules;

import model.algorithms.testinput.parse.lr.SLR1Production;

public class ReduceRule extends SLR1rule {

	private int myProductionIndex;

	public ReduceRule(int productionIndex) {
		myProductionIndex = productionIndex;
	}

	@Override
	public String getDescriptionName() {
		return "Reduce Rule";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		return "r" + myProductionIndex;
	}

	public int getProductionIndex() {
		return myProductionIndex;
	}

}

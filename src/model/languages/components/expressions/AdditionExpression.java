package model.languages.components.expressions;

public class AdditionExpression extends ArithmeticExpression {
	
	private AbstractExpression myFirstOperand;
	private AbstractExpression mySecondOperand;
	
	public AdditionExpression (AbstractExpression a, AbstractExpression b) {
		myFirstOperand = a;
		mySecondOperand  = b;
	}

	@Override
	public int evaluate () {
		return myFirstOperand.evaluate() + mySecondOperand.evaluate();
	}
	
	



}

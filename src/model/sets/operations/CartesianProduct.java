package model.sets.operations;

import java.util.LinkedHashSet;
import java.util.Set;

import model.sets.AbstractSet;
import model.sets.FiniteSet;
import model.sets.InfiniteSet;
import model.sets.elements.Element;

public class CartesianProduct extends SetOperation {

	@Override
	protected FiniteSet getFiniteAnswer() {
		Set<Element> elements = new LinkedHashSet<Element>();
		for (Element e1 : myOperands.get(0).getSet()) {
			for (Element e0: myOperands.get(1).getSet()) {
				elements.add(new Element(new Tuple(e1, e0).toString()));
			}
		}
		return new FiniteSet(getDescription(), elements);
	}

	@Override
	protected InfiniteSet getInfiniteAnswer() {
		// TODO Auto-generated method stub
		return new CartesianSet(myOperands.get(0), myOperands.get(1));
	}

	@Override
	public int getNumberOfOperands() {
		return 2;
	}

	@Override
	public String getName() {
		return "Cartesian Product";
	}

	@Override
	public String getDescription() {
		return "The Cartesian product of " + myOperands.get(0).getName() + " and " + myOperands.get(1).getName();
	}
	
	
	
	private class Tuple {
		
		private Element myFirst;
		private Element mySecond;
		
		public Tuple(Element arg0, Element arg1) {
			myFirst = arg0;
			mySecond = arg1;
		}
		
		public String toString() {
			return "(" + myFirst.toString() + ", " + mySecond.toString() + ")";
		}
	}
	
	
	private class CartesianSet extends InfiniteSet {
		
		private AbstractSet myFirst;
		private AbstractSet mySecond;
		
		public CartesianSet(AbstractSet first, AbstractSet second) {
			super();
			myFirst = first;
			mySecond = second;
		}

		@Override
		protected Element getNext() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public boolean contains(Element e) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public AbstractSet copy() {
			return new CartesianSet(myFirst, mySecond);
		}
		
	}

}

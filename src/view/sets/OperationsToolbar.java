package view.sets;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import javax.swing.JComponent;

import model.sets.operations.CartesianProduct;
import model.sets.operations.Difference;
import model.sets.operations.Intersection;
import model.sets.operations.Powerset;
import model.sets.operations.SetOperation;
import model.sets.operations.Union;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableToolbar;
import view.action.sets.DoSetOperationAction;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class OperationsToolbar extends MagnifiablePanel {
	
	private static final Class[] myOperationsClasses = {
		Intersection.class,
		Union.class,
		Powerset.class,
		Difference.class,
		CartesianProduct.class
	};

	
	public OperationsToolbar() {
//		setFloatable(floatable);
		initOperations();
	}
	
	private void initOperations() {
		for (Class c : myOperationsClasses) {
			if (!Modifier.isAbstract(c.getModifiers()) && 
				SetOperation.class.isAssignableFrom(c)) 
				add(createNewButton(c));
		}
	}
	
	/**
	 * 
	 * @param class of the set operation for which a button is being made
	 * @return button associated with a particular set operation
	 */
	private JComponent createNewButton(Class c) {
		try {
			SetOperation op = (SetOperation) c.getDeclaredConstructor().newInstance();
			return new SetOperationButton(op);
			
		// none of these exceptions should be thrown, method should not return null
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return null;
	}

	
	
	
	private class SetOperationButton extends MagnifiableButton {
		
		public SetOperationButton (SetOperation operation) {
			super(new DoSetOperationAction(operation), 30);
			setToolTipText(createToolTipText(operation));
		}
		
		private String createToolTipText(SetOperation op) {
			return op.getName() + " (" + op.getNumberOfOperands() + ")";
		}
		
	}
	
	
}

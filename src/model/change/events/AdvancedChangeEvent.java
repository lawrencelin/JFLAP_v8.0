package model.change.events;

import javax.swing.event.ChangeEvent;

import model.formaldef.components.ChangeTypes;
import model.formaldef.components.FormalDefinitionComponent;

import util.UtilFunctions;


public class AdvancedChangeEvent extends ChangeEvent implements ChangeTypes{

	
	private int myChangeType;
	
	private Object[] myArgs;
	
	
	public AdvancedChangeEvent(Object source,
									int type,
									Object ... args){
		super(source);
		myChangeType = type;
		myArgs = args;
	}
	
	
	/**
	 * Returns true if the {@link FormalDefinitionComponent} 
	 * class associated with this {@link AdvancedChangeEvent}
	 * is the same as or is a subclass of the passed in 
	 * <code>FormalDefinitionComponent</code> 
	 * 
	 * @see {@link Class}.isAssignableFrom()
	 * 
	 * @param c
	 * @return
	 */
	public boolean comesFrom(Class s){
		return s.equals(this.getSource().getClass());
	}
	
	public boolean comesFrom(Object o){
		return this.getSource().equals(o);
	}
	
	public int getNumArgs(){
		return myArgs.length;
	}
	
	public Object getArg(int i){
		return myArgs[i];
	}
	
	public int getType(){
		return myChangeType;
	}
	
}

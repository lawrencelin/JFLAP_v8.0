package model.formaldef.components;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;

import model.change.ChangingObject;
import model.change.events.SetToEvent;
import model.formaldef.Describable;
import model.formaldef.FormalDefinitionException;
import model.symbols.Symbol;
import util.Copyable;

public abstract class SetSubComponent<T extends SetSubComponent<T>> extends ChangingObject 
implements Describable, 
Copyable, 
Comparable<T>, 
Settable<T>{

	private Set<SetComponent<T>> myParents;

	public SetSubComponent(){
		myParents = new HashSet<SetComponent<T>>();
	}

	public void addParent(SetComponent<T> parent){
		this.addListener(parent);
		myParents.add(parent);
	}

	public void removeParent(SetComponent<T> parent) {
		this.removeListener(parent);
		if (myParents.remove(parent))
			parent.remove(this);
	}

	public Set<SetComponent<T>> getParents(){
		return myParents;
	}

	@Override
	public boolean setTo(T other) {
		if (other.equals(this)) return false;
		ChangeEvent change = new SetToEvent<T>((T) this, this.copy(), other);
		for (SetComponent<T> parent: myParents){
			if (parent.contains(other) ){
				throw new FormalDefinitionException("The " + parent.getDescriptionName() + 
						" already contains the " + other.getDescriptionName() + " " + other.toString() +".");
			}
		}
		applySetTo(other);
		distributeChange(change);
		return true;
	}

	protected abstract void applySetTo(T other); //not sure if I like this.

	@Override
	public abstract T copy();


}

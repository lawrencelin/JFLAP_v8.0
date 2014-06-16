package model.formaldef.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.change.ChangingObject;
import model.change.events.AddEvent;
import model.change.events.AdvancedChangeEvent;
import model.change.events.AdvancedUndoableEvent;
import model.change.events.ModifyEvent;
import model.change.events.RemoveEvent;
import model.change.events.SetToEvent;
import model.formaldef.FormalDefinitionException;
import model.symbols.Symbol;

import util.Copyable;

import errors.BooleanWrapper;

public abstract class SetComponent<T extends SetSubComponent<T>> extends FormalDefinitionComponent 
																	implements SortedSet<T>, ChangeListener{

	
	private TreeSet<T> myComponents;
	
	public SetComponent() {
		myComponents = new TreeSet<T>();
	}
	
	@Override
	public boolean add(T e) {
		Collection<T> toAdd = new ArrayList<T>();
		toAdd.add(e);
		return addAll(toAdd);	
		}

	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		ChangeEvent e = new AddEvent<T>(this, c);
		boolean added = myComponents.addAll(c);
		if (added){
			for (T item : c){
				item.addParent(this);
			}
			distributeChange(e);
		}
		return added;

	}
	
	public Set<T> toCopiedSet(){
		Set<T> set = new TreeSet<T>();
		for(T s: this){
			set.add(s.copy());
		}
		return set;
	}


	@Override
	public void clear() {
		this.removeAll(new TreeSet<T>(this));
	}

	@Override
	public boolean contains(Object o) {
		return myComponents.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return myComponents.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return myComponents.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return myComponents.iterator();
	}

	@Override
	public boolean remove(Object o) {
		Collection<T> toRemoved = new ArrayList<T>();
		toRemoved.add((T) o);
		return removeAll(toRemoved);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		ChangeEvent e = new RemoveEvent<T>(this, (Collection<? extends T>) c);
		boolean removed = myComponents.removeAll(c);
		if (removed){
			for (Object item : c){
				((SetSubComponent<T>) item).removeParent(this);
			}
			distributeChange(e);
		}
		return removed;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		Set<T> temp = new TreeSet<T>(this);
		temp.removeAll(c);
		return this.removeAll(temp);
	}

	@Override
	public int size() {
		return myComponents.size();
	}

	@Override
	public Object[] toArray() {
		return myComponents.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return myComponents.toArray(a);
	}

	@Override
	public Comparator<? super T> comparator() {
		return myComponents.comparator();
	}

	@Override
	public T first() {
		return myComponents.first();
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return myComponents.headSet(toElement);
	}

	@Override
	public T last() {
		return myComponents.last();
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return myComponents.subSet(fromElement, toElement);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return myComponents.tailSet(fromElement);
	}

	@Override
	public String toString() {
		return this.getDescriptionName() + ": " + myComponents.toString();
	}

	@Override
	public FormalDefinitionComponent copy() {
		try {
			SetComponent<T> cloned = this.getClass().newInstance();
			for (T obj: this){
				cloned.add((T) obj.copy());
			}
			return cloned;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof SetToEvent){
			SetToEvent<T> event = (SetToEvent<T>) e;
			this.distributeChange(new ModifyEvent(this, event));
		}
		else
			this.distributeChange(e);
	}
	

}

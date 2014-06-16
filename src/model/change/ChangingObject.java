package model.change;

import java.util.HashSet;
import java.util.Set;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class ChangingObject {

	
	private Set<ChangeListener> myListeners;
	
	public ChangingObject() {
		myListeners = new HashSet<ChangeListener>();
	}
	
	public boolean addListener(ChangeListener listener){
		return myListeners.add(listener);
	}
	
	public void clearListeners(){
		myListeners.clear();
	}
	
	public boolean removeListener(ChangeListener listener){
		return myListeners.remove(listener);
	}
	
	public void distributeChanged(){
		ChangeEvent event = new ChangeEvent(this);
		this.distributeChange(event);
	}

	public void distributeChange(ChangeEvent event) {
		for (ChangeListener listener: myListeners.toArray(new ChangeListener[0])){
			listener.stateChanged(event);
		}
	}
}

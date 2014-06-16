package model.algorithms.testinput.simulate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import debug.JFLAPDebug;


public class ConfigurationChain extends LinkedList<Configuration>{
	
	private boolean amFrozen;
	private boolean amFocused;
	private ConfigurationChain myParent;
	private int numForked;
	private String myID;
	
	public ConfigurationChain(Configuration configuration, 
									ConfigurationChain parent,
									String id) {
		amFrozen = false;
		myParent = parent;
		myID = id;
		numForked = 0;
		this.add(configuration);
	}
	

	public Configuration getCurrentConfiguration() {
		return getLast();
	}

	public boolean isFrozen(){
		return amFrozen;
	}


	public boolean canThaw(){
		return this.isFrozen();
	}


	public void thaw(){
		amFrozen = false;
	}


	public boolean canFreeze(){
		return !this.isFrozen() && !(isFinished());
	}


	public void freeze(){
		amFrozen = true;
	}



	public void reverse() {
		this.removeLast();
	}
	
	
	@Override
	public ConfigurationChain clone() {
		ConfigurationChain clone = (ConfigurationChain) super.clone();
		clone.myID = this.myID;
		clone.myParent = this.getParent();
		clone.amFrozen = this.isFrozen();
		return clone;
	}


	@Override
	public boolean equals(Object o) {
		return o instanceof ConfigurationChain && 
				((ConfigurationChain) o).getID().equals(this.getID());
	}

	@Override
	public int hashCode() {
		return myID.hashCode();
	}
	
	
	public String getID() {
		return myID;
	}


	public boolean isHalted() {
		return this.isFrozen() || isFinished();
	}

	public ConfigurationChain getParent() {
		return myParent;
	}

	

	@Override
	public String toString() {
		return myID + ": " + super.toString();
	}


	public boolean isFinished(){
		return getCurrentConfiguration().isAccept() || getCurrentConfiguration().isReject();
	}


	public boolean isAccept() {
		return getCurrentConfiguration().isAccept();
	}

	public boolean isReject() {
		return getCurrentConfiguration().isReject();
	}


	public int getNumChildren() {
		return numForked;
	}


	public void incrementNumChildren() {
		numForked++;
	}


	
}

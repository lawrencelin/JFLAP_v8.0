package model.sets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import view.sets.ActiveSetsList;


/**
 * Maintain a collection of the sets the user has 
 * created or decided to add from pre-existing options
 * 
 * @author peggyli
 * 
 */

public class ActiveSetsRegistry {
		
	private HashMap<String, AbstractSet> myRegistryMap;
	

	public ActiveSetsRegistry () {
		myRegistryMap = new HashMap<String, AbstractSet>();
	}
	
	/**
	 * Adds a set to the active sets registry
	 * @param set
	 */
	public void add (AbstractSet set) {
		myRegistryMap.put(set.getName(), set);
		notifyObserver();
	}
	
	public void remove (AbstractSet set) {
		myRegistryMap.remove(set.getName());
		notifyObserver();
	}
	
	
	public AbstractSet getSetByName (String key) {
		return myRegistryMap.get(key);
	}
	
	private ActiveSetsList mySubject;
	
	public void setObserver (ActiveSetsList disp) {
		mySubject = disp;
		
	}
	
	public void notifyObserver () {
		mySubject.update(this.getNameArray());
	}
	
	
	public ArrayList<AbstractSet> getSelectedSets () {
		return mySubject.getSelectedSets();
	}
	
	/**
	 * Return array of strings representing the names of the
	 * sets in the registry
	 * @return
	 */
	private String[] getNameArray () {
		String[] names = new String[myRegistryMap.size()];
		int index = 0;
		Iterator<String> iter = myRegistryMap.keySet().iterator();
		while (iter.hasNext()) {
			names[index] = iter.next();
			index++;
		}
		return names;
	}
	
	
	
	public void updateName(String oldname, String newname) {
		myRegistryMap.put(newname, myRegistryMap.get(oldname));
		myRegistryMap.remove(oldname);
	}

}

package model.undo;


import java.awt.Container;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Action;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.undo.UndoManager;

import model.change.events.SetToEvent;
import model.change.events.UndoableEvent;
import model.symbols.Symbol;

import debug.JFLAPDebug;


import util.Copyable;


import errors.BooleanWrapper;





public class UndoKeeper implements ChangeListener{

	private Deque<IUndoRedo> myUndoQueue;
	private Deque<IUndoRedo> myRedoQueue ;
	private Set<UndoKeeperListener> myListeners;
	private boolean amLocked;
	private boolean amCombining;
	private CompoundUndoRedo myCombineAction;

	public enum UndoableActionType{
		UNDO,REDO;
	}

	public UndoKeeper() {
		myUndoQueue = new LinkedList<IUndoRedo>();
		myRedoQueue = new LinkedList<IUndoRedo>();
		myListeners = new HashSet<UndoKeeperListener>();
	}

	public <T extends Copyable> void registerChange(IUndoRedo toAdd){
		if (amLocked) 
			return;
		else if (amCombining){
			if (myCombineAction == null)
				myCombineAction = new CompoundUndoRedo(toAdd);
			else
				myCombineAction.add(toAdd);
		}
		else if(toAdd != null){
			
			myUndoQueue.push(toAdd);
			myRedoQueue.clear();
			broadcastStateChange();
		}
	}

	public boolean undoLast(){
		return undoLast(1);
	}

	public boolean undoLast(int n){
		return genericAct(n, myUndoQueue, myRedoQueue, UndoableActionType.UNDO);
	}

	public boolean genericAct(int n, Deque<IUndoRedo> from, Deque<IUndoRedo> to, UndoableActionType help) {
		if (amLocked) return false;
		amLocked = true;
		boolean test = true;
		while (!from.isEmpty() && n > 0){
			IUndoRedo toApply = from.peek();
			switch(help){
			case UNDO: test = toApply.undo(); break;
			case REDO: test = toApply.redo(); break;
			}
			if (!test) break;
			to.push(from.pop());
			n--;
			broadcastStateChange();
		}
		amLocked = false;
		return test;
	}

	private void broadcastStateChange() {
		for (UndoKeeperListener listener: myListeners){
			listener.keeperStateChanged();
		}
	}

	public boolean redoLast(){
		return redoLast(1);
	}

	public boolean redoLast(int n){
		return genericAct(n, myRedoQueue, myUndoQueue, UndoableActionType.REDO);
	}


	public boolean canRedo() {
		return !myRedoQueue.isEmpty();
	}

	public boolean canUndo() {
		return !myUndoQueue.isEmpty();
	}

	public boolean addUndoListener(UndoKeeperListener listener) {
		return myListeners.add(listener);
	}

	public boolean removeUndoListener(UndoKeeperListener listener){
		return myListeners.remove(listener);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e instanceof UndoableEvent)
			this.registerChange((IUndoRedo) e);
	}

	public void clear() {
		myUndoQueue.clear();
		myRedoQueue.clear();
	}


	public void beginCombine() {
		amCombining = true;
	}

	public void endCombine(boolean shouldAdd) {
		amCombining = false;
		if (shouldAdd)
			registerChange(myCombineAction);
		myCombineAction = null;
	}
	
	public boolean applyAndListen(IUndoRedo init){
		if (amCombining) {
			registerChange(init);
			return init.redo();
		}
		beginCombine();
		myCombineAction = new CompoundUndoRedo(init);
		boolean shouldAdd = init.redo();
		endCombine(shouldAdd);
		return shouldAdd;
	}

}

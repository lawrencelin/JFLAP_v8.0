package view.lsystem.parameters;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import model.change.events.AdvancedUndoableEvent;
import model.lsystem.LSystem;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

import util.JFLAPConstants;
import view.lsystem.helperclasses.Parameter;
import view.lsystem.helperclasses.ParameterMap;

public class ParameterDataHelper extends ArrayList<Object[]> implements
		JFLAPConstants {
	private static final Object[] EMPTY = new Object[]{"",	""};
	private UndoKeeper myKeeper;
	private ArrayList<Parameter> myParameters;
	private ChangeListener listener;

	public ParameterDataHelper (LSystem model, UndoKeeper keeper){
		myKeeper = keeper;
		myParameters = new ArrayList<Parameter>();
		ParameterMap params = model.getParameters();
		
		for(String key : params.keySet())
			myParameters.add(new Parameter(key, params.get(key)));
	}
	
	public void setListener(ChangeListener listen){
		listener = listen;
	}
	
	private void fireChangeEvent(){
		if(listener != null)
			listener.stateChanged(new ChangeEvent(this));
	}
	
	@Override
	public void add(int index, Object[] input) {
		Parameter p = this.objectToParameter(input);
		if(isValid(p)){
			TableAddParamEvent add2 = new TableAddParamEvent(p, index);
			myKeeper.applyAndListen(add2);
		}
	}

	@Override
	public Object[] set(int index, Object[] input) {
		Object[] old = this.get(index);
		if (index >= this.myParameters.size())
			this.add(input); 
		else {
			Parameter to = this.objectToParameter(input);
			if (isValid(to)){
				Parameter from = myParameters.get(index);
				SetParamEvent set = 
						new SetParamEvent(from, from.copy(), to);
				if (!myKeeper.applyAndListen(set));
			}
			else{
				remove(index);
			}
		}

		return old;
	}

	private boolean isValid(Parameter p) {
		return !p.isEmpty();
	}

	@Override
	public boolean add(Object[] input) {
		int s = this.size()-1;
		this.add(s, input);
		return s < this.size();
	}

	@Override
	public void clear() {
		myParameters.clear();
		fireChangeEvent();
	}


	@Override
	public Object[] get(int index) {
		if (index >= myParameters.size()) return EMPTY;
		return myParameters.get(index).toArray();
	}

	@Override
	public Object[] remove(int index) {
		if (index >= myParameters.size()) return EMPTY;
		Parameter remove = myParameters.get(index);
		RemoveParamEvent event =
				new RemoveParamEvent(remove, index);
		myKeeper.applyAndListen(event);
		return remove.toArray();
	}

	@Override
	public int size() {
		return myParameters.size() + 1;
	}

	@Override
	public Iterator<Object[]> iterator() {
		ArrayList<Object[]> converted = new ArrayList<Object[]>();
		for (Parameter p: myParameters)
			converted.add(p.toArray());
		return converted.iterator();
	}


	private Parameter objectToParameter(Object[] input){
		
		if (input[0] == null)
			input[0] = "";
		if (input[1] == null)
			input[1] = "";
		return new Parameter((String) input[0], (String) input[1]);
	}	
	
	private class TableAddParamEvent implements IUndoRedo{

		private Parameter myParam;
		private int myIndex;

		public TableAddParamEvent(Parameter p, int i) {
			myParam = p;
			myIndex = i;
		}

		@Override
		public boolean undo() {
			boolean remove;
			if(remove = myParameters.remove(myIndex) != null)
				fireChangeEvent();
			return remove;
		}

		@Override
		public boolean redo() {
			myParameters.add(myIndex, myParam);
			fireChangeEvent();
			return true;
		}

		@Override
		public String getName() {
			return "Add Parameter to LSystem";
		}

	}
	
	private class SetParamEvent extends AdvancedUndoableEvent {

		private Parameter myParam;

		public SetParamEvent(Parameter source, Parameter from, Parameter to) {
			super(source, ITEM_MODIFIED, from, to);
			myParam = source;
		}

		@Override
		public boolean undo() {
			boolean set;
			if(set = myParam.setTo(getFrom()))
				fireChangeEvent();
			return set;
		}
		
		@Override
		public boolean redo() {
			boolean set;
			if(set = myParam.setTo(getTo()))
				fireChangeEvent();
			return set;
		}
		
		@Override
		public Parameter getSource() {
			return (Parameter) super.getSource();
		}
		
		public Parameter getFrom(){
			return (Parameter) getArg(0);
		}
		
		public Parameter getTo(){
			return (Parameter) getArg(1);
		}

		@Override
		public String getName() {
			return "Set " + getFrom() + " to " + getTo();
		}

	}
	
	private class RemoveParamEvent implements IUndoRedo{

		private Parameter myParam;
		private int myIndex;

		public RemoveParamEvent(Parameter p, int i) {
			myParam = p;
			myIndex = i;
		}


		@Override
		public boolean undo() {
			myParameters.add(myIndex, myParam);
			return true;
		}

		@Override
		public boolean redo() {
			boolean remove;
			if(remove = myParameters.remove(myIndex) != null)
				fireChangeEvent();
			return remove;
		}


		@Override
		public String getName() {
			return "Remove Parameter from LSystem";
		}

	}

}

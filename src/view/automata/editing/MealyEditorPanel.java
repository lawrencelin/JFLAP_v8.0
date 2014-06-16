package view.automata.editing;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import debug.JFLAPDebug;

import model.automata.State;
import model.automata.acceptors.fsa.FSATransition;
import model.automata.transducers.OutputFunctionSet;
import model.automata.transducers.mealy.MealyMachine;
import model.automata.transducers.mealy.MealyOutputFunction;
import model.change.events.RemoveEvent;
import model.formaldef.components.SetComponent;
import model.undo.CompoundUndoRedo;
import model.undo.IUndoRedo;
import model.undo.UndoKeeper;

public class MealyEditorPanel extends AutomatonEditorPanel<MealyMachine, FSATransition>{

	public MealyEditorPanel(MealyMachine m, UndoKeeper keeper, boolean editable) {
		super(m, keeper, editable);
	}
	
	@Override
	public IUndoRedo createTransitionRemove(Collection<FSATransition> trans) {
		OutputFunctionSet<MealyOutputFunction> funcSet = getAutomaton().getOutputFunctionSet();
		IUndoRedo sup = super.createTransitionRemove(trans);
		if(!trans.isEmpty()){
			RemoveEvent<MealyOutputFunction> remove = getMealyRemoveEvent(trans,
					funcSet);
			CompoundUndoRedo comp = new CompoundUndoRedo(remove);
			comp.add(sup);
			return comp;
		}
		return sup;
	}
	
	@Override
	public CompoundRemoveEvent createCompoundRemoveEvent(
			State[] states, Set<FSATransition> transitions, Point2D[] points) {
		CompoundRemoveEvent sup = super.createCompoundRemoveEvent(states, transitions, points);
		
		if(!transitions.isEmpty())
			sup.addEvent(getMealyRemoveEvent(transitions, getAutomaton().getOutputFunctionSet()));
		return sup;
	}


	private RemoveEvent<MealyOutputFunction> getMealyRemoveEvent(
			Collection<FSATransition> trans,
			OutputFunctionSet<MealyOutputFunction> funcSet) {
		List<MealyOutputFunction> toRemove = new ArrayList<MealyOutputFunction>();
		for(FSATransition t : trans){
			for(MealyOutputFunction func : funcSet)
				if(func.matches(t))
					toRemove.add(func);
		}
		RemoveEvent<MealyOutputFunction> remove = new RemoveEvent<MealyOutputFunction>(funcSet, toRemove);
		return remove;
	}

}

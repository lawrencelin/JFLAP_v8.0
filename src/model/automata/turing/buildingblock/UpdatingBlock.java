package model.automata.turing.buildingblock;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import model.automata.turing.TapeAlphabet;
import model.automata.turing.TuringMachine;
import model.change.events.AdvancedChangeEvent;

/**
 * Superclass for all blocks that need to implement internal changes
 * when the TapeAlphabet is modified.
 * @author Julian
 *
 */
public abstract class UpdatingBlock extends Block implements ChangeListener {
	
	public UpdatingBlock(TuringMachine tm, 
						TapeAlphabet parentAlph, 
						String name, 
						int id, 
						Object ... args) {
		super(tm, name, id);
		parentAlph.addListener(this);
		constructFromBase(parentAlph, tm, args);
		this.updateTuringMachine(parentAlph);
	}

	public abstract void constructFromBase(TapeAlphabet parentAlph, TuringMachine localTM, Object ... args);

	@Override
	public void stateChanged(ChangeEvent e) {
		if(!(e instanceof AdvancedChangeEvent))
		return;
		AdvancedChangeEvent event = (AdvancedChangeEvent) e;
		if(event.comesFrom(TapeAlphabet.class)){
			updateTuringMachine((TapeAlphabet) event.getSource());
		}
	}
	
	public abstract void updateTuringMachine(TapeAlphabet parentAlph);

}

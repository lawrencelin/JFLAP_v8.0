package oldnewstuff.model.change.interactions;

import model.change.events.CompoundUndoableChangeEvent;
import model.change.events.UndoableEvent;
import oldnewstuff.model.change.ChangeDistributingObject;
import oldnewstuff.model.change.ChangeDistributor;
import oldnewstuff.model.change.ChangeEvent;
import oldnewstuff.model.change.ChangeListener;
import oldnewstuff.model.change.ChangeRelated;
import oldnewstuff.model.change.ChangeTypes;

/**
 * An abstract interaction class which is responsible for applying the 
 * full effects of an individual change. 
 * @author Julian
 *
 * @param <T>
 */
public abstract class Interaction extends ChangeRelated{

	private ChangeDistributor myDistributor;

	public Interaction(int type, ChangeDistributor distributor){
		super(type);
		myDistributor = distributor;
	}
	

	public boolean applyInteraction(ChangeEvent e){
		
		boolean changed = e.applyChange();
			
		if (changed){
			CompoundUndoableChangeEvent event = new CompoundUndoableChangeEvent(myDistributor,
					e.getName());
			if (e instanceof UndoableEvent)
				event.addSubEvents((UndoableEvent)e);
			addAndApplyInteractions(e, event);
			myDistributor.distributeChange(event);
		}
		return changed;
	}
	
	/**
	 * Applies any interaction necessary to accomplish this change,
	 * and then returns the "meta" change containing all change 
	 * events applied in this interaction.
	 * 
	 * @param event
	 * @return
	 */
	protected abstract void addAndApplyInteractions(ChangeEvent e, CompoundUndoableChangeEvent event);

	


}

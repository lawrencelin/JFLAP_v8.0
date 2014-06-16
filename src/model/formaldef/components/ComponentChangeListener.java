package model.formaldef.components;

import model.change.events.AdvancedChangeEvent;

public interface ComponentChangeListener {

	public void componentChanged(AdvancedChangeEvent event);
	
}

package view.environment;

import java.awt.Component;

public class TabChangedEvent {

	private int myCount;
	private Component mySource;

	public TabChangedEvent(Component source, int tabCount){
		mySource = source;
		myCount = tabCount;
	}
	
	
	public Component getCurrentView(){
		return mySource;
	}
	
	public int getNewTabCount(){
		return myCount;
	}
}

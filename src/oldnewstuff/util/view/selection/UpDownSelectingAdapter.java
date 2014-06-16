package oldnewstuff.util.view.selection;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class UpDownSelectingAdapter extends KeyAdapter {

	ISelector mySelector;
	private boolean canGoIn;
	private boolean canExit;
	private Component myExitTarget;
	
	public UpDownSelectingAdapter(ISelector selector){
		this(selector, false);
	}
	
	public UpDownSelectingAdapter(ISelector selector, boolean goIn) {
		this(selector, goIn, false, null);
	}
	
	public UpDownSelectingAdapter(ISelector selector, boolean goIn, boolean exit, Component exitTarget){
		mySelector = selector;
		canGoIn = goIn;
		canExit = exit;
		myExitTarget = exitTarget;
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_UP){
			mySelector.selectPrevious();
		}
		else if(e.getKeyCode() == KeyEvent.VK_DOWN){
			mySelector.selectNext();
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER && canGoIn){
			((Component) mySelector.getSelected()).requestFocusInWindow();
		}
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE && canExit){
			myExitTarget.requestFocusInWindow();
		}
	}
}

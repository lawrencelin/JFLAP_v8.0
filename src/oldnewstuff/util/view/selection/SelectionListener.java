package oldnewstuff.util.view.selection;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;

import util.view.MouseClickAdapter;
import util.view.SuperMouseAdapter;



public class SelectionListener<S extends JComponent, T extends ISelector> extends MouseClickAdapter<S> {

	
	private T mySelector;


	public SelectionListener(T selector){
		mySelector = selector;
	}
	
	
	@Override
	public void leftClickResponse(MouseEvent e, S component) {
		mySelector.clearSelection();
		if (component instanceof ISelectable)
			mySelector.select((ISelectable) component);
		if (mySelector instanceof Component){
			((Component) mySelector).repaint();
		}
	}
	

}

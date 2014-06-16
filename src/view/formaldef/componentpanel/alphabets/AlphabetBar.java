package view.formaldef.componentpanel.alphabets;

import java.awt.Color;
import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.text.JTextComponent;


import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import view.action.ModifySymbolAction;
import view.formaldef.componentpanel.DefinitionComponentPanel;
import view.formaldef.componentpanel.SetComponentBar;



import model.formaldef.components.SetComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.symbols.Symbol;
import model.undo.UndoKeeper;

public class AlphabetBar<T extends Alphabet> extends DefinitionComponentPanel<T> {

	private SymbolBar mySymbolBar;
	private JTextComponent myFocus;

	public AlphabetBar(T comp, UndoKeeper keeper, boolean allowModify) {
		super(comp, keeper, allowModify);
		mySymbolBar = new SymbolBar();
		mySymbolBar.setTo(comp.toArray(new Symbol[0]));
		setUpLabels();
		setUpFocusManager();
	}

	private void setUpLabels() {
		this.add(new JLabel("{"));
		this.add(mySymbolBar);
		this.add(new JLabel("}"));
	}
	
	
	@Override
	public void update(ChangeEvent e) {
		if (e.getSource() instanceof Alphabet){
			Symbol[] symbols = ((Alphabet) e.getSource()).toArray(new Symbol[0]);
			mySymbolBar.setTo(symbols);	
		}
	}


	/**
	 * Retrieve the right-click menu linked to the 
	 * individual symbol boxes.
	 * 
	 * @param item
	 * @return
	 */
	public JPopupMenu getBoxMenu(Symbol item) {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new ModifySymbolAction(item, getKeeper()));
		
		return menu;
	}
	
	public void addToCurrentTextFocus(Symbol item) {
		if (myFocus == null) return;
		myFocus.replaceSelection(item.toString());
	}
	
	public Color getHighlightColor(){
		return JFLAPPreferences.getBackgroundColor();
	}
	
	private void setUpFocusManager() {
		KeyboardFocusManager focusManager =
		    KeyboardFocusManager.getCurrentKeyboardFocusManager();
		focusManager.addPropertyChangeListener(
		    new PropertyChangeListener() {
		    	

				public void propertyChange(PropertyChangeEvent e) {
		            String prop = e.getPropertyName();
		            if (("focusOwner".equals(prop))){
		                  if (e.getNewValue() instanceof JTextComponent)
		                	  myFocus = (JTextComponent) e.getNewValue();
		                  else
		                	  myFocus = null;
		            }
	            }
		    }
		);		
	}
	
	private class SymbolBar extends SetComponentBar<Symbol>{

		public SymbolBar() {
			super(getHighlightColor());
		}

		@Override
		public void doClickResponse(Symbol item, MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3 && isEditable())
				getBoxMenu(item).show(e.getComponent(), e.getX(), e.getY());
			else if(e.getButton() == MouseEvent.BUTTON1){
				addToCurrentTextFocus(item);
			}
		}
		
		
	}

	
}

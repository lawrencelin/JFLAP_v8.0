package view.formaldef.componentpanel;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import debug.JFLAPDebug;

import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import util.view.SuperMouseAdapter;
import util.view.magnify.Magnifiable;
import view.EditingPanel;


import model.formaldef.components.FormalDefinitionComponent;
import model.undo.UndoKeeper;

public abstract class DefinitionComponentPanel<T extends FormalDefinitionComponent> extends EditingPanel 
													implements JFLAPConstants, ChangeListener, Magnifiable{

	private JLabel myLabel;
	private T myComp;
	private UndoKeeper myKeeper;

	public DefinitionComponentPanel(T comp, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		comp.addListener(this);
		comp.addListener(keeper);
		myComp = comp;
		myKeeper = keeper;
		myLabel = new JLabel();
		myLabel.setText(comp.getCharacterAbbr() + " = ");
		this.add(myLabel);
		this.setToolTipText(comp.getDescriptionName());
	}
	
	public UndoKeeper getKeeper(){
		return myKeeper;
	}
	
	public T getComponent(){
		return myComp;
	}
	
	@Override
	public void stateChanged(ChangeEvent e) {
		update(e);
		revalidate();
		repaint();
	}
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		for (Component c: this.getComponents())
			c.setFont(this.getFont().deriveFont(size));
	}	


	public abstract void update(ChangeEvent e);

}

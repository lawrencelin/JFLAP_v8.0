package view.formaldef.componentpanel.alphabets;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;

import debug.JFLAPDebug;



import model.formaldef.components.FormalDefinitionComponent;
import model.symbols.SpecialSymbol;
import model.symbols.Symbol;
import model.undo.UndoKeeper;

import universe.preferences.JFLAPPreferences;
import view.action.ModifySymbolAction;
import view.action.SetSpecialSymbolAction;
import view.formaldef.componentpanel.DefinitionComponentPanel;

public class SpecialSymbolPanel extends DefinitionComponentPanel<SpecialSymbol>{

	private JButton myButton;
	private SpecialSymbol mySpecialSymbol;

	public SpecialSymbolPanel(SpecialSymbol comp, UndoKeeper keeper, boolean modify) {
		super(comp, keeper, modify );
		mySpecialSymbol = comp;
		JToolBar bar = new JToolBar();
		bar.add(myButton = new JButton(comp.symbolOnlyString()));
		myButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3 && 
						isEditable() && !mySpecialSymbol.isPermanent())
					getMenu().show(e.getComponent(), e.getX(), e.getY());
			}
		});
		this.add(bar);
		bar.setFloatable(false);
	}

	@Override
	public void update(ChangeEvent e) {
		if (e.getSource() instanceof SpecialSymbol){
			SpecialSymbol ss = (SpecialSymbol) e.getSource();
			myButton.setText(ss.symbolOnlyString());
		}
	}

	public JPopupMenu getMenu() {
		JPopupMenu menu = new JPopupMenu();
		menu.add(new SetSpecialSymbolAction(mySpecialSymbol, getKeeper()));
		return menu;

	}

	@Override
	public void setMagnification(double mag) {
		super.setMagnification(mag);
		float size = (float) (mag*JFLAPPreferences.getDefaultTextSize());
		Font f = this.getFont().deriveFont(size);
		myButton.setFont(f);
	}
}

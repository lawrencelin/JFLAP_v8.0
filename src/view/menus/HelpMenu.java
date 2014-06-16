package view.menus;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import view.help.AboutAction;
import view.help.HelpAction;
import view.help.MainMenuHelpAction;
import view.help.PreferenceAction;




public class HelpMenu extends JMenu {

	public HelpMenu(){
		super("Help");
		this.add(new HelpAction());
		this.add(new AboutAction());
		this.add(new PreferenceAction());
		
	}
	
}

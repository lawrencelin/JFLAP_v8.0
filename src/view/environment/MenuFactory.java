package view.environment;

import java.awt.Component;

import javax.swing.JMenuBar;

import view.menus.JFLAPMenuBar;

public class MenuFactory {

	public static JFLAPMenuBar createMenu(JFLAPEnvironment e) {
		JFLAPMenuBar menu = new JFLAPMenuBar(e);
		e.addTabListener(menu);
		return menu;
	}

}

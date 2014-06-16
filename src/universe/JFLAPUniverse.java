package universe;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JComponent;

import debug.JFLAPDebug;

import oldnewstuff.main.JFLAP;

import universe.mainmenu.MainMenu;
import universe.preferences.JFLAPPreferences;
import view.ViewFactory;
import view.environment.JFLAPEnvironment;
import view.pumping.PumpingLemmaChooserView;

/**
 * The universe which holds all windows/Environments in the currently running
 * JFLAP program. This is a singleton class which can be called statically by
 * anything in the program. Automatically handles swing-based focus changes and
 * window activations.
 * 
 * @author Julian Genkins
 * 
 */
public class JFLAPUniverse {

	/**
	 * The collection of all Environments.
	 */
	public static LinkedList<JFLAPEnvironment> REGISTRY;

	/**
	 * The main menu object. Singleton, there needs only be one.
	 */
	private static MainMenu MAIN_MENU;

	/**
	 * Intializing static block -
	 */
	static {
		MAIN_MENU = new MainMenu();
		REGISTRY = new LinkedList<JFLAPEnvironment>();
		JFLAPPreferences.importFromFile();
	}

	public static Collection<JFLAPEnvironment> getRegistry() {
		return REGISTRY;
	}

	public static JFLAPEnvironment registerEnvironment(Component comp) {
		JFLAPEnvironment env = new JFLAPEnvironment(comp, getNextUnusedID());
		registerEnvironment(env);
		return env;
	}

	public static void registerEnvironment(JFLAPEnvironment env) {
		JFLAPPreferences.addChangeListener(env);
		setUpWindowListener(env);
		setActiveEnvironment(env);
	}

	public static boolean registerEnvironment(File f) {
		JFLAPEnvironment e = getEnvironment(f);
		if (e != null) {
			e.requestFocus();
			return false;
		}
		registerEnvironment(new JFLAPEnvironment(f, getNextUnusedID()));
		return true;
	}

	private static JFLAPEnvironment getEnvironment(File f) {
		for (JFLAPEnvironment e : REGISTRY) {
			if (e.getFileName().equals(f.getName())){
				//check for empty PumpingLemma environments, instantiate the InputView if not there
				if(e.getPrimaryView() instanceof PumpingLemmaChooserView && e.getSavableObject() == null)
						e.addSelectedComponent(ViewFactory.createView(f));
				return e;
			}
				
		}
		return null;
	}

	private static int getNextUnusedID() {
		boolean used = true;
		int i = -1;
		while (used) {
			i++;
			used = getEnvironment(i) != null;
		}
		return i;
	}

	public static JFLAPEnvironment getEnvironment(int i) {
		for (JFLAPEnvironment e : REGISTRY) {
			if (e.getID() == i)
				return e;
		}
		return null;
	}

	private static void setUpWindowListener(JFLAPEnvironment env) {
		env.addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {
				setActiveEnvironment((JFLAPEnvironment) e.getComponent());
			}
		});
		env.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosed(WindowEvent e) {
				deregisterEnvironment((JFLAPEnvironment) e.getComponent());
			}
		});
	}

	public static void deregisterEnvironment(JFLAPEnvironment env) {
		REGISTRY.remove(env);
		JFLAPPreferences.removeChangeListener(env);
		if (REGISTRY.isEmpty())
			showMainMenu();
	}

	public static void setActiveEnvironment(JFLAPEnvironment env) {
		if (!REGISTRY.isEmpty() && getActiveEnvironment().equals(env))
			return;
		REGISTRY.remove(env);
		REGISTRY.addFirst(env);
	}

	public static void hideMainMenu() {
		MAIN_MENU.setVisible(false);
	}

	public static void showMainMenu() {
		MAIN_MENU.setVisible(true);
	}

	public static void closeMainMenu() {
		MAIN_MENU.dispose();
		MAIN_MENU = null;
	}

	public static JFLAPEnvironment getActiveEnvironment() {
		return REGISTRY.isEmpty() ? null : REGISTRY.getFirst();
	}

	public static boolean isRegistryEmpty() {
		return REGISTRY.isEmpty();
	}

	public static void exit(boolean should_save) {
		JFLAPEnvironment e;
		while ((e = getActiveEnvironment()) != null) {
			if (!e.close(should_save)) {
				return;
			}
			deregisterEnvironment(e);
		}
		System.exit(0);
	}

}

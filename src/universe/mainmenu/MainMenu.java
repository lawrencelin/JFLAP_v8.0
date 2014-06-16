package universe.mainmenu;


import java.awt.AWTKeyStroke;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.JFLAPConstants;
import view.action.batch.BatchTestAction;
import view.action.file.OpenAction;
import view.action.file.ExitAction;
import view.action.newactions.NewAction;
import view.help.AboutAction;
import view.help.MainMenuHelpAction;
import view.help.PreferenceAction;



/**
 * The main menu class for JFLAP. This constructs 
 * @author Julian
 *
 */
public class MainMenu extends JFrame {
	/**
	 * Instantiates a <CODE>NewDialog</CODE> instance.
	 */
	public MainMenu() {
		// super((java.awt.Frame)null, "New Document");
		super("JFLAP " + JFLAPConstants.VERSION);
		getContentPane().setLayout(new GridLayout(0, 1));
		initMenu();
		initComponents();
		setResizable(false);
		this.pack();
		this.setLocation(50, 50);
		
		addNavigationKeys();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				if (!JFLAPUniverse.isRegistryEmpty()) {
					MainMenu.this.setVisible(false);
				} else {
					JFLAPUniverse.exit(true);
				}
			}
		});
		
	}

	private void addNavigationKeys() {
		int forward = KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS;
		int backward = KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS;
		
		Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>(
				getFocusTraversalKeys(forward)), backwardTraversalKeys = new HashSet<AWTKeyStroke>(
				getFocusTraversalKeys(backward));
		forwardTraversalKeys.add(AWTKeyStroke.getAWTKeyStroke(
				KeyEvent.VK_DOWN, KeyEvent.VK_UNDEFINED));
		backwardTraversalKeys.add(AWTKeyStroke.getAWTKeyStroke(
				KeyEvent.VK_UP, KeyEvent.VK_UNDEFINED));		
		setFocusTraversalKeys(forward, forwardTraversalKeys);
		setFocusTraversalKeys(backward, backwardTraversalKeys);
	}

	private void initMenu() {
		// Mini menu!
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(this.createMenu("File", new OpenAction(), new ExitAction()));
		menuBar.add(this.createMenu("Help", new MainMenuHelpAction(), new AboutAction(), new PreferenceAction()));
		menuBar.add(this.createMenu("Batch", new BatchTestAction()));
		setJMenuBar(menuBar);
	}

	private JMenu createMenu(String string, Action ... actions) {
		JMenu menu = new JMenu(string);
		for (Action a: actions)
			menu.add(a);
		return menu;
	}
	
	private void initComponents() {
		
		for (NewAction a: NewAction.getAllNewActions())
		{
			this.add(new JButton(a));
		}	
	}
}

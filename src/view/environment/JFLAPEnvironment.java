package view.environment;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import debug.JFLAPDebug;
import model.automata.turing.TuringMachine;
import model.graph.TransitionGraph;
import universe.preferences.JFLAPPreferences.PREF_CHANGE;
import universe.preferences.PreferenceChangeListener;
import util.JFLAPConstants;
import view.EditingPanel;
import view.ViewFactory;
import view.automata.editing.AutomatonEditorPanel;
import view.automata.editing.BlockEditorPanel;
import view.automata.views.AutomatonView;
import view.automata.views.BlockTMView;
import view.automata.views.TuringMachineView;
import view.formaldef.FormalDefinitionView;
import view.grammar.parsing.cyk.CYKParseView;
import view.lsystem.LSystemRenderView;
import view.menus.JFLAPMenuBar;
import view.pumping.CFPumpingLemmaChooser;
import view.pumping.CompCFPumpingLemmaInputView;
import view.pumping.ComputerFirstView;
import view.pumping.HumanCFPumpingLemmaInputView;
import view.pumping.PumpingLemmaChooser;
import view.pumping.PumpingLemmaChooserView;
import view.pumping.PumpingLemmaInputView;
import view.pumping.RegPumpingLemmaChooser;
import file.XMLFileChooser;
import file.xml.XMLCodec;

public class JFLAPEnvironment extends JFrame implements
		PreferenceChangeListener {

	private File myFile;
	private JTabbedPane myTabbedPane;
	private Component myPrimaryView;
	private boolean amDirty;
	private int myID;
	private List<TabChangeListener> myListeners;

	public JFLAPEnvironment(Object model, int id) {
		this(ViewFactory.createView(model), id);
	}

	public JFLAPEnvironment(File f, int id) {
		this(ViewFactory.createView(f), id);
		setFile(f);
		if (myPrimaryView instanceof PumpingLemmaInputView)
			addPLChooser();
	}

	public JFLAPEnvironment(Component component, int id) {
		super(JFLAPConstants.VERSION_STRING);

		myListeners = new ArrayList<TabChangeListener>();

		myID = id;
		myTabbedPane = new SpecialTabbedPane();
		this.add(myTabbedPane);
		myPrimaryView = component;
		addSelectedComponent(component);
		JFLAPMenuBar menu = MenuFactory.createMenu(this);
		this.setJMenuBar(menu);
		// I believe this is needed to make sure it doesn't close on
		// a cancelled save.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!JFLAPEnvironment.this.close(true)) {
					return;
				}

			}

		});
		this.pack();
		this.setVisible(true);

		setVisible(true);
	}

	public void addTabListener(TabChangeListener menu) {
		myListeners.add(menu);
	}

	public void removeTabListener(TabChangeListener listener) {
		myListeners.remove(listener);
	}

	private void setFile(File f) {
		myFile = f;
		this.setTitle(JFLAPConstants.VERSION_STRING + "(" + myFile.getName()
				+ ")");
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JFLAPEnvironment)
			return this.getID() == ((JFLAPEnvironment) obj).getID();
		return super.equals(obj);
	}

	public int getID() {
		return myID;
	}

	public boolean close(boolean save) {
		// Should check if there's any actual information to save
		if (save && this.isDirty() && getSavableObject() != null) {
			int result = showConfirmDialog("Save changes before closing?");
			if (result == JOptionPane.CLOSED_OPTION
					|| result == JOptionPane.CANCEL_OPTION) {
				return false;
			}
			if (result == JOptionPane.YES_OPTION) {
				if (!this.save(false))
					return false;
			}
		}
		this.dispose();
		return true;
	}

	public boolean save(boolean saveAs) {
		// Used to change myFile back to its current state if save is cancelled
		File temp = myFile;

		// The getSavableObject() may need to be modified
		Object obj = getSavableObject();

		XMLFileChooser chooser = new XMLFileChooser(true);
		chooser.setSelectedFile(myFile);
		int n = JFileChooser.APPROVE_OPTION;

		if (saveAs || myFile == null)
			n = chooser.showSaveDialog(this);

		// Loop until save works or is cancelled.
		while (n == JFileChooser.APPROVE_OPTION) {
			myFile = chooser.getSelectedFile();

			if (!chooser.accept(myFile)){
				String path = myFile.getAbsolutePath();
				if(path.endsWith(JFLAPConstants.JFF_SUFFIX))
					path = path.substring(0, path.length() - JFLAPConstants.JFF_SUFFIX.length());
				myFile = new File(path + JFLAPConstants.JFLAP_SUFFIX);
			}
			if (myFile.exists()) {
				int confirm = showConfirmDialog("File exists. Overwrite file?");

				if (confirm == JOptionPane.CANCEL_OPTION
						|| confirm == JOptionPane.CLOSED_OPTION) {
					myFile = temp;
					return false;
				}
				if (confirm == JOptionPane.NO_OPTION) {
					n = chooser.showSaveDialog(this);
					continue;
				}
			}
			// Either file is new or user chose to overwrite existing file
			setTitle(JFLAPConstants.VERSION_STRING + "(" + myFile.getName()
					+ ")");
			XMLCodec codec = new XMLCodec();

			codec.encode(obj, myFile, null);
			amDirty = false;
			for (EditingPanel ep : getEditingPanels()) {
				ep.setDirty(false);
			}
			return true;
		}
		return false;
	}

	/**
	 * Helper function to see whether or not there is anything to save/what
	 * object to save.
	 * 
	 * @return A formal definition or pumping lemma to save.
	 */
	public Object getSavableObject() {
		for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
			Component c = myTabbedPane.getComponent(i);

			if (c instanceof AutomatonView) {
				return ((AutomatonView) c).createData();
			} else if (c instanceof FormalDefinitionView) {
				return ((FormalDefinitionView) c).getDefinition();
			} else if (c instanceof PumpingLemmaInputView) {
				return ((PumpingLemmaInputView) c).getLemma();
			}

		}
		return null;
	}

	private EditingPanel[] getEditingPanels() {
		List<EditingPanel> editPanels = new ArrayList<EditingPanel>();
		for (int i = 0; i < myTabbedPane.getTabCount(); i++) {
			Component c = myTabbedPane.getComponent(i);
			if (c instanceof EditingPanel)
				editPanels.add((EditingPanel) c);

		}
		return editPanels.toArray(new EditingPanel[0]);
	}

	public boolean isDirty() {
		if (amDirty)
			return true;
		for (EditingPanel ep : getEditingPanels()) {
			if (ep.isDirty())
				return true;
		}
		return false;
	}

	public Component getPrimaryView() {
		return myPrimaryView;
	}

	public void addView(Component component) {
		myTabbedPane.add(component);
		// myTabbedPane.setSelectedComponent(component);

		if (component instanceof EditingPanel)
			amDirty = true;
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		update();
	}

	public void addSelectedComponent(Component component) {
		addView(component);
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount() - 1);
	}

	private void distributeTabChangedEvent() {
		TabChangeListener[] listeners = myListeners.toArray(new TabChangeListener[0]);
		
		for (TabChangeListener l : listeners) {
			l.tabChanged(new TabChangedEvent(myTabbedPane
					.getSelectedComponent(), myTabbedPane.getTabCount()));
		}
	}

	public void closeActiveTab() {
		closeTab(myTabbedPane.getSelectedIndex());
	}

	public void closeTab(int i) {
		Component c = myTabbedPane.getComponent(i);
		if (c instanceof PumpingLemmaInputView) {
			int result = showConfirmDialog("Save changes before closing?");
			if (result == JOptionPane.CLOSED_OPTION
					|| result == JOptionPane.CANCEL_OPTION) {
				return;
			}
			if (result == JOptionPane.YES_OPTION) {
				if (!this.save(false))
					return;
			}
		}

		if (myPrimaryView instanceof BlockTMView && i != 0){
			if(c instanceof TuringMachineView){
				TuringMachineView view = (TuringMachineView) c;
				BlockEditorPanel panel = ((BlockTMView) myPrimaryView).getCentralPanel();
				TransitionGraph graph = ((AutomatonEditorPanel) view.getCentralPanel()).getGraph();
				
				panel.setGraph((TuringMachine) view.getDefinition(), graph);
			}
		}
		if (c instanceof EditingPanel)
			amDirty = true;
		myTabbedPane.remove(i);
		distributeTabChangedEvent();
		myTabbedPane.revalidate();
		myTabbedPane.setSelectedIndex(myTabbedPane.getTabCount() - 1);
		this.repaint();
	}

	public void update() {
		updatePrimaryPanel();
		this.repaint();
	}

	private void updatePrimaryPanel() {
		boolean enabled = myTabbedPane.getTabCount() == 1;
		if (myPrimaryView instanceof EditingPanel)
			((EditingPanel) myPrimaryView).setEditable(enabled);
		myTabbedPane.setEnabledAt(0, enabled);
	}

	@Override
	public String getName() {
		String file = "";
		if (myFile != null){
			String name = myFile.getName();
			if(name.endsWith(JFLAPConstants.JFF_SUFFIX))
				name = name.substring(0, name.length() - JFLAPConstants.JFF_SUFFIX.length());
			file = " (" + name + ")";
		}
		return super.getName() + file;
	}

	private class SpecialTabbedPane extends JTabbedPane {

		@Override
		public void setSelectedComponent(Component c) {
			setSelectedIndex(indexOfTabComponent(c));

		}

		@Override
		public void setSelectedIndex(int index) {
			super.setSelectedIndex(index);
			Dimension newSize = this.getComponentAt(index).getPreferredSize();

			JFLAPEnvironment.this.setPreferredSize(newSize);
			JFLAPEnvironment.this.setSize(newSize);
			JFLAPEnvironment.this.update();
			distributeTabChangedEvent();
		}
	}

	public int getTabCount() {
		return myTabbedPane.getTabCount();
	}

	public boolean hasFile() {
		return myFile != null;
	}

	public String getFileName() {
		if (!hasFile())
			return "";
		String name = myFile.getName();
		if(name.endsWith(JFLAPConstants.JFF_SUFFIX))
			name = name.substring(0, name.length() - JFLAPConstants.JFF_SUFFIX.length());
		return name;
	}

	public Component getCurrentView() {
		return myTabbedPane.getSelectedComponent();
	}

	@Override
	public String toString() {
		if(this.getFileName().isEmpty())
			return "Environment: "+this.getID(); 
		return "Environment: " + this.getFileName();
	}

	private void addPLChooser() {
		PumpingLemmaChooser plc;
		PumpingLemmaChooserView pane;

		if (myPrimaryView instanceof CompCFPumpingLemmaInputView
				|| myPrimaryView instanceof HumanCFPumpingLemmaInputView) {
			plc = new CFPumpingLemmaChooser();
			pane = new PumpingLemmaChooserView((CFPumpingLemmaChooser) plc);
		} else {
			plc = new RegPumpingLemmaChooser();
			pane = new PumpingLemmaChooserView((RegPumpingLemmaChooser) plc);
		}

		// As PumpingLemmaChooserView instatiates as Human First by default:
		if (myPrimaryView instanceof ComputerFirstView)
			pane.setComputerFirst();

		// Place the PLCV under the active tab
		myTabbedPane.removeAll();
		myTabbedPane.add(pane, 0);
		myTabbedPane.add(myPrimaryView, 1);
		myTabbedPane.setSelectedIndex(1);
		myPrimaryView = pane; // To follow the hierarchy as before, with the
								// chooser as the "Primary View"

		myTabbedPane.revalidate();
	}
	
	public Object showConfirmDialog(Object message, Object[] options, Object initialValue){
		JOptionPane pane = new JOptionPane(message,
				JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_CANCEL_OPTION,
				null, options, initialValue);

		JDialog dialog = pane.createDialog(this, "Select an Option");
		NoAltMnemonicDispatcher mnem = new NoAltMnemonicDispatcher(pane);

		int forward = KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS;
		int backward = KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS;
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addKeyEventDispatcher(mnem);

		Set<AWTKeyStroke> forwardTraversalKeys = new HashSet<AWTKeyStroke>(
				dialog.getFocusTraversalKeys(forward)), backwardTraversalKeys = new HashSet<AWTKeyStroke>(
				dialog.getFocusTraversalKeys(backward));

		forwardTraversalKeys.add(AWTKeyStroke.getAWTKeyStroke(
				KeyEvent.VK_RIGHT, KeyEvent.VK_UNDEFINED));
		backwardTraversalKeys.add(AWTKeyStroke.getAWTKeyStroke(
				KeyEvent.VK_LEFT, KeyEvent.VK_UNDEFINED));

		dialog.setFocusTraversalKeys(forward, forwardTraversalKeys);
		dialog.setFocusTraversalKeys(backward, backwardTraversalKeys);
		dialog.setVisible(true);
		dialog.dispose();

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.removeKeyEventDispatcher(mnem);
		return pane.getValue();
	}

	/**
	 * Special rewrite of the static JOptionPane.showConfirmDialog method such
	 * that the dialog can be navigated by using the right and left arrow keys
	 * along with tab and shift-tab.
	 * 
	 * @param message
	 *            The message to be displayed to the user for confirmation.
	 * @return YES_OPTION, NO_OPTION, or CANCEL_OPTION depending on user's
	 *         selection.
	 */
	public int showConfirmDialog(Object message) {
		Object conf = showConfirmDialog(message, null, null);
		if(conf instanceof Integer)
			return ((Integer) conf).intValue();
		return -1;
	}

	private class NoAltMnemonicDispatcher implements KeyEventDispatcher {
		private JButton yes;
		private JButton no;
		private JButton cancel;

		public NoAltMnemonicDispatcher(JOptionPane option) {
			JPanel buttonArea = (JPanel) option.getComponent(1);
			yes = (JButton) buttonArea.getComponent(0);
			no = (JButton) buttonArea.getComponent(1);
			cancel = (JButton) buttonArea.getComponent(2);
		}

		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			boolean keyHandled = false;
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if (e.getKeyCode() == KeyEvent.VK_Y) {
					yes.doClick();
					keyHandled = true;
				} else if (e.getKeyCode() == KeyEvent.VK_N) {
					no.doClick();
					keyHandled = true;
				} else if (e.getKeyCode() == KeyEvent.VK_C) {
					cancel.doClick();
					keyHandled = true;
				}
			}
			return keyHandled;
		}
	}

	@Override
	public void preferenceChanged(String pref, Object val) {
		Component current = getCurrentView();

		if (current instanceof LSystemRenderView && isRenderChange(pref))
			((LSystemRenderView) current).updateDisplay();
		else if (pref.equals(PREF_CHANGE.CYK_direction_change.toString())
				&& current instanceof CYKParseView) {
			((CYKParseView) current).getRunningView().changeDiagonal(
					(Boolean) val);
		} else if (current instanceof AutomatonView) {
			if (pref.equals(PREF_CHANGE.lambda_change.toString()))
				distributeTabChangedEvent();

		}
		revalidate();
		repaint();
	}

	public boolean isRenderChange(String pref) {
		PREF_CHANGE[] renderNeeded = new PREF_CHANGE[] {
				PREF_CHANGE.lambda_change, PREF_CHANGE.LSangle_change,
				PREF_CHANGE.LSdistance_change, PREF_CHANGE.LShue_change,
				PREF_CHANGE.LSincrement_change, PREF_CHANGE.LSwidth_change };
		for (PREF_CHANGE c : renderNeeded)
			if (c.toString().equals(pref))
				return true;
		return false;
	}
}

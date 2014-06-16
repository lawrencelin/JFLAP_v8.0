package oldnewstuff.view.menus.old;


import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import view.menus.HelpMenu;

import jflap.actions.AboutAction;
import jflap.actions.CloseButton;
import jflap.actions.CloseWindowAction;
import jflap.actions.CloseTabAction;
import jflap.actions.HelpAction;
import jflap.actions.NewAction;
import jflap.actions.NewHelpAction;
import jflap.actions.OpenAction;
import jflap.actions.PrintAction;
import jflap.actions.QuitAction;
import jflap.actions.SaveAction;
import jflap.actions.SaveAsAction;
import jflap.actions.SaveGraphBMPAction;
import jflap.actions.SaveGraphGIFAction;
import jflap.actions.SaveGraphJPGAction;
import jflap.actions.SaveGraphPNGAction;
import jflap.actions.TrimAlphabetsAction;
import jflap.actions.delete.DeleteSelectionAction;
import jflap.controller.JFLAPController;
import jflap.model.JFLAPModel;






public abstract class PrimaryMenuCreator implements IMenuCreator{

	

	public JMenuBar createMenuBar(){
		JMenuBar bar = new JMenuBar();
		addMenu(bar, "File", getFileMenuComponents());
		addMenu(bar, "Edit", getModifyMenuComponents());
		addMenu(bar, "Transform", getInputMenuComponents());
		addMenu(bar, "Test", getTestMenuComponents());
		addMenu(bar, "Convert", getConvertMenuComponents());
		bar.add(new HelpMenu());
		bar.add(Box.createGlue());
		bar.add(new CloseButton());
		return bar;
	}

	private void addMenu(JMenuBar bar, String title,
			List<JMenuItem> menuItems) {
		if (menuItems == null) return;
		
		JMenu menu = new JMenu(title);
//		Collections.sort(menuItems, new Comparator<JMenuItem>() {
//
//			@Override
//			public int compare(JMenuItem o1, JMenuItem o2) {
//				return o1.getText().compareTo(o2.getText());
//			}
//		});
		for (JMenuItem item: menuItems){
			menu.add(item);
		}
		bar.add(menu);
	}

	public List<JMenuItem> getFileMenuComponents(){
		return combineActionLists(new ArrayList<JMenuItem>(), 
				new JMenuItem(new NewAction()), 
				new JMenuItem(new OpenAction()), 
				new JMenuItem(new SaveAction()),
				new JMenuItem(new SaveAsAction()),
				constructImageSaveMenu(),
				new JMenuItem(new CloseTabAction(false)),
				new JMenuItem(new CloseWindowAction()),
				new JMenuItem(new PrintAction()),
				new JMenuItem(new ExitAction()));
	}

	protected List<JMenuItem> combineActionLists(List<JMenuItem> list,JMenuItem ... items ) {
		list.addAll(Arrays.asList(items));
		return list;
	}

	protected JMenuItem constructImageSaveMenu() {
		JMenu saveImageMenu = new JMenu("Save Image As...");
		saveImageMenu.add(new SaveGraphJPGAction());
		saveImageMenu.add(new SaveGraphPNGAction());
		saveImageMenu.add(new SaveGraphGIFAction());
		saveImageMenu.add(new SaveGraphBMPAction());
		return saveImageMenu;
	}

	public abstract List<JMenuItem> getInputMenuComponents();

	public abstract List<JMenuItem> getModifyMenuComponents();

	public abstract List<JMenuItem> getTestMenuComponents();

	public abstract List<JMenuItem> getConvertMenuComponents();

}

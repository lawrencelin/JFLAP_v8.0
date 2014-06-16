package view.sets;

/**
 * @author Peggy Li
 */


import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import javax.swing.ScrollPaneConstants;

import model.sets.AbstractSet;
import model.sets.SetsManager;
import model.undo.UndoKeeper;
import universe.JFLAPUniverse;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableLabel;
import util.view.magnify.MagnifiableList;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import view.action.sets.RemoveSetAction;
import view.sets.state.ModifyState;

@SuppressWarnings("serial")
public class ActiveSetsList extends MagnifiablePanel {

	private UndoKeeper myKeeper;

	private MagnifiableLabel myTitle;
	private static MagnifiableList myActiveSets;

	public ActiveSetsList(UndoKeeper keeper) {

		myKeeper = keeper;
		myTitle = new MagnifiableLabel("Active Sets", JFLAPPreferences.getDefaultTextSize());
		myActiveSets = new MagnifiableList(JFLAPPreferences.getDefaultTextSize());

		MagnifiableScrollPane scroll = new MagnifiableScrollPane(myActiveSets);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(myTitle);
		myTitle.setAlignmentX(CENTER_ALIGNMENT);
		
		this.add(scroll);
		myActiveSets.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (getSelectedSets() == null || getSelectedSets().size() == 0)
					return;
				doClickResponse(getSelectedSets().get(0), e);
			}

		});

	}


	public void update(Object[] actives) {
		myActiveSets.setListData(actives);
		myActiveSets.repaint();
	}


	public ArrayList<AbstractSet> getSelectedSets() {
		ArrayList<AbstractSet> selected = new ArrayList<AbstractSet>();
		for (Object list : myActiveSets.getSelectedValues()) {
			AbstractSet set = SetsManager.ACTIVE_REGISTRY.getSetByName(list.toString());
			selected.add(set);
		}

		return selected;

	}


	/*
	 * Right-click menu for each list
	 */
	public JPopupMenu getPopupMenu(AbstractSet set) {
		JPopupMenu menu = new JPopupMenu();
		menu.add((Action) new RemoveSetAction(myKeeper, set));
		return menu;
	}


	public void doClickResponse(AbstractSet set, MouseEvent e) {
		if (set == null)	return;
		// right click to delete
		if (e.getButton() == MouseEvent.BUTTON3)
			getPopupMenu(set).show(e.getComponent(), e.getX(), e.getY());
		// double click to edit
		if (e.getClickCount() == 2) {
			SetsEditingPanel editor = new SetsEditingPanel(myKeeper, true);
			ModifyState state = new ModifyState(set, myKeeper);
			editor.createCentralPanel(state);
			JFLAPUniverse.getActiveEnvironment().addSelectedComponent(editor);
		
		}
	}


}

package view.sets;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import model.sets.AbstractSet;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import view.action.sets.FinishConstructionAction;
import view.sets.state.CreateState;
import view.sets.state.UseExistingState;

public class OptionsMenu extends MagnifiablePanel {

	private SetsEditingPanel myContainer;
	private UndoKeeper myKeeper;

	public OptionsMenu (SetsEditingPanel parent, UndoKeeper keeper) {
		myContainer = parent;
		myKeeper = keeper;
		createOptions();
	}


	private void createOptions () {
		int size = JFLAPPreferences.getDefaultTextSize();
		MagnifiableButton use = new MagnifiableButton("Use an existing set", size);
		use.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				SetsDropdownMenu source = new SetsDropdownMenu();
				UseExistingState state = new UseExistingState(source, myKeeper);
								
				MagnifiablePanel panel = new MagnifiablePanel();
				panel.setLayout(new BorderLayout());
				panel.add(source, BorderLayout.CENTER);
				FinishConstructionAction action = new FinishConstructionAction(myKeeper, state);
				panel.add(new MagnifiableButton(action, JFLAPPreferences.getDefaultTextSize()), BorderLayout.SOUTH);
				myContainer.createRequestPanel(panel);
			}

		});

		MagnifiableButton input = new MagnifiableButton("Input elements for new set", size);
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {	
				CreateState state = new CreateState(myKeeper);
				myContainer.createCentralPanel(state);
			}

		});

		add(use);
		add(input);

	}


}

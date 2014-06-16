package view.sets;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.border.LineBorder;

import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.action.sets.FinishConstructionAction;
import view.sets.state.State;
import debug.JFLAPDebug;

@SuppressWarnings("serial")
public class SetsEditingPanel extends EditingPanel {
	
	private UndoKeeper myKeeper;
	private State myState;
	
	private OptionsMenu myOptionsMenu;
	private MagnifiablePanel myCentralPanel;
	
	public SetsEditingPanel (UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		myKeeper = keeper;
		
		setLayout(new BorderLayout());
		setSize(getPreferredSize().width, getPreferredSize().height);
		myOptionsMenu = new OptionsMenu(this, myKeeper);
		add(myOptionsMenu, BorderLayout.NORTH);
		
		myCentralPanel = new MagnifiablePanel();
		myCentralPanel.setLayout(new BorderLayout());
		myCentralPanel.setBorder(new LineBorder(Color.green));
		add(myCentralPanel, BorderLayout.CENTER);
		
		SizeSlider slider = new SizeSlider();
		slider.distributeMagnification();
		add(slider, BorderLayout.SOUTH);
		
	}
	
	public void createCentralPanel(State state) {
		myState = state;
		myOptionsMenu.setVisible(false);
		myCentralPanel.removeAll();
		myCentralPanel.add(myState.createDefinitionView(), BorderLayout.CENTER);
		myCentralPanel.add(new MagnifiableButton(
				new FinishConstructionAction(myKeeper, state), 
				JFLAPPreferences.getDefaultTextSize()), BorderLayout.SOUTH);
		this.repaint();
	}
	
	
	@Override
	public String getName() {
		return "Sets Editor";
	}

	
	public void createRequestPanel(JComponent comp) {
		myCentralPanel.removeAll();
		myCentralPanel.add(comp);
		myCentralPanel.repaint();
		JFLAPDebug.print("Select");
	}
	
}

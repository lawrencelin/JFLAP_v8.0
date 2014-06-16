package view.sets;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import model.sets.SetsManager;
import model.undo.UndoKeeper;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.Magnifiable;
import util.view.magnify.MagnifiableButton;
import util.view.magnify.MagnifiablePanel;
import util.view.magnify.MagnifiableScrollPane;
import util.view.magnify.MagnifiableSplitPane;
import util.view.magnify.MagnifiableTextField;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.action.sets.ActivateSetAction;
import view.undoing.UndoPanel;

@SuppressWarnings("serial")
public class SetView extends EditingPanel {
	
	private ActiveSetsList myActiveList;
	private JComponent myCentralPanel;
	private UndoKeeper myKeeper;
	private SizeSlider slider;

	public SetView(SetsManager manager) {
		this(manager, new UndoKeeper(), true);
	}
	
	public SetView(SetsManager manager, UndoKeeper keeper, boolean editable) {
		super(keeper, editable);
		setLayout(new BorderLayout());
		
		myKeeper = keeper;
		myCentralPanel = createCentralPanel();
		
		slider = new SizeSlider();
		if (myCentralPanel instanceof Magnifiable) 
			slider.addListener((Magnifiable) myCentralPanel);
		slider.distributeMagnification();
		this.add(slider, BorderLayout.SOUTH);
		
		this.add(myCentralPanel, BorderLayout.CENTER);
		
		if (editable) {
			add(new UndoPanel(myKeeper), BorderLayout.NORTH);
		}
		
		setAsObserveable(manager);
	}
	
	private JComponent createCentralPanel() {
		myActiveList = new ActiveSetsList(myKeeper);
		JScrollPane scroller = new MagnifiableScrollPane(myActiveList);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		OperationsToolbar toolbar = new OperationsToolbar();
		MagnifiableButton button = new MagnifiableButton(new ActivateSetAction(myKeeper), 
				JFLAPPreferences.getDefaultTextSize());
		
		MagnifiablePanel panel = new MagnifiablePanel();
//		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setLayout(new BorderLayout());
		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(scroller, BorderLayout.CENTER);
		panel.add(button, BorderLayout.SOUTH);
		return panel;
	}
	
	@Override
	public String getName() {
		return "Sets View";
	}

	
	public void setAsObserveable(SetsManager observer) {
		observer.setObserveableTarget(myActiveList);
	}
	
}

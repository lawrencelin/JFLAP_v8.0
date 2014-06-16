package view.formaldef;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import debug.JFLAPDebug;


import util.view.magnify.Magnifiable;
import util.view.magnify.SizeSlider;
import view.EditingPanel;
import view.undoing.UndoPanel;

import model.formaldef.FormalDefinition;
import model.undo.UndoKeeper;

public abstract class FormalDefinitionView<T, S extends FormalDefinition> extends EditingPanel{

	
	private FormalDefinitionPanel myDefinitionPanel;
	private JComponent myCentralPanel;
	private UndoKeeper myKeeper;
	private T myModel;
	private SizeSlider slider;
	private JScrollPane scroller;

	public FormalDefinitionView(T model, UndoKeeper keeper, boolean editable){
		super(keeper, editable);
		
		this.setLayout(new BorderLayout());
		myModel = model;
		myKeeper = keeper;
		myDefinitionPanel = new FormalDefinitionPanel(getDefinition(), keeper, editable);
		myCentralPanel = createCentralPanel(myModel, keeper, editable);
		
		scroller = new JScrollPane(myCentralPanel);
		slider = new SizeSlider(myDefinitionPanel);
		if (myCentralPanel instanceof Magnifiable)
			slider.addListener((Magnifiable) myCentralPanel);
		slider.distributeMagnification();
		
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.add(myDefinitionPanel);
		panel.add(slider);
		
		this.add(scroller, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		
		if (editable)
			this.add(createToolbar(getDefinition(), keeper), BorderLayout.NORTH);
	}

	public Component createToolbar(S definition, UndoKeeper keeper) {
		return new UndoPanel(keeper);
	}

	public abstract JComponent createCentralPanel(T model, UndoKeeper keeper, boolean editable);
	
	public Component getCentralPanel(){
		return myCentralPanel;
	}
	
	public void setCentralPanel(JComponent panel){
		myCentralPanel = panel;
		revalidate();
		repaint();
	}
	
	public T getModel(){
		return myModel;
	}
	
	public JScrollPane getScroller(){
		return scroller;
	}
	
	public void distributeMagnifiation() {
		slider.distributeMagnification();
	}
	
	public abstract S getDefinition();
	
	@Override
	public abstract String getName();
}

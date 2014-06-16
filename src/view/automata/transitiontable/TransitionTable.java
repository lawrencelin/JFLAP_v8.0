package view.automata.transitiontable;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;

import model.automata.Automaton;
import model.automata.Transition;
import model.automata.TransitionSet;
import model.change.events.AddEvent;
import model.change.events.SetToEvent;
import model.undo.IUndoRedo;
import universe.preferences.JFLAPPreferences;
import view.automata.editing.AutomatonEditorPanel;
import view.grammar.productions.LambdaRemovingEditor;

/**
 * Table that will pop up to implement the editing of transitions in an
 * automaton graph.
 * 
 * @author Ian McMahon
 */
public abstract class TransitionTable<T extends Automaton<S>, S extends Transition<S>>
		extends JTable {

	private T myAutomaton;
	private S myTrans;
	private AutomatonEditorPanel<T, S> myPanel;
	private MouseAdapter myListener;

	public TransitionTable(int row, int col, S trans, T automaton,
			AutomatonEditorPanel<T, S> panel) {
		super(row, col);
		myTrans = trans;
		myAutomaton = automaton;
		myPanel = panel;
		myPanel.add(this);

		// Set the look and model
		setModel(createModel());
		setGridColor(Color.gray);
		setBorder(new EtchedBorder());

		TableCellEditor edit = new LambdaRemovingEditor();
		for (int i = 0; i < getColumnCount(); i++) {
			getColumnModel().getColumn(i).setCellEditor(edit);
		}

		// Add mouse listener to the AutomatonEditorPanel to stop editing.
		myListener = new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				IUndoRedo stop = stopEditing(false);
				if (stop != null)
					myPanel.getKeeper().registerChange(stop);
			}
		};
		myPanel.addMouseListener(myListener);
		addKeyBindings();
	}

	public abstract TableModel createModel();

	public abstract S modifyTransition();

	public static String getValidString(String s) {
		if (s == null || s.equals(JFLAPPreferences.getEmptyString()))
			s = "";
		return s;
	}

	private void addKeyBindings() {
		InputMap imap = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
		ActionMap amap = getActionMap();

		imap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "Enter");
		amap.put("Enter", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IUndoRedo stop = stopEditing(false);
				if (stop != null)
					myPanel.getKeeper().registerChange(stop);
				if ((e.getModifiers() & ActionEvent.SHIFT_MASK) != 0) {
					S trans = myPanel.createTransition(myTrans.getFromState(),
							myTrans.getToState());
					myPanel.editTransition(trans, true);
				}
			}
		});
		imap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ESCAPE), "Escape");
		amap.put("Escape", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				stopEditing(true);
			}
		});
	}

	/** Returns the table's transition. */
	public S getTransition() {
		return myTrans;
	}

	/** Returns the table's Automaton. */
	public T getAutomaton() {
		return myAutomaton;
	}

	/**
	 * Stops editing the table and modifies the transition accordingly. Will add
	 * events to the UndoKeeper if applicable.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public IUndoRedo stopEditing(boolean cancel) {
		try {
			getCellEditor().stopCellEditing();
		} catch (NullPointerException e) {
		}
		IUndoRedo ret = null;
		if (!cancel) {
			S t = modifyTransition();
			if (t != null) {
				TransitionSet<S> transitions = myAutomaton.getTransitions();
				
				if(!transitions.contains(t)){
					S temp = myTrans.copy();
					boolean wasInTrans = transitions.contains(temp);
					myTrans.setTo(t);
					//Setting may modify the transition set
					if(wasInTrans && !transitions.contains(temp)){
						ret = new SetToEvent<S>(myTrans, temp, t) {
							@Override
							public boolean undo() {
								myPanel.clearSelection();
								return super.undo();
							}

							@Override
							public boolean redo() {
								myPanel.clearSelection();
								return super.redo();
							}
						};
					} else{
						transitions.add(myTrans);
						
						ret = new AddEvent<S>(transitions, myTrans) {
						@Override
						public boolean undo() {
							myPanel.clearSelection();
							return super.undo();
						}

						@Override
						public boolean redo() {
							myPanel.clearSelection();
							return super.redo();
						}
						};
					}
				}
			}				
		}
		removeSelf();
		return ret;
	}

	/**
	 * Clears, revalidates, and notifies the AutomatonEditorPanel that this
	 * Table is no longer needed.
	 */
	private void removeSelf() {
		myPanel.removeMouseListener(myListener);
		myPanel.remove(this);
		myPanel.clearTableInfo();
	}
}

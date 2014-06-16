package view.automata;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import view.automata.editing.AutomatonEditorPanel;
import debug.JFLAPDebug;

public class Note extends JTextArea {

	public static final String BLANK_TEXT = "insert_text";

	private AutomatonEditorPanel myPanel;

	public Note(AutomatonEditorPanel panel, Point p) {
		this(panel, p, BLANK_TEXT);
	}

	public Note(AutomatonEditorPanel panel, Point p, String message) {
		myPanel = panel;
		setLocation(p);
		setText(message);

		setBorder(BorderFactory.createLineBorder(Color.black));
		setDisabledTextColor(Color.black);
		this.setSelectionStart(0);
		this.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				resetBounds();
			}
			
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				resetBounds();
			}
			
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				resetBounds();
			}
		});
		this.requestFocus();
	}
	
	public void resetBounds() {
		setBounds(new Rectangle(getLocation(), getPreferredSize()));
	}
	
	
	
	public boolean isEmpty() {
		String text = getText();
		return text == null || text.isEmpty();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Note){
			return getLocation().equals(((Note) obj).getLocation()) && getText().equals(((Note) obj).getText());
		}
		return false;
	}
}

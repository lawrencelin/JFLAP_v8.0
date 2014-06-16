package util.view.magnify;

import java.awt.Font;

import javax.swing.JList;
import javax.swing.ListModel;

import universe.preferences.JFLAPPreferences;

public class MagnifiableList extends JList implements Magnifiable {
	
	private int myDefaultSize;

	public MagnifiableList(int defaultSize) {
		super();
		myDefaultSize = defaultSize;
	}
	
	public MagnifiableList (ListModel model, int defaultSize) {
		super(model);
		myDefaultSize = defaultSize;
	}
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(Font.PLAIN, size);
		this.setFont(f);
	}

}

package util.view.magnify;

import java.awt.Font;

import javax.swing.JLabel;


public class MagnifiableLabel extends JLabel implements Magnifiable {

	private int myDefaultSize;

	public MagnifiableLabel(int defaultSize){
		this("", defaultSize);
	}

	public MagnifiableLabel(String string, int defaultSize) {
		super(string);
		myDefaultSize = defaultSize;
	}

	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(Font.PLAIN, size);
		this.setFont(f);
	}

}

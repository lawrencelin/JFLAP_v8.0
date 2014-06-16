package util.view.magnify;

import java.awt.Font;

import javax.swing.JTextArea;

public class MagnifiableTextArea extends JTextArea implements Magnifiable {

	private int myDefaultSize;
	
	public MagnifiableTextArea (int defaultSize) {
		super();
		myDefaultSize = defaultSize;
	}
	
	
	public MagnifiableTextArea(String string, int defaultSize) {
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

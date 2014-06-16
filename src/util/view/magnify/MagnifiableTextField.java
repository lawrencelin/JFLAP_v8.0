package util.view.magnify;

import java.awt.Font;

import javax.swing.JTextField;

public class MagnifiableTextField extends JTextField implements Magnifiable {
	private int myDefaultSize;
	
	public MagnifiableTextField(int defaultSize){
		super();
		myDefaultSize = defaultSize;
	}
	
	
	@Override
	public void setMagnification(double mag) {
		float size = (float) (mag*myDefaultSize);
		Font f = this.getFont().deriveFont(Font.PLAIN, size);
		this.setFont(f);
	}

}

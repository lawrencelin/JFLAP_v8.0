package util.view.magnify;

import java.awt.Component;
import java.awt.LayoutManager;

import javax.swing.JPanel;


public class MagnifiablePanel extends JPanel implements Magnifiable {

	public MagnifiablePanel() {
		super();
	}

	public MagnifiablePanel(boolean b) {
		super(b);
	}

	public MagnifiablePanel(LayoutManager lm, boolean b) {
		super(lm, b);
	}

	public MagnifiablePanel(LayoutManager lm) {
		super(lm);
	}

	@Override
	public void setMagnification(double mag) {
		for (Component c: this.getComponents()){
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
		additionalMagnificationAction(mag);
		this.repaint();
	}
	
	protected void additionalMagnificationAction(double mag) {
		// by default, do nothing
	}
	
	
}

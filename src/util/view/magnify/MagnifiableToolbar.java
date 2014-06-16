package util.view.magnify;

import java.awt.Component;

import javax.swing.JToolBar;

public class MagnifiableToolbar extends JToolBar implements Magnifiable {

	public MagnifiableToolbar() {

	}

	public MagnifiableToolbar(int arg0) {
		super(arg0);

	}

	public MagnifiableToolbar(String arg0) {
		super(arg0);

	}

	public MagnifiableToolbar(String arg0, int arg1) {
		super(arg0, arg1);
	}
	
	@Override
	public void setMagnification(double mag) {
		for (Component c: this.getComponents()){
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
		this.repaint();
	}

}

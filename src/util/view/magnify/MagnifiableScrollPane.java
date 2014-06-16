package util.view.magnify;

import java.awt.Component;

import javax.swing.JScrollPane;

import debug.JFLAPDebug;

public class MagnifiableScrollPane extends JScrollPane implements Magnifiable {

	public MagnifiableScrollPane(Component comp){
		super(comp);
	}
	
	@Override
	public void setMagnification(double mag) {
		Component view = getViewport().getView();
		if(view instanceof Magnifiable)
			((Magnifiable) view).setMagnification(mag);
		this.repaint();
	}

}

package util.view.magnify;

import java.awt.Component;

import javax.swing.JSplitPane;

public class MagnifiableSplitPane extends JSplitPane implements Magnifiable {

	public MagnifiableSplitPane() {
	}

	public MagnifiableSplitPane(int arg0) {
		super(arg0);
	}

	public MagnifiableSplitPane(int arg0, boolean arg1) {
		super(arg0, arg1);
	}

	public MagnifiableSplitPane(int arg0, Component arg1, Component arg2) {
		super(arg0, arg1, arg2);
	}

	public MagnifiableSplitPane(int arg0, boolean arg1, Component arg2,
			Component arg3) {
		super(arg0, arg1, arg2, arg3);
	}
	
	@Override
	public void setMagnification(double mag) {
		Component cRight = this.getRightComponent();
		Component cLeft = this.getLeftComponent();
		for (Component c: new Component[]{cRight, cLeft}){
			if (c instanceof Magnifiable)
				((Magnifiable) c).setMagnification(mag);
		}
		this.repaint();
	}

}

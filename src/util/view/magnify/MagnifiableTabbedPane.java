package util.view.magnify;

import java.awt.Component;

import javax.swing.JTabbedPane;

public class MagnifiableTabbedPane extends JTabbedPane implements Magnifiable {

	public MagnifiableTabbedPane() {
	}

	public MagnifiableTabbedPane(int tabPlacement) {
		super(tabPlacement);
	}

	public MagnifiableTabbedPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}

	@Override
	public void setMagnification(double mag) {
		for(Component c: this.getComponents()){
			if (c instanceof Magnifiable)
				((Magnifiable)c).setMagnification(mag);
		}
	}

}

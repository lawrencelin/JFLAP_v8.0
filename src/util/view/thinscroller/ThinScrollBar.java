package util.view.thinscroller;

import java.awt.Point;

import javax.swing.JScrollBar;


public class ThinScrollBar extends JScrollBar{

	public ThinScrollBar(int orientation, int thinness) {
		super(orientation);
		this.setUI(new ThinScrollBarUI());
		this.setSize(this.getSize().width, thinness);
		Point p = this.getLocation();
		this.setLocation(new Point((int)p.getX(), (int) (p.getY() + thinness/2)));
	}
	
}

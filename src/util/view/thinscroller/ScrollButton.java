package util.view.thinscroller;

import java.awt.Adjustable;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.plaf.basic.BasicArrowButton;

public class ScrollButton extends BasicArrowButton{

	
	public ScrollButton(int direction, int height) {
		super(direction);
	}

	@Override
	public void paintTriangle(Graphics g, int x, int y, int size,
			int direction, boolean isEnabled) {
		x = x/2;
		y = y*2;
		super.paintTriangle(g, x, y, size, direction, isEnabled);
	}



}

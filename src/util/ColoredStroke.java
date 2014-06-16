package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class ColoredStroke extends BasicStroke {

	private Color myColor;

	public ColoredStroke() {
		super();
	}

	public ColoredStroke(float width, int cap, int join, float miterlimit,
			float[] dash, float dash_phase, Color c) {
		super(width, cap, join, miterlimit, dash, dash_phase);
		setColor(c);
	}

	public ColoredStroke(float width, int cap, int join, float miterlimit, Color c) {
		super(width, cap, join, miterlimit);
		setColor(c);
	}

	public ColoredStroke(float width, int cap, int join, Color c) {
		super(width, cap, join);
		setColor(c);
	}

	public ColoredStroke(float width, Color c) {
		super(width);
		setColor(c);
	}

	public Color getColor() {
		return myColor;
	}

	public void setColor(Color color) {
		this.myColor = color;
	}

	public void apply(Graphics2D g2) {
		g2.setStroke(this);
		g2.setColor(myColor);
	}


}

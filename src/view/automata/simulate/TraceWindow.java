/*
 *  JFLAP - Formal Languages and Automata Package
 * 
 * 
 *  Susan H. Rodger
 *  Computer Science Department
 *  Duke University
 *  August 27, 2009

 *  Copyright (c) 2002-2009
 *  All rights reserved.

 *  JFLAP is open source software. Please see the LICENSE for terms.
 *
 */

package view.automata.simulate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import debug.JFLAPDebug;

import model.algorithms.testinput.simulate.Configuration;
import model.algorithms.testinput.simulate.ConfigurationChain;

/**
 * A class that epitomizes the ultimate in bad design: a fusion of model, view,
 * and controller. Look upon my works, ye mighty, and despair.
 * 
 * @author Thomas Finley
 */

public class TraceWindow extends JFrame {
	/** The maximum height these trace displays should get to. */
	private static final int MAXHEIGHT = 400;
	
	/**
	 * Instantiates a new step window with the given configuration.
	 * 
	 * @param last
	 *            the last configuration that we are tracing; we display it
	 *            along with all its ancestors
	 */
	public TraceWindow(ConfigurationChain last) {
		super("Traceback");
		getContentPane().setLayout(new BorderLayout());
		update(last);
	}

	public void update(ConfigurationChain last) {
		getContentPane().removeAll();
		getContentPane().add(getPastPane(last));
		pack();
		if (getSize().height > MAXHEIGHT)
			setSize(getSize().width, MAXHEIGHT);
		setVisible(true);
	}

	/**
	 * Returns a component that displays the ancestry of a configuration.
	 * 
	 * @param configuration
	 *            the configuration whose ancestry we want to display
	 * @return a component with the ancestry of the configuration contained
	 *         within a scroll pane
	 */
	public static JScrollPane getPastPane(ConfigurationChain configuration) {
		JScrollPane sp = new JScrollPane(new PastPane(configuration),
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.revalidate();
		if (sp.getSize().height > MAXHEIGHT) {
			sp.setSize(sp.getSize().width, MAXHEIGHT);
		}
		return sp;
	}

	/**
	 * Returns a component that displays the ancestry of a configuration.
	 * 
	 * @param configuration
	 *            the configuration whose ancestry we want to display
	 * @return a component with the ancestry of the configuration, not contained
	 *         within any scroll pane
	 */
	public static PastPane getPastPaneNoScroll(ConfigurationChain configuration) {
		return new PastPane(configuration);
	}

	/** The pane that displays the past configurations. */
	public static class PastPane extends JComponent {

		private static final int PADDING = 5;
		private Icon[] icons;
		private static final int ARROW_LENGTH = 20;
		
		public PastPane(ConfigurationChain last) {
			setConfiguration(last);
		}

		public void setConfiguration(ConfigurationChain last) {
			LinkedList<Icon> list = new LinkedList<Icon>();
			int height = PADDING;
			int width = 0;

			while (last != null) {
				for (int i = last.size()-1; i>= 0; i--) {
					Icon icon = ConfigurationIconFactory
							.iconForConfiguration(last.get(i));
					width = Math.max(width, icon.getIconWidth());
					height += icon.getIconHeight() + ARROW_LENGTH;
					list.add(icon);
				}
				last = last.getParent();
			}
			width += PADDING*2;
			icons = list.toArray(new Icon[0]);
			this.setPreferredSize(new Dimension(width, height));
		}

		public void paintComponent(Graphics g) {
			Rectangle visible = getVisibleRect();
			int height = ARROW_LENGTH + icons[0].getIconHeight();
			int max = icons.length - 1 - visible.y / height;
			int min = icons.length - 1 - (visible.y + visible.height) / height;
			try {
				min = Math.max(min, 0);
				g = g.create();
				g.translate(0, height * (icons.length - 1 - max));
				for (int i = max; i >= min; i--) {
					drawArrow(g);
					drawIcon(g, icons[i]);
				}
				g.dispose();
			} catch (Throwable e) {
				System.err.println(e);
			}
		}

		public final void drawArrow(Graphics g) {
			int center = getWidth() >> 1;
			g.setColor(Color.black);
			g.drawLine(center, 0, center, ARROW_LENGTH);
			g.drawLine(center, ARROW_LENGTH, center - 10, ARROW_LENGTH - 10);
			g.drawLine(center, ARROW_LENGTH, center + 10, ARROW_LENGTH - 10);
			g.translate(0, ARROW_LENGTH);
		}

		public final void drawIcon(Graphics g, Icon icon) {
			icon.paintIcon(this, g, (getWidth() - icon.getIconWidth()) >> 1, 0);
			g.translate(0, icon.getIconHeight());
		}
	}

}

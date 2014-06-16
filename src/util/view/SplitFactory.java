package util.view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSplitPane;

import universe.JFLAPUniverse;
import view.environment.JFLAPEnvironment;

public class SplitFactory {

	/**
	 * Creates a JSplitPane defined by the parameters passed in and the active
	 * JFLAPEnvironment
	 * 
	 * @param horizontal
	 *            true if split is to be horizontal, vertical otherwise.
	 * @param ratio
	 *            the ratio for splitting the pane.
	 * @param left
	 *            the left or top Component
	 * @param right
	 *            the right or bottom Component
	 */
	public static JSplitPane createSplit(JFLAPEnvironment environment,boolean horizontal, double ratio,
			Component left, Component right) {
		JSplitPane split = new JSplitPane(
				horizontal ? JSplitPane.HORIZONTAL_SPLIT
						: JSplitPane.VERTICAL_SPLIT, true, left, right);
		Dimension dim = environment.getSize();
		Component[] comps = environment.getComponents();
		if (comps.length != 0)
			dim = comps[0].getSize();
		int size = horizontal ? dim.width : dim.height;
		split.setDividerLocation((int) ((double) size * ratio));
		split.setResizeWeight(ratio);
		return split;
	}
}

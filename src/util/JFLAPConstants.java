package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Stroke;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.UIManager;
import javax.swing.border.Border;

import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.ArrowIcon;
import model.grammar.Variable;

public interface JFLAPConstants {

	public static final String JFLAP_SUFFIX = ".jflap",
						JFF_SUFFIX = ".jff",
						LAMBDA = "\u03BB",
						UPPER_LAMBDA = "\u039B",
						EPSILON = "\u03B5",
						BLANK = "\u25A1",
						EMPTY_SET = "\u2205",
						PLUS = "+", BAR = "|",
						UNION = "\u222A", LOGICAL_OR = "\u2228";
	public static final Icon PRODUCTION_ARROW = new ArrowIcon(20, 8);
	public static final String VERSION = "8.0";
	public static final Border DEF_PANEL_BORDER = BorderFactory
			.createLineBorder(Color.BLACK, 3);
	public static final Color DEFAULT_SWING_BG = UIManager
			.getColor("Panel.background"), SPECIAL_SYMBOL = new Color(235, 235,
			150), BAR_SELECTED = new Color(140, 175, 255);
	public static final GroupingPair DEFAULT_GROUPING_PAIR = new GroupingPair('<', '>');

	// STATE CONSTANTS//
	/** The default radius of a state. */
	public static final int STATE_RADIUS = 20;

	// TRANSITION CONSTANTS//
	/** The angle for the arrow heads. */
	public static final double ARROW_ANGLE = Math.PI / 10;
	/** The length of the arrow head edges. */
	public static final double ARROW_LENGTH = 15;
	/** Radius of the transition control point **/
	public static final int CONTROL_POINT_RADIUS = 5;
	/** Height of auto bend **/
	public static final double AUTO_BEND_HEIGHT = 16.123;

	/**
	 * Default font size of a grammar
	 */
	public static Font DEFAULT_FONT = new Font("TimesRoman", Font.PLAIN, 20);
	/** The base color for states. */
	public static final Color DEFAULT_STATE_COLOR = new Color(255, 255, 150);
	public static final Color DEFAULT_SELECTED_COLOR = new Color(100, 200, 200);
	public static final Color DEFAULT_CYK_HIGHLIGHT_COLOR = new Color(255, 255, 66);
	public static final Color DEFAULT_TRANS_COLOR = Color.BLACK;
	public static final Color RED_HIGHLIGHT = new Color(255, 150, 150);
	public static final int EDITOR_CELL_WIDTH = 60;

	public static final Stroke DEFAULT_TRANSITION_STROKE = new BasicStroke(2.4f);
	public static final Color DEFAULT_TRANS_TOOL_COLOR = new Color(.5f, .5f, .5f, .5f);
	public static final Color BACKGROUND_CARET_COLOR = new Color(255, 255, 150);
	// public static final Note BASE_NOTE = new Note("Edit me");
	public static final int EDITOR_CELL_HEIGHT = 13;
	public static final int INITAL_LOOP_HEIGHT = 70;
	public static final int STATE_LABEL_HEIGHT = 17;

	public static final int MAIN_MENU_MASK = (System.getProperty("os.name")
			.lastIndexOf("Windows") != -1 || (System.getProperty("os.name")
			.lastIndexOf("windows") != -1)) ? KeyEvent.CTRL_MASK
			: KeyEvent.META_MASK;

	public static final String TILDE = "~";
	public static final String NOT = "!";
	public static final String TM_MARKER = "#";

	public static final String RESOURCE_ROOT = System.getProperty("user.dir")
			+ "/src/resources";
	public static final String VERSION_STRING = "JFLAP v"
			+ JFLAPConstants.VERSION;

	public static final double DEFAULT_LS_ANGLE = 15.0, DEFAULT_LS_DISTANCE = 15.0, DEFAULT_LS_HUE = 10.0, DEFAULT_LS_INCREM = 1.0, DEFAULT_LS_WIDTH = 1.0;
	public static final int DEFAULT_TM_BUFFER = 5;
	
	public static final Variable JFF_START_SYMBOL= new Variable("S");
}

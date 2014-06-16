package universe.preferences;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import util.JFLAPConstants;
import view.help.PreferenceDialog;

import model.formaldef.components.alphabets.grouping.GroupingPair;
import model.grammar.Terminal;
import model.grammar.Variable;
import model.lsystem.commands.BeginPolygonCommand;
import model.lsystem.commands.DecrementColorCommand;
import model.lsystem.commands.DecrementPColorCommand;
import model.lsystem.commands.DecrementWidthCommand;
import model.lsystem.commands.EndPolygonCommand;
import model.lsystem.commands.ForwardCommand;
import model.lsystem.commands.DrawCommand;
import model.lsystem.commands.IncrementColorCommand;
import model.lsystem.commands.IncrementPColorCommand;
import model.lsystem.commands.IncrementWidthCommand;
import model.lsystem.commands.LeftYawCommand;
import model.lsystem.commands.PitchDownCommand;
import model.lsystem.commands.PitchUpCommand;
import model.lsystem.commands.PopCommand;
import model.lsystem.commands.PushCommand;
import model.lsystem.commands.RightYawCommand;
import model.lsystem.commands.LeftRollCommand;
import model.lsystem.commands.RightRollCommand;
import model.lsystem.commands.YawCommand;
import model.regex.EmptySub;
import model.regex.operators.CloseGroup;
import model.regex.operators.OpenGroup;
import model.regex.operators.UnionOperator;
import model.symbols.PermanentSymbol;
import model.symbols.Symbol;

/**
 * A class designed to hold all of the user preferences associated with JFLAP.
 * This class imports/exports from/to an xml file specifically named:
 * 
 * "preferences.xml"
 * 
 * The file can be adjusted through the Preferences menu in the JFLAP Main Menu
 * or directly via text editors (in the latter case see documentation on file
 * format.)
 * 
 * @author Julian
 * 
 */
public class JFLAPPreferences {

	private static final File PREFERENCE_FILE = new File("preferences.xml");

	private static LinkedList<File> RECENTLY_OPENED;
	private static List<PreferenceChangeListener> LISTENERS;

	static {
		RECENTLY_OPENED = new LinkedList<File>();
		LISTENERS = new ArrayList<PreferenceChangeListener>();
		importFromFile();
	}

	public static void importFromFile() {
		// TODO Auto-generated method stub

	}

	public static final Terminal SLR_MARKER = new Terminal("_");
	public static final String RECENT_CHANGED = "recent_changed";
	public static final String MODE_CHANGED = "mode";
	public static enum PREF_CHANGE { lambda_change, set_change, LSdistance_change,
			LSangle_change, LShue_change, LSwidth_change,
			LSincrement_change, CYK_direction_change, CYK_color_change, TM_buffer_change,
			regex_union_change, regex_group_change, state_color_change, 
			selected_color_change, background_color_change, transition_color_change,
			selected_trans_color_change, grouping_change};
			
	public static JFLAPMode DEFAULT_MODE = JFLAPMode.DEFAULT;

	private static String LAMBDA = JFLAPConstants.LAMBDA;
	private static String EMPTY_SET = JFLAPConstants.EMPTY_SET;
	private static String EMPTY_SUB = "!";

	private static double DEFAULT_LS_DISTANCE = JFLAPConstants.DEFAULT_LS_DISTANCE;
	private static double DEFAULT_LS_ANGLE = JFLAPConstants.DEFAULT_LS_ANGLE;
	private static double DEFAULT_LS_HUE = JFLAPConstants.DEFAULT_LS_HUE;
	private static double DEFAULT_LS_WIDTH = JFLAPConstants.DEFAULT_LS_WIDTH;
	private static double DEFAULT_LS_LINCREMENT = JFLAPConstants.DEFAULT_LS_INCREM;
	private static boolean CYK_DIAGONAL = false;
	private static int DEFAULT_TM_BUFFER = JFLAPConstants.DEFAULT_TM_BUFFER;
	private static String UNION_OPERATOR = JFLAPConstants.PLUS;
	private static String DEFAULT_OPEN_GROUP = "(";
	private static String DEFAULT_CLOSE_GROUP = ")";
	private static GroupingPair DEFAULT_GROUPING_PAIR = JFLAPConstants.DEFAULT_GROUPING_PAIR;

	private static Color STATE_COLOR = JFLAPConstants.DEFAULT_STATE_COLOR;
	private static Color SELECTED_COLOR = JFLAPConstants.DEFAULT_SELECTED_COLOR;
	private static Color CYK_COLOR = JFLAPConstants.DEFAULT_CYK_HIGHLIGHT_COLOR;
	private static Color BACKGROUND_COLOR = JFLAPConstants.DEFAULT_SWING_BG;
	private static Color TRANS_COLOR = JFLAPConstants.DEFAULT_TRANS_COLOR;
	private static Color SELECTED_TRANS_COLOR = JFLAPConstants.RED_HIGHLIGHT;
	
	private static int DEFAULT_TEXT_SIZE = 50;
	private static String FONT_NAME = "Dialog";
	private static int FONT_STYLE = 1;


	public static String getSymbolStringDelimiter() {
		return " ";
	}

	public static String getSymbolizeDelimiter() {
		return " ";
	}

	// //////////////////////////////////////////////////////
	// Symbol getters and setters, with change distribution//
	// //////////////////////////////////////////////////////

	public static String getEmptyString() {
		return LAMBDA;
	}

	public static void setEmptyString(String empty) {
		if (!LAMBDA.equals(empty)) {
			LAMBDA = empty;
			distributeChange(PREF_CHANGE.lambda_change.toString(), LAMBDA);
		}
	}
	
	public static String getEmptySetString() {
		return EMPTY_SET;
	}

	public static void setEmptySetString(String empty) {
		if (!EMPTY_SET.equals(empty)) {
			EMPTY_SET = empty;
			distributeChange(PREF_CHANGE.set_change, EMPTY_SET);
		}
	}

	// //////////////////////////////////////////////////////////////////
	// LSystem Preference getters and setters, with change distribution//
	// //////////////////////////////////////////////////////////////////

	public static double getLSDistance() {
		return DEFAULT_LS_DISTANCE;
	}

	public static void setLSDistance(double distance) {
		if (DEFAULT_LS_DISTANCE != distance) {
			DEFAULT_LS_DISTANCE = distance;
			distributeChange(PREF_CHANGE.LSdistance_change, distance);
		}
	}

	public static double getLSAngle() {
		return DEFAULT_LS_ANGLE;
	}

	public static void setLSAngle(double angle) {
		if (DEFAULT_LS_ANGLE != angle) {
			DEFAULT_LS_ANGLE = angle;
			distributeChange(PREF_CHANGE.LSangle_change, angle);
		}
	}

	public static double getLSHue() {
		return DEFAULT_LS_HUE;
	}

	public static void setLSHue(double hue) {
		if (DEFAULT_LS_HUE != hue) {
			DEFAULT_LS_HUE = hue;
			distributeChange(PREF_CHANGE.LShue_change, hue);
		}
	}

	public static double getLSWidth() {
		return DEFAULT_LS_WIDTH;
	}

	public static void setLSWidth(double width) {
		if (DEFAULT_LS_WIDTH != width) {
			DEFAULT_LS_WIDTH = width;
			distributeChange(PREF_CHANGE.LSwidth_change, width);
		}
	}

	public static double getLSIncrement() {
		return DEFAULT_LS_LINCREMENT;
	}

	public static void setLSIncrement(double line_increment) {
		if (DEFAULT_LS_LINCREMENT != line_increment) {
			DEFAULT_LS_LINCREMENT = line_increment;
			distributeChange(PREF_CHANGE.LSincrement_change, line_increment);
		}
	}

	// ////////////////////////////////////
	// Other preference getters & setters//
	// ////////////////////////////////////

	public static boolean isCYKtableDiagonal() {
		return CYK_DIAGONAL;
	}

	public static void setCYKDiagonal(boolean diagonal) {
		if (CYK_DIAGONAL != diagonal) {
			CYK_DIAGONAL = diagonal;
			distributeChange(PREF_CHANGE.CYK_direction_change, CYK_DIAGONAL);
		}
	}
	
	public static Color getCYKHighlight() {
		return CYK_COLOR;
	}
	
	public static void setCYKHighlight(Color color){
		if(!CYK_COLOR.equals(color)){
			CYK_COLOR = color;
			distributeChange(PREF_CHANGE.CYK_color_change, color);
		}
	}

	public static int getDefaultTMBufferSize() {
		return DEFAULT_TM_BUFFER;
	}

	public static void setDefaultTMBufferSize(int size) {
		if (DEFAULT_TM_BUFFER != size) {
			DEFAULT_TM_BUFFER = size;
			distributeChange(PREF_CHANGE.TM_buffer_change, size);
		}
	}

	public static UnionOperator getUnionOperator() {
		return new UnionOperator(UNION_OPERATOR);
	}

	public static void setUnionOperator(String union) {
		if (!UNION_OPERATOR.equals(union)) {
			UNION_OPERATOR = union;
			distributeChange(PREF_CHANGE.regex_union_change, union);
		}
	}

	public static OpenGroup getCurrentRegExOpenGroup() {
		return new OpenGroup(DEFAULT_OPEN_GROUP);
	}
	public static CloseGroup getCurrentRegExCloseGroup() {
		return new CloseGroup(DEFAULT_CLOSE_GROUP);
	}
	
	public static void setRegexGrouping(String[] group){
		if(!(DEFAULT_OPEN_GROUP.equals(group[0]) && DEFAULT_CLOSE_GROUP.equals(group[1]))){
			DEFAULT_OPEN_GROUP = group[0];
			DEFAULT_CLOSE_GROUP = group[1];
			distributeChange(PREF_CHANGE.regex_group_change, group);
		}
	}
	
	public static Color getStateColor() {
		return STATE_COLOR;
	}
	
	public static void setStateColor(Color color){
		if(!STATE_COLOR.equals(color)){
			STATE_COLOR = color;
			distributeChange(PREF_CHANGE.state_color_change, color);
		}
	}
	
	public static Color getSelectedStateColor() {
		return SELECTED_COLOR;
	}
	
	public static void setSelectedStateColor(Color color){
		if(!SELECTED_COLOR.equals(color)){
			SELECTED_COLOR = color;
			distributeChange(PREF_CHANGE.selected_color_change, color);
		}
	}
	
	public static Color getTransitionColor() {
		return TRANS_COLOR;
	}
	
	public static void setTransitionColor(Color color) {
		if(!TRANS_COLOR.equals(color)){
			TRANS_COLOR = color;
			distributeChange(PREF_CHANGE.transition_color_change, color);
		}
	}
	
	public static Color getSelectedTransitionColor() {
		return SELECTED_TRANS_COLOR;
	}
	
	public static void setSelectedTransitionColor(Color color) {
		if(!SELECTED_TRANS_COLOR.equals(color)){
			SELECTED_TRANS_COLOR = color;
			distributeChange(PREF_CHANGE.selected_trans_color_change, color);
		}
	}
	
	public static Color getBackgroundColor() {
		return BACKGROUND_COLOR;
	}
	
	public static void setBackgroundColor(Color color){
		if(!BACKGROUND_COLOR.equals(color)){
			BACKGROUND_COLOR = color;
			distributeChange(PREF_CHANGE.background_color_change, color);
		}
	}

	public static String getDefaultStateNameBase() {
		return "q";
	}

	public static Symbol getTMBlankSymbol() {
		return new PermanentSymbol(JFLAPConstants.BLANK);
	}

	public static Symbol getEmptySetSymbol() {
		return new Symbol(EMPTY_SET);
	}

	public static BeginPolygonCommand getBeginPolygonCommand() {
		return new BeginPolygonCommand("{");
	}

	public static EndPolygonCommand getEndPolygonCommand() {
		return new EndPolygonCommand("}");
	}

	public static DecrementColorCommand getDColorCommand() {
		return new DecrementColorCommand("@");
	}

	public static IncrementColorCommand getIColorCommand() {
		return new IncrementColorCommand("#");
	}

	public static DecrementPColorCommand getDPolyColorCommand() {
		return new DecrementPColorCommand("@@");
	}

	public static IncrementPColorCommand getIPolyColorCommand() {
		return new IncrementPColorCommand("##");
	}

	public static DecrementWidthCommand getDWidthCommand() {
		return new DecrementWidthCommand("~");
	}

	public static IncrementWidthCommand getIWidthCommand() {
		return new IncrementWidthCommand("!");
	}

	public static ForwardCommand getForwardCommand() {
		return new ForwardCommand("f");
	}

	public static DrawCommand getDrawCommand() {
		return new DrawCommand("g");
	}

	public static LeftYawCommand getLeftYawCommand() {
		return new LeftYawCommand("-");
	}

	public static RightYawCommand getRightYawCommand() {
		return new RightYawCommand("+");
	}

	public static PitchDownCommand getPitchDownCommand() {
		return new PitchDownCommand("&");
	}

	public static PitchUpCommand getPitchUpCommand() {
		return new PitchUpCommand("^");
	}

	public static PushCommand getPushCommand() {
		return new PushCommand("[");
	}

	public static PopCommand getPopCommand() {
		return new PopCommand("]");
	}

	public static RightRollCommand getRightRollCommand() {
		return new RightRollCommand("/");
	}

	public static LeftRollCommand getLeftRollCommand() {
		return new LeftRollCommand("*");
	}

	public static YawCommand getYawCommand() {
		return new YawCommand("%");
	}

	public static EmptySub getSubForEmptyString() {
		return new EmptySub(EMPTY_SUB);
	}
	
	public static String getEmptySubLiteral() {
		return EMPTY_SUB;
	}

	public static Terminal getEndOfStringMarker() {
		return new Terminal("$");
	}

	public static boolean useDefinitionAbbreviations() {
		return false;
	}

	public static Font getFormalDefinitionFont() {
		// TODO: font changing??
		return new Font(getDefinitionFontName(), getDefinitionFontStyle(),
				getDefaultTextSize());
	}

	private static String getDefinitionFontName() {
		return FONT_NAME;
	}

	private static int getDefinitionFontStyle() {
		return FONT_STYLE;
	}

	public static int getDefaultTextSize() {
		return DEFAULT_TEXT_SIZE;
	}

	public static JFLAPMode getDefaultMode() {
		return DEFAULT_MODE;
	}

	public static void setDefaultMode(JFLAPMode mode) {
		if (DEFAULT_MODE != mode) {
			DEFAULT_MODE = mode;
			distributeChange(MODE_CHANGED, mode);
		}
	}

	public static Variable getDefaultStartVariable() {
		return new Variable("S");
	}

	public static GroupingPair getDefaultGrouping() {
		return DEFAULT_GROUPING_PAIR;
	}
	
	public static void setDefaultGrouping(GroupingPair pair){
		if(!DEFAULT_GROUPING_PAIR.equals(pair)){
			DEFAULT_GROUPING_PAIR = pair;
			distributeChange(PREF_CHANGE.grouping_change, pair);
		}
	}

	public static void showPreferenceDisplay() {
		new PreferenceDialog();
	}

	public static int getNumSwitchOptions() {
		return 5;
	}

	public static File[] getRecentlyOpenedFiles() {
		return RECENTLY_OPENED.toArray(new File[0]);
	}

	public static void addRecentlyOpenend(File f) {
		if (!RECENTLY_OPENED.contains(f)) {
			RECENTLY_OPENED.addFirst(f);
			distributeChange(RECENT_CHANGED, RECENTLY_OPENED);
		}
	}
	
	public static void resetColors() {
		setBackgroundColor(JFLAPConstants.DEFAULT_SWING_BG);
		setCYKHighlight(JFLAPConstants.DEFAULT_CYK_HIGHLIGHT_COLOR);
		setStateColor(JFLAPConstants.DEFAULT_STATE_COLOR);
		setSelectedStateColor(JFLAPConstants.DEFAULT_SELECTED_COLOR);
		setTransitionColor(JFLAPConstants.DEFAULT_TRANS_COLOR);
		setSelectedTransitionColor(JFLAPConstants.RED_HIGHLIGHT);
	}

	public static void distributeChange(String s, Object o) {
		for (PreferenceChangeListener l : LISTENERS)
			l.preferenceChanged(s, o);
	}
	
	private static void distributeChange(PREF_CHANGE change,
			Object o) {
		distributeChange(change.toString(), o);
	}

	public static void addChangeListener(PreferenceChangeListener l) {
		LISTENERS.add(l);
	}

	public static void removeChangeListener(PreferenceChangeListener l) {
		LISTENERS.remove(l);
	}
}

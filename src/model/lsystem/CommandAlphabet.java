package model.lsystem;

import universe.preferences.JFLAPPreferences;
import model.formaldef.components.FormalDefinitionComponent;
import model.formaldef.components.alphabets.Alphabet;
import model.grammar.Terminal;
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
import model.symbols.Symbol;

/**
 * Collection of the special symbols used by LSystems to render turtle graphics
 * 
 * @author Ian McMahon
 *
 */
public class CommandAlphabet extends Alphabet{

	private BeginPolygonCommand myBegin;
	private EndPolygonCommand myEnd;
	private DecrementColorCommand myDColor;
	private IncrementColorCommand myIColor;
	private DecrementPColorCommand myDPolyColor;
	private IncrementPColorCommand myIPolyColor;
	private DecrementWidthCommand	myDWidth;
	private IncrementWidthCommand myIWidth;
	private ForwardCommand myForward;
	private DrawCommand myDraw;
	private LeftYawCommand myLeft;
	private RightYawCommand myRight;
	private PitchDownCommand myDown;
	private PitchUpCommand myUp;
	private PopCommand myPop;
	private PushCommand myPush;
	private LeftRollCommand myRLeft;
	private RightRollCommand myRRight;
	private YawCommand myYaw;
	
	public CommandAlphabet(){
		super();
		myBegin = JFLAPPreferences.getBeginPolygonCommand();
		myEnd = JFLAPPreferences.getEndPolygonCommand();
		myDColor = JFLAPPreferences.getDColorCommand();
		myIColor = JFLAPPreferences.getIColorCommand();
		myDPolyColor = JFLAPPreferences.getDPolyColorCommand();
		myIPolyColor = JFLAPPreferences.getIPolyColorCommand();
		myDWidth = JFLAPPreferences.getDWidthCommand();
		myIWidth = JFLAPPreferences.getIWidthCommand();
		myForward = JFLAPPreferences.getForwardCommand();
		myDraw = JFLAPPreferences.getDrawCommand();
		myLeft = JFLAPPreferences.getLeftYawCommand();
		myRight = JFLAPPreferences.getRightYawCommand();
		myDown = JFLAPPreferences.getPitchDownCommand();
		myUp = JFLAPPreferences.getPitchUpCommand();
		myPop = JFLAPPreferences.getPopCommand();
		myPush = JFLAPPreferences.getPushCommand();
		myRLeft = JFLAPPreferences.getLeftRollCommand();
		myRRight = JFLAPPreferences.getRightRollCommand();
		myYaw = JFLAPPreferences.getYawCommand();
		
		this.addAll(myBegin, myEnd, myDColor, myIColor, myDPolyColor,
				myIPolyColor, myDWidth, myIWidth, myForward, myDraw,
				myLeft, myRight, myDown, myUp, myPop, myPush, myRLeft,
				myRRight, myYaw);
	}
	
	public BeginPolygonCommand getBeginPolygon(){
		return myBegin;
	}
	
	public EndPolygonCommand getEndPolygon(){
		return myEnd;
	}
	
	public DecrementColorCommand getDecrementColor(){
		return myDColor;
	}
	
	public IncrementColorCommand getIncrementColor(){
		return myIColor;
	}
	
	public DecrementPColorCommand getDecrementPolygonColor(){
		return myDPolyColor;
	}
	
	public IncrementPColorCommand getIncrementPolygonColor(){
		return myIPolyColor;
	}
	
	public DecrementWidthCommand getDecrementWidth(){
		return myDWidth;
	}
	
	public IncrementWidthCommand getIncrementWidth(){
		return myIWidth;
	}
	
	public ForwardCommand getForward(){
		return myForward;
	}
	
	public DrawCommand getDraw(){
		return myDraw;
	}
	
	public LeftYawCommand getLeftYaw(){
		return myLeft;
	}
	
	public RightYawCommand getRightYaw(){
		return myRight;
	}
	
	public PitchDownCommand getPitchDown(){
		return myDown;
	}
	
	public PitchUpCommand getPitchUp(){
		return myUp;
	}
	
	public PopCommand getPop(){
		return myPop;
	}
	
	public PushCommand getPush(){
		return myPush;
	}
	
	public LeftRollCommand getLeftRoll(){
		return myRLeft;
	}
	
	public RightRollCommand getRightRoll(){
		return myRRight;
	}
	
	public YawCommand getYaw(){
		return myYaw;
	}
	
	@Override
	public String getDescriptionName() {
		return "Commands";
	}

	@Override
	public String getDescription() {
		return "The set of all commands possible in an L-System.";
	}

	@Override
	public String getSymbolName() {
		return "command";
	}

	@Override
	public Character getCharacterAbbr() {
		return 'C';
	}

	@Override
	public CommandAlphabet copy() {
		return (CommandAlphabet) super.copy();
	}
}

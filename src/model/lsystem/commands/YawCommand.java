package model.lsystem.commands;

/**
 * Symbol for LSystem to yaw/turn 180 degrees
 * 
 * @author Ian McMahon
 *
 */
public class YawCommand extends LSystemCommand {

	public YawCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Yaw Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

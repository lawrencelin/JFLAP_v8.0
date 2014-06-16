package model.lsystem.commands;

/**
 * Symbol for LSystem to yaw/turn right
 * 
 * @author Ian McMahon
 *
 */
public class RightYawCommand extends LSystemCommand {

	public RightYawCommand(String s) {
		super(s);
	}
	
	@Override
	public String getDescriptionName() {
		return "Right Turn Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

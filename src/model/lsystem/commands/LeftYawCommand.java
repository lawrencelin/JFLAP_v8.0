package model.lsystem.commands;

/**
 * Symbol for LSystem to yaw/turn left
 * 
 * @author Ian McMahon
 *
 */
public class LeftYawCommand extends LSystemCommand {

	public LeftYawCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Left Turn Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

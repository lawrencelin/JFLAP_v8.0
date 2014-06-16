package model.lsystem.commands;

/**
 * Symbol for LSystem to push a turtle onto the stack
 * 
 * @author Ian McMahon
 *
 */
public class PushCommand extends LSystemCommand {

	public PushCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Push Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

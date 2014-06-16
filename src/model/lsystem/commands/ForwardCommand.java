package model.lsystem.commands;

/**
 * Symbol for LSystem to move forward
 * 
 * @author Ian McMahon
 *
 */
public class ForwardCommand extends LSystemCommand {

	public ForwardCommand(String s) {
		super(s);
	}

	@Override
	public String getDescriptionName() {
		return "Forward Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

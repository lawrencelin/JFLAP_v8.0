package model.lsystem.commands;

/**
 * Symbol for LSystem to roll left
 * 
 * @author Ian McMahon
 *
 */
public class LeftRollCommand extends LSystemCommand {

	public LeftRollCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Roll Left Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

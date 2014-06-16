package model.lsystem.commands;

/**
 * Symbol for LSystem to increment line width
 * 
 * @author Ian McMahon
 *
 */
public class IncrementWidthCommand extends LSystemCommand {

	public IncrementWidthCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Increment Width Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

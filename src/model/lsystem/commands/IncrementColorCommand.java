package model.lsystem.commands;

/**
 * Symbol for LSystem to increment color
 * 
 * @author Ian McMahon
 *
 */
public class IncrementColorCommand extends LSystemCommand {

	public IncrementColorCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Increment Color Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

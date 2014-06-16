package model.lsystem.commands;

/**
 * Symbol for LSystem to decrement the color
 * 
 * @author Ian McMahon
 *
 */
public class DecrementColorCommand extends LSystemCommand {

	public DecrementColorCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Decrement Color Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

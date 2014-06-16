package model.lsystem.commands;

/**
 * Symbol for LSystem to decrement line width
 * 
 * @author Ian McMahon
 *
 */
public class DecrementWidthCommand extends LSystemCommand{

	public DecrementWidthCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Decrement Width Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

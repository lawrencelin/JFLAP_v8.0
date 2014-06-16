package model.lsystem.commands;

/**
 * Symbol for LSystem to decrement the polygon color
 * 
 * @author Ian McMahon
 *
 */
public class DecrementPColorCommand extends LSystemCommand {

	public DecrementPColorCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Decrement Polygon Color Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

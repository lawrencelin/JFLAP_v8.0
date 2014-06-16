package model.lsystem.commands;

/**
 * Symbol for LSystem to increment polygon color
 * 
 * @author Ian McMahon
 *
 */
public class IncrementPColorCommand extends LSystemCommand {

	public IncrementPColorCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getDescriptionName() {
		return "Increment Polygon Color Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

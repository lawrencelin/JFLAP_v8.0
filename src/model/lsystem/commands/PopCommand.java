package model.lsystem.commands;

/**
 * Symbol for LSystem to pop a single turtle from the stack
 * 
 * @author Ian McMahon
 *
 */
public class PopCommand extends LSystemCommand {

	public PopCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Pop Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

package model.lsystem.commands;

/**
 * Symbol for LSystem to pitch down
 * 
 * @author Ian McMahon
 *
 */
public class PitchDownCommand extends LSystemCommand {

	public PitchDownCommand(String s) {
		super(s);
	}
	
	@Override
	public String getDescriptionName() {
		return "Pitch Down Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

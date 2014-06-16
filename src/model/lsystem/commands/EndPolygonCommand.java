package model.lsystem.commands;

/**
 * Symbol for LSystem to stop drawing a polygon
 * 
 * @author Ian McMahon
 *
 */
public class EndPolygonCommand extends LSystemCommand {

	public EndPolygonCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "End Polygon Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

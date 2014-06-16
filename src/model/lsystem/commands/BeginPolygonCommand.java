package model.lsystem.commands;

/**
 * Symbol for LSystem to start drawing a polygon
 * 
 * @author Ian McMahon
 *
 */
public class BeginPolygonCommand extends LSystemCommand {

	public BeginPolygonCommand(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDescriptionName() {
		return "Begin Polygon Command";
	}
	
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}

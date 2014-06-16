package oldnewstuff.util.view.selection;

import java.awt.Shape;

/**
 * Used for selecting items in a draw panel
 * @author Julian Genkins
 *
 */
public interface IBoundsSelection extends ISelectable{
	
	/**
	 * Returns true if the object was selected as determined by how this function
	 * is implemented
	 * @param bounds
	 * @return
	 */
	public boolean isWithinBounds(Shape bounds);

}

package oldnewstuff.view.menus.old.creation;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import jflap.actions.AutomatonAction;
import jflap.actions.CombineAutomatonAction;
import jflap.actions.LambdaHighlightAction;
import jflap.actions.LayoutAlgorithmAction;
import jflap.actions.LayoutStorageAction;
import jflap.actions.MultipleSimulateAction;
import jflap.actions.AutoSimulateAction;
import jflap.actions.NondeterminismAction;
import jflap.actions.SortTransitionsAction;
import jflap.actions.automata.sim.OpenSimulationAction;
import jflap.controller.JFLAPController;
import jflap.model.automaton.Automaton;
import jflap.model.automaton.graph.LayoutAlgorithmFactory;
import jflap.model.automaton.graph.layout.VertexMover;

import JFLAPold.environment.Environment;







public class AutomataMenuCreator extends FormalDefinitionMenuCreator {

	@Override
	public List<JMenuItem> getInputMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(), 
				new JMenuItem(new AutoSimulateAction()),
				new JMenuItem(new MultipleSimulateAction()),
				new JMenuItem(new OpenSimulationAction()));
	}

	@Override
	public List<JMenuItem> getModifyMenuComponents() {
		return combineActionLists(super.getModifyMenuComponents(), 
									getLayoutMenu(),
									new JMenuItem(new SortTransitionsAction()));
	}

	/**
	 * Instantiates the menu holding events concerning the manipulation of object
	 * positions in the window.
	 * 
	 * @param frame
	 *            the environment frame that holds the environment and object
	 * @return a view menu
	 */
	private JMenu getLayoutMenu() {
		JMenu menu = new JMenu("Layout");
//			LayoutStorageAction store = new LayoutStorageAction("Save Current Graph Layout", 
//					"Restore Saved Graph Layout");			
//			menu.add(store);			
//			menu.add(store.getRestoreAction());
			
			
			JMenu viewMenu, subMenu;
			viewMenu = new JMenu("Move Vertices");
			subMenu = new JMenu("Reflect Across Line...");
			subMenu.add(new LayoutAlgorithmAction("Horizontal Line Through Center", VertexMover.HORIZONTAL_CENTER));
			subMenu.add(new LayoutAlgorithmAction("Vertical Line Through Center", VertexMover.VERTICAL_CENTER));
			subMenu.add(new LayoutAlgorithmAction("Diagonal From Upper-Left To Lower-Right", VertexMover.POSITIVE_SLOPE_DIAGONAL));
			subMenu.add(new LayoutAlgorithmAction("Diagonal From Lower-Left To Upper-Right", VertexMover.NEGATIVE_SLOPE_DIAGONAL));
			viewMenu.add(subMenu);
			viewMenu.add(new LayoutAlgorithmAction("Rotate The Graph", VertexMover.ROTATE));
			viewMenu.add(new LayoutAlgorithmAction("Fill Screen With Graph", VertexMover.FILL));
			menu.add(viewMenu);
			
			menu.add(new LayoutAlgorithmAction("Apply A Random Layout Algorithm", LayoutAlgorithmFactory.RANDOM_CHOICE));
			viewMenu = new JMenu("Apply A Specific Layout Algorithm");
			viewMenu.add(new LayoutAlgorithmAction("Circle", LayoutAlgorithmFactory.CIRCLE));
			viewMenu.add(new LayoutAlgorithmAction("GEM", LayoutAlgorithmFactory.GEM));
			viewMenu.add(new LayoutAlgorithmAction("Random", LayoutAlgorithmFactory.RANDOM));
			viewMenu.add(new LayoutAlgorithmAction("Spiral", LayoutAlgorithmFactory.SPIRAL));
			subMenu = new JMenu("Tree");
			subMenu.add(new LayoutAlgorithmAction("Degree", LayoutAlgorithmFactory.TREE_DEGREE));
			subMenu.add(new LayoutAlgorithmAction("Hierarchy", LayoutAlgorithmFactory.TREE_HIERARCHY));
			viewMenu.add(subMenu);
			viewMenu.add(new LayoutAlgorithmAction("Two Circle", LayoutAlgorithmFactory.TWO_CIRCLE));
			menu.add(viewMenu);
//			menu.add(new StateColorSelector(automaton,environment,menu));
		return menu;
	}

	@Override
	public List<JMenuItem> getTestMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(), 
			new JMenuItem(new NondeterminismAction()),
			new JMenuItem(new LambdaHighlightAction()));
	}

	@Override
	public List<JMenuItem> getConvertMenuComponents() {
		return combineActionLists(new ArrayList<JMenuItem>(), 
				new JMenuItem(new CombineAutomatonAction()));
	}

}

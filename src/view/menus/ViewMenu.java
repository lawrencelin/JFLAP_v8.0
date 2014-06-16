package view.menus;

import java.awt.Component;

import javax.swing.JMenu;

import model.graph.LayoutAlgorithmFactory;
import model.graph.layout.VertexMover;
import view.action.automata.LayoutAlgorithmAction;
import view.action.automata.LayoutStorageAction;
import view.automata.views.AutomatonView;
import view.environment.JFLAPEnvironment;
import view.environment.TabChangeListener;
import view.environment.TabChangedEvent;

public class ViewMenu extends JMenu implements TabChangeListener {

	public ViewMenu(JFLAPEnvironment e){
		super("View");
		e.addTabListener(this);
		update(e.getCurrentView());
	}

	@Override
	public void tabChanged(TabChangedEvent e) {
		update(e.getCurrentView());
	}
	
	private void update(Component currentView) {
		this.removeAll();
		setVisible(false);
		
		if(currentView instanceof AutomatonView){
			setVisible(true);
			AutomatonView v = (AutomatonView) currentView;
			LayoutStorageAction store = new LayoutStorageAction("Save Current Graph Layout", 
					"Restore Saved Graph Layout", v);			
			this.add(store);			
			this.add(store.getRestoreAction());
			
			
			JMenu viewMenu, subMenu;
			viewMenu = new JMenu("Move Vertices");
			subMenu = new JMenu("Reflect Across Line...");
			subMenu.add(new LayoutAlgorithmAction("Horizontal Line Through Center", v, VertexMover.HORIZONTAL_CENTER));
			subMenu.add(new LayoutAlgorithmAction("Vertical Line Through Center", v, VertexMover.VERTICAL_CENTER));
			subMenu.add(new LayoutAlgorithmAction("Diagonal From Upper-Left To Lower-Right", v, VertexMover.POSITIVE_SLOPE_DIAGONAL));
			subMenu.add(new LayoutAlgorithmAction("Diagonal From Lower-Left To Upper-Right", v, VertexMover.NEGATIVE_SLOPE_DIAGONAL));
			viewMenu.add(subMenu);
			viewMenu.add(new LayoutAlgorithmAction("Rotate The Graph", v, VertexMover.ROTATE));
			viewMenu.add(new LayoutAlgorithmAction("Fill Screen With Graph", v, VertexMover.FILL));
			this.add(viewMenu);
			
			this.add(new LayoutAlgorithmAction("Apply A Random Layout Algorithm", v, LayoutAlgorithmFactory.RANDOM_CHOICE));
			viewMenu = new JMenu("Apply A Specific Layout Algorithm");
			viewMenu.add(new LayoutAlgorithmAction("Circle", v, LayoutAlgorithmFactory.CIRCLE));
			viewMenu.add(new LayoutAlgorithmAction("GEM", v, LayoutAlgorithmFactory.GEM));
			viewMenu.add(new LayoutAlgorithmAction("Random", v, LayoutAlgorithmFactory.RANDOM));
			viewMenu.add(new LayoutAlgorithmAction("Spiral", v, LayoutAlgorithmFactory.SPIRAL));
			
			subMenu = new JMenu("Tree");
			subMenu.add(new LayoutAlgorithmAction("Degree", v, LayoutAlgorithmFactory.TREE_DEGREE));
			subMenu.add(new LayoutAlgorithmAction("Hierarchy", v, LayoutAlgorithmFactory.TREE_HIERARCHY));
			viewMenu.add(subMenu);
			viewMenu.add(new LayoutAlgorithmAction("Two Circle", v, LayoutAlgorithmFactory.TWO_CIRCLE));
			this.add(viewMenu);
		}
	}

}

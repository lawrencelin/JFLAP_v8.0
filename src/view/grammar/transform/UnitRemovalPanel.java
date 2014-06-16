package view.grammar.transform;

import model.algorithms.transform.grammar.UnitProductionRemover;
import model.grammar.Grammar;
import model.grammar.Production;
import util.view.magnify.MagnifiablePanel;

public class UnitRemovalPanel extends GrammarTransformPanel {

	private UnitProductionRemover myAlg;
//	private UnitRemovalController myController;

	public UnitRemovalPanel(Grammar g, UnitProductionRemover remover) {
		super(g, "Unit Removal");
		myAlg = remover;
//		myController = new UnitRemovalController(this, myAlg);
		initView();
	}

	@Override
	public void productionClicked(Production p) {
		
	}

	@Override
	public MagnifiablePanel initRightPanel() {
		// TODO Auto-generated method stub
		return null;
	}

}

package view.grammar.parsing.cyk;

import java.awt.Dimension;

import model.algorithms.testinput.parse.Parser;
import model.change.events.AdvancedChangeEvent;
import universe.preferences.JFLAPPreferences;
import util.view.magnify.MagnifiableToolbar;
import view.algorithms.toolbar.CYKToolbar;
import view.grammar.parsing.FindFirstParserView;

/**
 * CYK Parser GUI
 * @author Ian McMahon
 *
 */
public class CYKParseView extends FindFirstParserView <CYKParseTablePanel>{

	private static final Dimension CKY_SIZE = new Dimension(817, 725);

	public CYKParseView(Parser alg) {
		super(alg);
		setPreferredSize(CKY_SIZE);
	}

	@Override
	public CYKParseTablePanel createRunningView(Parser alg) {
		CYKParseTablePanel table = new CYKParseTablePanel(alg, JFLAPPreferences.isCYKtableDiagonal());
		return table;
	}
	
	@Override
	public MagnifiableToolbar createToolbar(Parser alg) {
		return new CYKToolbar(getRunningView(), alg, false);
	}
	
	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		Parser alg = getAlgorithm();
		if(!alg.isDone()){
			setStatus("Fill in the next row of the parse table! Enter a space for empty sets.");
		}
		super.updateStatus(e);
	}

}

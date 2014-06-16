package view.grammar.parsing.brute;

import java.awt.Dimension;

import javax.swing.JOptionPane;

import model.algorithms.testinput.parse.Parser;
import model.algorithms.testinput.parse.brute.UnrestrictedBruteParser;
import model.change.events.AdvancedChangeEvent;
import universe.JFLAPUniverse;
import view.algorithms.toolbar.SteppableToolbar;
import view.grammar.parsing.FindFirstParserView;

/**
 * Brute force parse pane.
 * 
 * @author Ian McMahon
 */
public class BruteParserView extends FindFirstParserView<BruteParseTablePanel> {

	public BruteParserView(UnrestrictedBruteParser alg) {
		super(alg);

		setPreferredSize(new Dimension(800, 700));
	}

	@Override
	public void updateStatus(AdvancedChangeEvent e) {
		UnrestrictedBruteParser parser = (UnrestrictedBruteParser) getAlgorithm();
		if (e.comesFrom(parser)) {
			setStatus("Press one of the buttons to continue, restart, or choose a new input.");
			if (e.getType() == UnrestrictedBruteParser.MAX_REACHED) {
				boolean shouldInc = promptForIncreaseCapacity(
						parser.getNumberOfNodes(), e);
				if (shouldInc)
					parser.raiseCapacity(5);
			}

		}
		super.updateStatus(e);
	}

	/**
	 * Opens up a dialog asking the user if they would like to increase the
	 * capacity of generated nodes for the Brute Parser, warning
	 * them of JFLAP freezing.
	 */
	private boolean promptForIncreaseCapacity(int nodeNum, AdvancedChangeEvent e) {

		int n = JFLAPUniverse.getActiveEnvironment()
				.showConfirmDialog(
						"Warning: The parser may slow down drastically and cause JFLAP to freeze if you continue."
								+ "\nNumber of nodes generated: "
								+ nodeNum
								+ " Would you like to continue?");

		return n == JOptionPane.YES_OPTION;
	}

	@Override
	public BruteParseTablePanel createRunningView(Parser alg) {
		BruteParseTablePanel running = new BruteParseTablePanel(
				(UnrestrictedBruteParser) alg);
		return running;
	}

	@Override
	public SteppableToolbar createToolbar(Parser alg) {
		return new SteppableToolbar(alg, false);
	}
}

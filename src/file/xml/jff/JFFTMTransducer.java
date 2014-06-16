package file.xml.jff;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.automata.State;
import model.automata.turing.MultiTapeTMTransition;
import model.automata.turing.MultiTapeTuringMachine;
import model.automata.turing.TuringMachineMove;
import model.symbols.Symbol;

import org.w3c.dom.Element;

import universe.preferences.JFLAPPreferences;
import file.DataException;
import file.xml.XMLHelper;

public class JFFTMTransducer extends
		JFFAutomatonTransducer<MultiTapeTuringMachine, MultiTapeTMTransition> {

	private static final String TURING_TAPES_NAME = "tapes";
	private static final String TRANSITION_TAPE_NAME = "tape";
	private static final String TRANSITION_READ_NAME = "read";
	private static final String TRANSITION_WRITE_NAME = "write";
	private static final String TRANSITION_MOVE_NAME = "move";
	private static final String TM_TAG = "turing";

	@Override
	public String getTag() {
		return TM_TAG;
	}

	@Override
	public MultiTapeTMTransition createTransition(MultiTapeTuringMachine auto,
			State from, State to, Element trans_elem) {
		MultiTapeTuringMachine tm = (MultiTapeTuringMachine) auto;
		int tapes = tm.getNumTapes();
		String[] readStrings = new String[tapes], writeStrings = new String[tapes], moveStrings = new String[tapes];
		// Set defaults in case the transition for that tape is not specified.
		Arrays.fill(readStrings, "");
		Arrays.fill(writeStrings, "");
		Arrays.fill(moveStrings, "R");

		// Avoid undue code duplication.
		Map<String, String[]> tag2array = new HashMap<String, String[]>();
		tag2array.put(TRANSITION_READ_NAME, readStrings);
		tag2array.put(TRANSITION_WRITE_NAME, writeStrings);
		tag2array.put(TRANSITION_MOVE_NAME, moveStrings);

		// Go through the tags.
		for (String tag : tag2array.keySet()) {
			String[] array = tag2array.get(tag);
			List<Element> nodes = XMLHelper.getChildrenWithTag(trans_elem, tag);

			for (Element elem : nodes) {
				// Get which tape this is for.
				String tapeString = elem.getAttribute(TRANSITION_TAPE_NAME);
				if (tapeString.length() == 0)
					tapeString = "1"; // Default single tape.
				int tape = 1;
				try {
					tape = Integer.parseInt(tapeString);
					if (tape < 1 || tape > tapes)
						throw new DataException("In " + tag + " tag, tape "
								+ tape + " identified but only 1-" + tapes
								+ " are valid.");
				} catch (NumberFormatException e) {
					throw new DataException("In " + tag
							+ " tag, error reading " + tapeString + " as tape.");
				}
				// Get the contained text.
				String contained = XMLHelper.containedText(elem);
				if (contained == null)
					contained = "";
				// Set the right text.
				array[tape - 1] = contained;
			}
		}

		// Now, try creating the transition.
		Symbol[] readSymbols = new Symbol[tapes], writeSymbols = new Symbol[tapes];
		TuringMachineMove[] moves = new TuringMachineMove[tapes];
		for (int i = 0; i < tapes; i++) {
			readSymbols[i] = readStrings[i] == null || readStrings[i].isEmpty() ? JFLAPPreferences
					.getTMBlankSymbol() : new Symbol(readStrings[i]);
			writeSymbols[i] = writeStrings[i] == null || writeStrings[i].isEmpty() ? JFLAPPreferences
					.getTMBlankSymbol() : new Symbol(writeStrings[i]);
			moves[i] = moveStrings[i].equals("R") ? TuringMachineMove.RIGHT
					: (moveStrings[i].equals("L") ? TuringMachineMove.LEFT
							: TuringMachineMove.STAY);
		}

		MultiTapeTMTransition t = new MultiTapeTMTransition(from, to,
				readSymbols, writeSymbols, moves);
		return t;
	}

	@Override
	public MultiTapeTuringMachine createAutomaton(Element root) {
		List<Element> tapes_list = XMLHelper.getChildrenWithTag(root,
				TURING_TAPES_NAME);
		String s = "1";

		if (!tapes_list.isEmpty())
			s = XMLHelper.containedText(tapes_list.get(0));
		try {
			int tapes = Integer.parseInt(s);

			return new MultiTapeTuringMachine(tapes);
		} catch (NumberFormatException e) {
			throw new DataException("Error reading " + s
					+ " as number of tapes.");
		}
	}

}

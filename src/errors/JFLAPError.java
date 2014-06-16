package errors;

import java.awt.Component;

import javax.swing.JOptionPane;

public enum JFLAPError {

	BASIC("An error has occurred"),

	INVALID_DEGREE("That is not a valid degree value", "Invalid Input"),

	ILLEGAL_VAR_LOC(
			"You cannot use a variable on the left side of the assignment operator!"
					+ "\nPlease fix this and restart the simulation.",
			"Illegal Variable Location!"),

	NO_INIT_STATE("Simulation requires an automaton\n with an initial state!",
			"No Initial State"), TOO_MANY_STATES(
			"There may be at most 26 states for conversion."),

	NO_INIT_STATE_BB(
			"It appears that one of your building blocks, possibly nested, lacks an initial state.\n "
					+ "Please resolve this problem and restart the simulation.",
			"Missing Initial State"),

	PAREN_AS_TOKEN(
			"Grammar has the ( or ) character(s), which are reserved in current mode.\n"
					+ "See Preferences menu above to toggle this feature.",
			"Production Error"),

	NO_FOLLOW_SET("JFLAP failed to find a follow set."),

	VAR_NOT_FOUND(
			"JFLAP failed to find a variable.  You may have used a variable on the "
					+ "right hand side without providing a derivation for it."),

	NONDETERMINISM("Please remove nondeterminism for simulation.\n"
			+ "Select menu item Test : Highlight Nondeterminism\n"
			+ "to see nondeterministic states.",
			"Nondeterministic states detected"),

	NO_FINAL_STATE("Conversion requires at least\n" + "one final state!",
			"No Final States"),

	S_DERIVES_LAMBDA("WARNING : The start variable derives lambda.\n"
			+ "New Grammar will not produce lambda String.",
			"Start Derives Lambda"),

	ACCEPTS_NO_STRING("Error : This grammar does not accept any Strings. ",
			"Cannot Proceed with CYK"),

	ALREADY_CNF("The grammar is already in Chomsky NF.", "Already in CNF"),

	NEED_NONEMPTY_AXIOM("The axiom must have one or more symbols.",
			"Nonempty Axiom Required"),

	PRIMITIVE_JVM("Sorry, but this uses features requiring Java 1.4 or later!",
			"JVM too primitive"),

	REQUIRES_TM_FILETYPE(
			"Only Turing Machine files can be added as building blocks.",
			"Wrong File Type"),

	INSUF_INPUT_TM("Input file does not have enough input for all tapes",
			"File read error"),

	NEED_TRANS_TO_TRAP("You have to make transition to the trap state!"),

	DFA_NO_LAMBDA("One can't have lambda in the DFA!", "Improper terminal"),

	NOT_NFA("This is not an NFA!", "Not an NFA"),

	TRANS_FROM_FINAL(
			"There are transitions from final states.  Please remove them or change "
					+ "\nthe preference in the \"Preferences\" menu in the JFLAP main menu.",
			"Transitions From Final States"),

	ONLY_FA_PDA_TM(
			"This feature only works with Finite Automata, Pushdown Automata, and Turing Machines"),

	SAME_FILE_TYPE("Files must be of the same type"),

	INCOMPLETE_CONVERSION(
			"Conversion unfinished!  Objects to convert are highlighted.",
			"Conversion Unfinished"),

	CANNOT_ADD_EMPTY_SYMBOL("Cannot add an empty string symbol to any alphabet");

	public String message, title;

	private JFLAPError(String m) {
		this(m, "Error");
	}

	private JFLAPError(String m, String t) {
		message = m;
		title = t;
	}

	public void show() {
		JFLAPError.show(this.message, this.title);
	}

	public static void show(String message, String title) {
		show(null, message, title);
		// Thread.currentThread().interrupt();
	}

	public static void show(Component parent, String message, String title) {
		JOptionPane.showMessageDialog(parent, message, title,
				JOptionPane.ERROR_MESSAGE);
	}

	public static void show(BooleanWrapper ew) {
		JFLAPError.show(ew.getMessage(), "Error");
	}

}

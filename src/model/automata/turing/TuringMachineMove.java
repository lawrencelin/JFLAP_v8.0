package model.automata.turing;

public enum TuringMachineMove {

	RIGHT('R', 1),
	LEFT('L', -1),
	STAY('S', 0);
	
	public char char_abbr;
	public int int_move;

	private TuringMachineMove(char abbr, int move) {
		char_abbr = abbr;
		int_move = move;
	}
	
	public static TuringMachineMove getMove(String source){
		if(source != null && source.length() == 1){
			char move = source.charAt(0);
			if(move == 'R')
				return RIGHT;
			if(move == 'L')
				return LEFT;
			if(move == 'S')
				return STAY;
		}
		return null;
	}
	
	
	@Override
	public String toString() {
		return ""+char_abbr;
	}
}
